package com.alescher.chessplayerserver.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.List;

/**
 * An interface that every chess piece implements.
 * It offers functionality such as {@link ChessPiece#isLegalMove}
 * and {@link ChessPiece#getLegalMoves}
 *
 * @author AlEscher
 */
public abstract class ChessPiece
{
	/** The current position of the chess piece */
	protected Point position;
	protected Color color;

	public ChessPiece(Color color, Point position)
	{
		this.color = color;
		this.position = position;
	}
	
	/**
	 * Checks whether the move to be performed is allowed for this particular chess piece
	 * @param moveFrom The current position of the piece
	 * @param moveTo The position we want to move to
	 * @param gameBoard The gameboard representing the current state of the chessboard
	 * @return True if the move is allowed, False otherwise
	 */
	public abstract boolean isLegalMove(@NotNull Point moveFrom, @NotNull Point moveTo, @NotNull ChessPiece[][] gameBoard);

	/**
	 * Calculates all moves that this piece is allowed to perform at the moment
	 * @param moveFrom The current position of the piece
	 * @param gameBoard The gameboard representing the current state of the chessboard
	 * @return A list of all tiles the piece can move to
	 */
	public abstract List<Point> getLegalMoves(@NotNull Point moveFrom, @NotNull ChessPiece[][] gameBoard);

	public abstract int getValue();

	public Color getColor()
	{
		return color;
	}

	public Point getPosition()
	{
		return position;
	}

	public void setPosition(@Nullable Point position)
	{
		this.position = position;
	}
}
