package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;
import com.alescher.chessplayerserver.helper.CheckUtility;
import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import com.alescher.chessplayerserver.helper.Move;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the chessboard and its current state
 *
 * @author AlEscher
 */
public class ChessBoard
{
	private final ChessPiece[][] gameBoard;
	private final Stack<Move> pastMoves;
	private final CheckUtility checkUtility;
	private Color currentTurn;
	private Color checkMated = null;
	private boolean gameOver = false;
	private static final Logger logger = LoggerFactory.getLogger(ChessBoard.class);

	public ChessBoard()
	{
		this.gameBoard = new ChessPiece[8][8];
		setupBoard();
		this.pastMoves = new Stack<>();
		this.checkUtility = new CheckUtility(this, gameBoard, gameBoard[0][4], gameBoard[7][4]);
		this.currentTurn = Color.WHITE;
	}

	/**
	 * Get all legal moves for a specified chess piece
	 *
	 * @param moveFrom The position of the chess piece
	 * @return A list of points where the piece can move to
	 */
	public List<Point> getLegalMoves(@NotNull Point moveFrom)
	{
		return getLegalMoves(gameBoard[moveFrom.y][moveFrom.x]);
	}

	/**
	 * Get all legal moves for a specified chess piece
	 *
	 * @param piece The chess piece
	 * @return A list of points where the piece can move to
	 */
	public List<Point> getLegalMoves(@Nullable ChessPiece piece)
	{
		if (piece == null) return new ArrayList<>();

		return piece.getPossibleMoves()
				.stream()
				.filter(move -> isLegalMove(piece.getPosition(), move, false))
				.toList();
	}

	/**
	 * Checks whether a move can be performed on the chessboard
	 *
	 * @param fromTile The tile a piece is being moved from, e.g. "A1", "B3", etc...
	 * @param toTile   The tile a piece is being moved to, e.g. "A1", "B3", etc...
	 * @return True if the move is allowed to be performed, false otherwise
	 */
	public boolean isLegalMove(@NotNull String fromTile, @NotNull String toTile)
	{
		if (gameOver) return false;

		Point fromPoint = ChessPositionConverter.convertTileToPoint(fromTile);
		Point toPoint = ChessPositionConverter.convertTileToPoint(toTile);

		logger.info(String.format("Checking move from %s to %s", fromPoint, toPoint));
		// Check bounds
		if (!BoardUtility.checkBounds(fromPoint) || !BoardUtility.checkBounds(toPoint))
			return false;

		boolean isLegal = isLegalMove(fromPoint, toPoint);
		if (isLegal)
		{
			makeMove(fromPoint, toPoint);
			swapTurn();
			checkUtility.detectCheckMate().ifPresent(this::handleCheckMate);
		}

		logger.info(String.format("Move checked. Legal: %b, current turn: %s", isLegal, currentTurn));
		return isLegal;
	}

	private boolean isLegalMove(Point moveFrom, Point moveTo)
	{
		return isLegalMove(moveFrom, moveTo, true);
	}

	private boolean isLegalMove(Point moveFrom, Point moveTo, boolean keepState)
	{
		if (gameBoard[moveFrom.y][moveFrom.x] == null)
			return false;
		if (moveFrom.equals(moveTo))
			return false;
		if (!checkTurn(moveFrom))
			return false;
		if (BoardUtility.checkFriendlyFire(moveFrom, moveTo, gameBoard))
			return false;
		if (!gameBoard[moveFrom.y][moveFrom.x].isPossibleMove(moveTo))
			return false;

		// Simulate the move to see if any player's king is checked
		makeMove(moveFrom, moveTo, false);
		boolean isLegal = checkUtility.isMoveLegal(moveFrom, moveTo, keepState);
		undoMove();

		return isLegal;
	}

	/**
	 * Undoes the last move that was performed.
	 * Removes the undone move and sets the currentTurn back to the previous value
	 */
	private void undoMove()
	{
		Move move = pastMoves.pop();
		gameBoard[move.getFrom().y][move.getFrom().x] = gameBoard[move.getTo().y][move.getTo().x];
		BoardUtility.updatePiecePosition(move.getFrom(), gameBoard);
		gameBoard[move.getTo().y][move.getTo().x] = move.getCapturedPiece();
		BoardUtility.updatePiecePosition(move.getTo(), gameBoard);
	}

