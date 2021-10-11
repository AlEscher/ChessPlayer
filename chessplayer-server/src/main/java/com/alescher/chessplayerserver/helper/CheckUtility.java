package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.King;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Keep track of which player is currently under check
 * @author AlEscher
 */
public class CheckUtility
{
	private final ChessPiece[][] gameBoard;
	private final King whiteKing;
	private final King blackKing;
	private List<ChessPiece> whiteAttackers;
	private List<ChessPiece> blackAttackers;
	private static final Logger logger = LoggerFactory.getLogger(CheckUtility.class);
	private boolean whiteChecked = false;
	private boolean blackChecked = false;

	public CheckUtility(ChessPiece[][] gameBoard, ChessPiece king1, ChessPiece king2)
	{
		if (king1.getColor() == Color.WHITE)
		{
			this.whiteKing = (King)king1;
			this.blackKing = (King)king2;
		}
		else
		{
			this.blackKing = (King)king1;
			this.whiteKing = (King)king2;
		}
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
		// A player should not be making a move if the enemies king was already under check
		assert !(gameBoard[to.y][to.x].getColor() == Color.WHITE && this.blackChecked);
		assert !(gameBoard[to.y][to.x].getColor() == Color.BLACK && this.whiteChecked);

		if (!checkPreviousAttackers())
			return false;

		CheckState backup = new CheckState(whiteChecked, blackChecked, whiteAttackers, blackAttackers);

		whiteAttackers.clear();
		blackAttackers.clear();
		whiteChecked = blackChecked = false;
		// No previous threats, update the state for the new simulated move
		updateState(from, to);
		Color player = gameBoard[to.y][to.x].getColor();
		boolean isLegal = (player == Color.WHITE) ? !isWhiteChecked() : !isBlackChecked();

		if (!keepState) resetState(backup);

		return isLegal;
	}

	/**
	 * Checks if any player is checked and updates <code>this.whiteChecked</code> and <code>this.blackChecked</code>
	 * accordingly.
	 * @param from The tile the piece moved from
	 * @param to The tile the piece is currently on
	 */
	public void updateState(Point from, Point to)
	{
		logger.info(String.format("Updating CheckUtility state. Current: %s", this));
		// Check if moved piece attacks any king
		gameBoard[to.y][to.x].getPossibleMoves().forEach(point -> checkAttacksKing(to, point));
		logger.info(String.format("CheckUtility updated: %s", this));
		// TODO Check for discovery attack
	}

	/**
	 * If any changes have been manually made to the board, {@link CheckUtility#updateState(Point, Point)}
	 * should be called before this method is invoked.
	 * @return <code>true</code> if white is currently checked
	 */
	public boolean isWhiteChecked()
	{
		return this.whiteChecked;
	}

	/**
	 * If any changes have been manually made to the board, {@link CheckUtility#updateState(Point, Point)}
	 * should be called before this method is invoked.
	 * @return <code>true</code> if black is currently checked
	 */
	public boolean isBlackChecked()
	{
		return this.blackChecked;
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
		if ((attackedPiece.equals(whiteKing) && attackingPiece.getColor() == Color.BLACK)
		 || (attackedPiece.equals(blackKing) && attackingPiece.getColor() == Color.WHITE))
		{
			return Optional.of(attackedPiece);
		}

		return Optional.empty();
	}

	/**
	 * Check whether the attacked position is inhabited by a king of the opposite color.
	 * Updates <code>whiteChecked</code> and <code>blackChecked</code> accordingly.
	 * @param from The tile of the piece that is attacking
	 * @param to The tile that is being attacked
	 */
	private void checkAttacksKing(Point from, Point to)
	{
		Optional<ChessPiece> attackedKing = getAttackedKing(from, to);
		if (attackedKing.isEmpty())
		{
			return;
		}
		else if (attackedKing.get().getColor() == Color.WHITE)
		{
			whiteChecked = true;
			blackAttackers.add(gameBoard[from.y][from.x]);
		}
		else if (attackedKing.get().getColor() == Color.BLACK)
		{
			blackChecked = true;
			whiteAttackers.add(gameBoard[from.y][from.x]);
		}
	}

	/**
	 * Check whether previous threats have been eliminated
	 * @return <code>true</code> if there are no current threats, <code>false</code> otherwise
	 */
	private boolean checkPreviousAttackers()
	{
		// Only one king can be under attack at any given time
		assert (this.blackAttackers.isEmpty() || this.whiteAttackers.isEmpty());

		List<ChessPiece> currentAttackers = blackAttackers.isEmpty() ? whiteAttackers : blackAttackers;
		logger.info(String.format("Checking previous %d attackers", currentAttackers.size()));
		// Filter out all attackers that are no longer a threat
		List<ChessPiece> remainingAttackers = currentAttackers
				.stream()
				.filter(chessPiece ->
					chessPiece.getPossibleMoves()
							.stream()
							.anyMatch(point -> !getAttackedKing(chessPiece.getPosition(), point).isEmpty()))
				.toList();
		logger.info(String.format("%d attackers remain", remainingAttackers.size()));

		return remainingAttackers.isEmpty();
	}

	/** Reset this objects internal state to a previously saved one */
	private void resetState(CheckState oldState)
	{
		this.whiteChecked = oldState.whiteChecked;
		this.blackChecked = oldState.blackChecked;
		this.whiteAttackers = oldState.whiteAttackers;
		this.blackAttackers = oldState.blackAttackers;
	}

	@Override
	public String toString()
	{
		return String.format("{ whiteChecked: %b, blackAttackers: %s, blackChecked: %b, whiteAttackers: %s }",
				this.whiteChecked, this.blackAttackers, this.blackChecked, this.whiteAttackers);
	}

	private record CheckState(boolean whiteChecked, boolean blackChecked,
	                         List<ChessPiece> whiteAttackers,
	                         List<ChessPiece> blackAttackers)
	{
		private CheckState(boolean whiteChecked, boolean blackChecked, List<ChessPiece> whiteAttackers, List<ChessPiece> blackAttackers)
		{
			this.whiteChecked = whiteChecked;
			this.blackChecked = blackChecked;
			this.whiteAttackers = new ArrayList<>(whiteAttackers);
			this.blackAttackers = new ArrayList<>(blackAttackers);
		}
	}
}
