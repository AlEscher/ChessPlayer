package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.controller.ChessplayerController;
import com.alescher.chessplayerserver.helper.BoardUtility;
import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import org.jetbrains.annotations.NotNull;

import java.awt.Point;

/**
 * Represents the chessboard and its current state
 *
 * @author AlEscher
 */
public class ChessBoard
{
	private final ChessPiece[][] gameBoard = new ChessPiece[8][8];

	public ChessBoard()
	{
		setupBoard();
	}

	/**
	 * Checks whether a move can be performed on the chessboard
	 * @param fromTile The tile a piece is being moved from, e.g. "A1", "B3", etc...
	 * @param toTile The tile a piece is being moved to, e.g. "A1", "B3", etc...
	 * @return True if the move is allowed to be performed, false otherwise
	 */
	public boolean isLegalMove(@NotNull String fromTile, @NotNull String toTile)
	{
		Point fromPoint = ChessPositionConverter.convertTileToPoint(fromTile);
		Point toPoint = ChessPositionConverter.convertTileToPoint(toTile);

		ChessplayerController.logger.info(String.format("Checking move from %s to %s", fromPoint.toString(), toPoint.toString()));
		// Check bounds
		if (!BoardUtility.checkBounds(fromPoint) || !BoardUtility.checkBounds(toPoint))
			return false;

		boolean isLegal = isLegalMove(fromPoint, toPoint);
		if (isLegal) makeMove(fromPoint, toPoint); // If the move is allowed, update our gameBoard

		return isLegal;
	}

	private boolean isLegalMove(Point moveFrom, Point moveTo)
	{
		if (gameBoard[moveFrom.y][moveFrom.x] == null)
			return false;
		if (moveFrom.equals(moveTo))
			return false;
		if (BoardUtility.checkFriendlyFire(moveFrom, moveTo, gameBoard))
			return false;

		return gameBoard[moveFrom.y][moveFrom.x].isLegalMove(moveFrom, moveTo, gameBoard);
	}

	private void makeMove(Point from, Point to)
	{
		// TODO: Handle capture (points update, etc...)
		gameBoard[to.y][to.x] = gameBoard[from.y][from.x];
		gameBoard[from.y][from.x] = null;
		ChessplayerController.logger.info(String.valueOf(this));
	}

	private void setupBoard()
	{
		for (int i = 0; i < 8; i++)
		{
			gameBoard[6][i] = new Pawn(Color.WHITE, Pawn.Direction.UP);
			gameBoard[1][i] = new Pawn(Color.BLACK, Pawn.Direction.DOWN);
		}
		gameBoard[0][0] = new Rook(Color.BLACK);
		gameBoard[0][1] = new Knight(Color.BLACK);
		gameBoard[0][2] = new Bishop(Color.BLACK);
		gameBoard[0][3] = new King(Color.BLACK);
		gameBoard[0][4] = new Queen(Color.BLACK);
		gameBoard[0][5] = new Bishop(Color.BLACK);
		gameBoard[0][6] = new Knight(Color.BLACK);
		gameBoard[0][7] = new Rook(Color.BLACK);

		gameBoard[7][0] = new Rook(Color.WHITE);
		gameBoard[7][1] = new Knight(Color.WHITE);
		gameBoard[7][2] = new Bishop(Color.WHITE);
		gameBoard[7][3] = new King(Color.WHITE);
		gameBoard[7][4] = new Queen(Color.WHITE);
		gameBoard[7][5] = new Bishop(Color.WHITE);
		gameBoard[7][6] = new Knight(Color.WHITE);
		gameBoard[7][7] = new Rook(Color.WHITE);
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
}
