package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.King;

import java.awt.Point;
import java.util.List;

/**
 * Keep track of which player is currently under check
 * @author AlEscher
 */
public class CheckUtility
{
	private final ChessPiece[][] gameBoard;
	private final King whiteKing;
	private final King blackKing;
	private boolean whiteChecked = false;
	private boolean blackChecked = false;

	public CheckUtility(ChessPiece[][] gameBoard, ChessPiece king1, ChessPiece king2)
	{
		if (king1.getColor() == Color.WHITE)
		{
			whiteKing = (King)king1;
			blackKing = (King)king2;
		}
		else
		{
			blackKing = (King)king1;
			whiteKing = (King)king2;
		}
		this.gameBoard = gameBoard;
	}

	/**
	 * Checks if a simulated move is allowed.
	 * A move is illegal if it endangers the own king, i.e. if the own king would be under check after the move.
	 * @param from The starting tile
	 * @param to The tile the piece is moving to
	 * @return <code>true</code> if the move is allowed
	 */
	public boolean isMoveLegal(Point from, Point to)
	{
		updateState(from, to);
		Color player = gameBoard[to.y][to.x].getColor();
		if (player == Color.WHITE)
			return !isWhiteChecked();
		else
			return !isBlackChecked();
	}

	/**
	 * Checks if any player is checked and updates <code>this.whiteChecked</code> and <code>this.blackChecked</code>
	 * accordingly.
	 * @param from The tile the piece moved from
	 * @param to The tile the piece is currently on
	 */
	public void updateState(Point from, Point to)
	{
		// Check if moved piece attacks any king
		gameBoard[to.y][to.x].getLegalMoves().forEach(point -> checkAttacksKing(to, point));
		// TODO Check for discovery attack
		// TODO Update state to see if king is no longer under attack
		// TODO Save attacking pieces
	}

	/**
	 * If any changes have been manually made to the board, {@link CheckUtility#updateState(Point, Point)}
	 * should be called before this method is invoked.
	 * @return <code>true</code> if white is currently checked
	 */
	public boolean isWhiteChecked()
	{
		return whiteChecked;
	}

	/**
	 * If any changes have been manually made to the board, {@link CheckUtility#updateState(Point, Point)}
	 * should be called before this method is invoked.
	 * @return <code>true</code> if black is currently checked
	 */
	public boolean isBlackChecked()
	{
		return blackChecked;
	}

	/**
	 * Check whether this position is inhabited by a king of the opposite color.
	 * Updates <code>whiteChecked</code> and <code>blackChecked</code> accordingly.
	 * @param from The tile of the piece that is attacking
	 * @param to The tile that is being attacked
	 */
	private void checkAttacksKing(Point from, Point to)
	{
		if (gameBoard[to.y][to.x] == null)
			return;

		Color attackingColor = gameBoard[from.y][from.x].getColor();
		ChessPiece attackedPiece = gameBoard[to.y][to.x];
		if (attackedPiece.equals(whiteKing) && attackingColor == Color.BLACK)
		{
			this.whiteChecked = true;
		}
		else if (attackedPiece.equals(blackKing) && attackingColor == Color.WHITE)
		{
			this.blackChecked = true;
		}
	}
}
