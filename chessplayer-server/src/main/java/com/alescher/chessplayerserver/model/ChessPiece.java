package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;
import com.alescher.chessplayerserver.helper.CheckUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.ArrayList;
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
	protected final ChessGame game;

	public ChessPiece(Color color, Point position, ChessGame game)
	{
		this.color = color;
		this.position = position;
		this.game = game;
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
	public boolean isPossibleMove(@NotNull Point moveTo)
	{
		if (position == null)
			return false;

		return checkMove(moveTo);
	}

	/**
	 * Calculates all moves that this piece could perform
	 * @return A list of all tiles the piece can move to
	 */
	public List<Point> getPossibleMoves()
	{
		if (position == null)
			return new ArrayList<>();

		return calculatePossibleMoves();
	}

	public Color getColor()
	{
		return color;
	}

	/**@return The piece's position, <code>null</code> if this piece is no longer on the board */
	public Point getPosition()
	{
		return position;
	}

	public abstract int getValue();

	/**
	 * Update the piece's position
	 * @param position The new position, <code>null</code> if this piece was captured
	 */
	public void setPosition(@Nullable Point position)
	{
		this.position = position;
	}

	public boolean isWhite()
	{
		return getColor() == Color.WHITE;
	}

	public boolean isBlack()
	{
		return getColor() == Color.BLACK;
	}

	protected abstract List<Point> calculatePossibleMoves();

	protected abstract boolean checkMove(Point to);

	protected ChessPiece[][] getGameBoard()
	{
		return game.getGameBoard();
	}

	protected CheckUtility getCheckUtility()
	{
		return game.getCheckUtility();
	}

	protected boolean checkPathUnobstructed(Point from, Point to)
	{
		return BoardUtility.checkPathUnobstructed(from, to, getGameBoard());
	}

	protected List<Point> generatePossibleMoves(List<Point> directions)
	{
		return BoardUtility.generatePossibleMoves(position, directions, this, getGameBoard());
	}

	protected void removeImpossibleMoves(List<Point> possibleMoves)
	{
		BoardUtility.removeImpossibleMoves(possibleMoves, this, position, getGameBoard());
	}
}
