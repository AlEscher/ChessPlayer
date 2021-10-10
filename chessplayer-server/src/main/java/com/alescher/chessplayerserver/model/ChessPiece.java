package com.alescher.chessplayerserver.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.List;

/**
 * An interface that every chess piece implements.
 * It offers functionality such as {@link ChessPiece#isPossibleMove}
 * and {@link ChessPiece#getPossibleMoves}
 *
 * @author AlEscher
 */
public abstract class ChessPiece
{
	/** The current position of the chess piece */
	protected Point position;
	protected Color color;
	protected final ChessPiece[][] gameBoard;

	public ChessPiece(Color color, Point position, ChessPiece[][] gameBoard)
	{
		this.color = color;
		this.position = position;
		this.gameBoard = gameBoard;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;
		ChessPiece piece = (ChessPiece)o;
		return piece.getPosition().equals(this.getPosition()) && piece.getColor() == this.getColor();
	}
	
	/**
	 * Checks whether the move to be performed is allowed for this particular chess piece
	 * @param moveTo The position we want to move to
	 * @return True if the move is allowed, False otherwise
	 */
	public abstract boolean isPossibleMove(@NotNull Point moveTo);

	/**
	 * Calculates all moves that this piece could perform
	 * @return A list of all tiles the piece can move to
	 */
	public abstract List<Point> getPossibleMoves();

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