	private void handleCheckMate(Color checkMated)
	{
		this.checkMated = checkMated;
		this.gameOver = true;
	}

	/**
	 * Checks that the piece to be moved belongs to the player whose turn it currently is.
	 *
	 * @param moveFrom The piece to be moved
	 * @return True if it's the correct player's turn, false otherwise
	 */
	private boolean checkTurn(Point moveFrom)
	{
		return gameBoard[moveFrom.y][moveFrom.x].getColor() == currentTurn;
	}

	/** Updates the current turn to the next player */
	private void swapTurn()
	{
		currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	/**
	 * Updates the gameboard by performing the specified move. Also logs the
	 * updated gameboard to the console and updates the piece's positions.
	 * Changes to the gameBoard should only be done through this method.
	 *
	 * @param from The position of the piece to be moved
	 * @param to   The position where the piece should be moved to
	 * @param log  If true, the updated chessboard will be logged to the console
	 */
	private void makeMove(Point from, Point to, boolean log)
	{
		// TODO: Handle capture (points update, etc...)
		pastMoves.push(new Move(from, to, gameBoard[to.y][to.x]));

		if (gameBoard[to.y][to.x] != null)
			gameBoard[to.y][to.x].setPosition(null);
		gameBoard[to.y][to.x] = gameBoard[from.y][from.x];
		gameBoard[from.y][from.x] = null;
		BoardUtility.updatePiecePosition(to, gameBoard);

		if (log) logger.info(String.valueOf(this));
	}

	private void makeMove(Point from, Point to)
	{
		makeMove(from, to, true);
	}

	private void setupBoard()
	{
		for (int i = 0; i < 8; i++)
		{
			gameBoard[6][i] = new Pawn(Color.WHITE, Pawn.Direction.UP, new Point(i, 6), gameBoard);
			gameBoard[1][i] = new Pawn(Color.BLACK, Pawn.Direction.DOWN, new Point(i, 1), gameBoard);
		}
		gameBoard[0][0] = new Rook(Color.BLACK, new Point(0, 0), gameBoard);
		gameBoard[0][1] = new Knight(Color.BLACK, new Point(1, 0), gameBoard);
		gameBoard[0][2] = new Bishop(Color.BLACK, new Point(2, 0), gameBoard);
		gameBoard[0][3] = new Queen(Color.BLACK, new Point(3, 0), gameBoard);
		gameBoard[0][4] = new King(Color.BLACK, new Point(4, 0), gameBoard);
		gameBoard[0][5] = new Bishop(Color.BLACK, new Point(5, 0), gameBoard);
		gameBoard[0][6] = new Knight(Color.BLACK, new Point(6, 0), gameBoard);
		gameBoard[0][7] = new Rook(Color.BLACK, new Point(7, 0), gameBoard);

		gameBoard[7][0] = new Rook(Color.WHITE, new Point(0, 7), gameBoard);
		gameBoard[7][1] = new Knight(Color.WHITE, new Point(1, 7), gameBoard);
		gameBoard[7][2] = new Bishop(Color.WHITE, new Point(2, 7), gameBoard);
		gameBoard[7][3] = new Queen(Color.WHITE, new Point(3, 7), gameBoard);
		gameBoard[7][4] = new King(Color.WHITE, new Point(4, 7), gameBoard);
		gameBoard[7][5] = new Bishop(Color.WHITE, new Point(5, 7), gameBoard);
		gameBoard[7][6] = new Knight(Color.WHITE, new Point(6, 7), gameBoard);
		gameBoard[7][7] = new Rook(Color.WHITE, new Point(7, 7), gameBoard);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("\n_________________________________\n");
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				String piece = (gameBoard[i][j] != null) ? gameBoard[i][j].toString() : " ";
				builder.append(String.format("| %s ", piece));
			}
			builder.append("|\n");
		}
		builder.append("---------------------------------");

		return builder.toString();
	}

	public Color getCurrentTurn()
	{
		return currentTurn;
	}

	public Color getCheckMated()
	{
		return checkMated;
	}

}
