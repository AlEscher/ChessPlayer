package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.*;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;
import com.alescher.chessplayerserver.model.pieces.King;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Keep track of which player is currently under check
 * @author AlEscher
 */
public class CheckUtility
{
	private final ChessGame chessGame;
	private final ChessPiece[][] gameBoard;
	// White pieces that are attacking the black king
	private List<ChessPiece> whiteAttackers;
	// Black pieces that are attacking the white king
	private List<ChessPiece> blackAttackers;
	private static final Logger logger = LoggerFactory.getLogger(CheckUtility.class);

	public CheckUtility(ChessGame chessGame, ChessPiece[][] gameBoard)
	{
		this.chessGame = chessGame;
		this.gameBoard = gameBoard;
		this.whiteAttackers = new ArrayList<>();
		this.blackAttackers = new ArrayList<>();
	}

	/**
	 * Checks if a simulated move is allowed.
	 * A move is illegal if it endangers the own king, i.e. if the own king would be under check after the move.
	 * @param from The tile the piece moved from
	 * @param to The tile the piece is currently on
	 * @param keepState Whether the computed state should be kept or reset before the function exits
	 * @return <code>true</code> if the move is allowed
	 */
	public boolean isMoveLegal(Point from, Point to, boolean keepState)
	{
		if (gameBoard[from.y][from.x] instanceof King && Math.abs(from.x - to.x) > 1)
		{
			// King is castling, a king cannot castle if it is checked or is castling "through" a check
			return isCastleLegal(from, to);
		}
		// Simulate the move to see if any player's king is checked
		chessGame.simulateMove(from, to, false);
		Assert.state(!(gameBoard[to.y][to.x].getColor() == Color.WHITE && isBlackChecked()),
				"A player should not be making a move if the enemies king was already under check");
		Assert.state(!(gameBoard[to.y][to.x].getColor() == Color.BLACK && isWhiteChecked()),
				"A player should not be making a move if the enemies king was already under check");

		if (!checkPreviousAttackers())
		{
			chessGame.undoMove();
			return false;
		}

		CheckState backup = new CheckState(whiteAttackers, blackAttackers);

		whiteAttackers.clear();
		blackAttackers.clear();
		// No previous threats, update the state for the new simulated move
		updateState();
		Color player = gameBoard[to.y][to.x].getColor();
		boolean isLegal = (player == Color.WHITE) ? !isWhiteChecked() : !isBlackChecked();

		if (!keepState || !isLegal) resetState(backup);

		chessGame.undoMove();
		return isLegal;
	}

	/**
	 * Checks if any player is checked after simulating a move and updates <code>whiteAttackers</code> and <code>blackAttackers</code>
	 * accordingly.
	 */
	private void updateState()
	{
		logger.info(String.format("Updating CheckUtility state. Current: %s", this));
		// Check if a piece attacks a king of the opposite color
		BoardUtility.getAllPieces(gameBoard).filter(Objects::nonNull).forEach(chessPiece -> {
			if (!chessPiece.getClass().equals(King.class))
			{
				chessPiece.getPossibleMoves().forEach(point -> this.checkAttacksKing(chessPiece.getPosition(), point));
			}
		});
		logger.info(String.format("CheckUtility updated: %s", this));
	}

	/**
	 * @return <code>true</code> if white is currently checked
	 */
	public boolean isWhiteChecked()
	{
		return this.blackAttackers.size() > 0;
	}

	/**
	 * @return <code>true</code> if black is currently checked
	 */
	public boolean isBlackChecked()
	{
		return this.whiteAttackers.size() > 0;
	}

	public Optional<Color> detectCheckMate()
	{
		if (!isWhiteChecked() && !isBlackChecked()) return Optional.empty();

		Color color = isWhiteChecked() ? Color.WHITE : Color.BLACK;
		// Check if any piece of this color can still make a move
		boolean canMove = BoardUtility.getAllPieces(gameBoard)
				.anyMatch(chessPiece -> chessPiece != null && chessPiece.getColor() == color && !chessGame.getLegalMoves(chessPiece).isEmpty());

		return canMove ? Optional.empty() : Optional.of(color);
	}

