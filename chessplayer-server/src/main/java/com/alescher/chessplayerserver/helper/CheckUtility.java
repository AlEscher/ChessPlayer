package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import com.alescher.chessplayerserver.model.Color;

import java.awt.Point;

/**
 * Keep track of which player is currently under check
 * @author AlEscher
 */
public class CheckUtility
{
	private final ChessPiece[][] gameBoard;
	private boolean whiteChecked = false;
	private boolean blackChecked = false;

	public CheckUtility(ChessPiece[][] gameBoard)
	{
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
	 * @param from The starting tile
	 * @param to The tile the piece is moving to
	 */
	public void updateState(Point from, Point to)
	{

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
}