	/**
	 * Check whether the attacked position is inhabited by a king of the opposite color.
	 * @param from The tile of the piece that is attacking
	 * @param to The tile that is being attacked
	 * @return Optional containing the king being attacked, empty if no king is attacked.
	 */
	private Optional<ChessPiece> getAttackedKing(Point from, Point to)
	{
		if (gameBoard[to.y][to.x] == null)
			return Optional.empty();

		ChessPiece attackingPiece = gameBoard[from.y][from.x];
		ChessPiece attackedPiece = gameBoard[to.y][to.x];
		if ((attackedPiece.equals(chessGame.getWhiteKing()) && attackingPiece.getColor() == Color.BLACK)
		 || (attackedPiece.equals(chessGame.getBlackKing()) && attackingPiece.getColor() == Color.WHITE))
		{
			return Optional.of(attackedPiece);
		}

		return Optional.empty();
	}

	/**
	 * Check whether the attacked position is inhabited by a king of the opposite color.
	 * Updates <code>whiteAttackers</code> and <code>blackAttackers</code> accordingly.
	 * @param from The tile of the piece that is attacking
	 * @param to The tile that is being attacked
	 */
	private void checkAttacksKing(Point from, Point to)
	{
		Optional<ChessPiece> attackedKing = getAttackedKing(from, to);
		attackedKing.ifPresent(king -> handleCheck(gameBoard[from.y][from.x], king));
	}

	/** Sets the correct check-attributes and adds the attacker to the correct list */
	private void handleCheck(ChessPiece attacker, ChessPiece king)
	{
		if (king.getColor() == Color.WHITE)
		{
			blackAttackers.add(attacker);
		}
		else if (king.getColor() == Color.BLACK)
		{
			whiteAttackers.add(attacker);
		}
	}

	/**
	 * Check whether previous threats have been eliminated
	 * @return <code>true</code> if there are no current threats, <code>false</code> otherwise
	 */
	private boolean checkPreviousAttackers()
	{
		Assert.state(this.blackAttackers.isEmpty() || this.whiteAttackers.isEmpty(),
				"Only one king should be under attack at any given time");

		List<ChessPiece> currentAttackers = blackAttackers.isEmpty() ? whiteAttackers : blackAttackers;
		logger.info(String.format("Checking previous %d attackers", currentAttackers.size()));
		// Filter out all attackers that are no longer a threat
		List<ChessPiece> remainingAttackers = currentAttackers
				.stream()
				.filter(chessPiece ->
					chessPiece.getPossibleMoves()
							.stream()
							.anyMatch(point -> getAttackedKing(chessPiece.getPosition(), point).isPresent()))
				.toList();
		logger.info(String.format("%d attackers remain", remainingAttackers.size()));

		return remainingAttackers.isEmpty();
	}

	private boolean isCastleLegal(Point from, Point to)
	{
		CheckState backup = new CheckState(whiteAttackers, blackAttackers);
		King king = (King) gameBoard[from.y][from.x];
		boolean legal = true;
		updateState();
		// King cannot castle if under check
		if ((king.isWhite() && isWhiteChecked()) || king.isBlack() && isBlackChecked())
		{
			legal = false;
		}
		resetState(backup);
		Point direction = BoardUtility.normalizeDirectionalVector(BoardUtility.getDirectionalVector(from, to));
		// Points from the king's current position to where he will castle
		List<Point> intermediateMoves = BoardUtility.generatePossibleMoves(from, List.of(direction), king, gameBoard);
		for (Point moveTo : intermediateMoves)
		{
			// Simulate each intermediate move and check if any enemy piece would be attacking that square
			chessGame.simulateMove(from, moveTo, false);
			updateState();
			boolean underCheck = (king.isWhite() && isWhiteChecked()) || king.isBlack() && isBlackChecked();
			if (underCheck)
			{
				return false;
			}
			chessGame.undoMove();
			resetState(backup);
		}
		return legal;
	}

	/** Reset this objects internal state to a previously saved one */
	private void resetState(CheckState oldState)
	{
		this.whiteAttackers = oldState.whiteAttackers;
		this.blackAttackers = oldState.blackAttackers;
	}

	@Override
	public String toString()
	{
		return String.format("{ whiteChecked: %b, blackAttackers: %s, blackChecked: %b, whiteAttackers: %s }",
				isWhiteChecked(), this.blackAttackers, isBlackChecked(), this.whiteAttackers);
	}

	private record CheckState(List<ChessPiece> whiteAttackers,
	                         List<ChessPiece> blackAttackers)
	{
		private CheckState(List<ChessPiece> whiteAttackers, List<ChessPiece> blackAttackers)
		{
			this.whiteAttackers = new ArrayList<>(whiteAttackers);
			this.blackAttackers = new ArrayList<>(blackAttackers);
		}
	}
}
