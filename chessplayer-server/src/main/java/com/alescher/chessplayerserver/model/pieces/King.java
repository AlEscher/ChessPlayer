package com.alescher.chessplayerserver.model.pieces;

import com.alescher.chessplayerserver.model.Castle;
import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King extends ChessPiece
{
	private final List<Castle> possibleCastles;

	public King(Color color, Point position, ChessGame game)
	{
		super(color, position, game);
		this.possibleCastles = new ArrayList<>(Arrays.asList(Castle.KINGSIDE, Castle.QUEENSIDE));
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (moveTo.x - getPosition().x == 2 && possibleCastles.contains(Castle.KINGSIDE))
			return true;
		if (moveTo.x - getPosition().x == -2 && possibleCastles.contains(Castle.QUEENSIDE))
			return true;
		if (Math.abs(getPosition().x - moveTo.x) > 1 || Math.abs(getPosition().y - moveTo.y) > 1)
			return false;

		return true;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		Point position = getPosition();
		// All the moves a king can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(position.x - 1, position.y - 1));
		possibleMoves.add(new Point(position.x - 1, position.y + 1));
		possibleMoves.add(new Point(position.x + 1, position.y - 1));
		possibleMoves.add(new Point(position.x + 1, position.y + 1));
		possibleMoves.add(new Point(position.x, position.y - 1));
		possibleMoves.add(new Point(position.x, position.y + 1));
		possibleMoves.add(new Point(position.x - 1, position.y));
		possibleMoves.add(new Point(position.x + 1, position.y));
		possibleMoves.addAll(checkCastle());

		removeImpossibleMoves(possibleMoves);
		return possibleMoves;
	}

	/**
	 * Check whether castling is possible in either direction and return the resulting possible moves
	 * @return The possible castle moves
	 */
	private List<Point> checkCastle()
	{
		List<Point> possibleMoves = new ArrayList<>();
		if (isChecked())
		{
			return possibleMoves;
		}
		if (possibleCastles.contains(Castle.KINGSIDE) && isPathSafe(new Point(1, 0)))
		{
			Point castleDestination = new Point(getPosition().x + 2, getPosition().y);
			if (checkPathUnobstructed(getPosition(), castleDestination))
			{
				possibleMoves.add(castleDestination);
			}
		}
		if (possibleCastles.contains(Castle.QUEENSIDE) && isPathSafe(new Point(-1, 0)))
		{
			Point castleDestination = new Point(getPosition().x - 2, getPosition().y);
			if (checkPathUnobstructed(getPosition(), castleDestination))
			{
				possibleMoves.add(castleDestination);
			}
		}
		return possibleMoves;
	}

	/**
	 * Checks whether the path for a castle is safe (a king cannot castle through an enemy's attack path)
	 * @param direction The direction in which to castle
	 * @return <code>true</code> if the path is safe
	 */
	private boolean isPathSafe(Point direction)
	{
		List<Point> path = List.of(new Point(getPosition().x + direction.x, getPosition().y), new Point(getPosition().x + direction.x * 2, getPosition().y));
		return path.stream().allMatch(point -> getCheckUtility().isMoveLegal(getPosition(), point, false));
	}

	@Override
	public int getValue()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public String toString()
	{
		return getColor() == Color.WHITE ? "K" : "k";
	}

	public List<Castle> getPossibleCastles()
	{
		return possibleCastles;
	}

	/**
	 * Remove one or both castle options, e.g. because the king or a rook moved.
	 * Only use if castling won't be possible at all for the rest of game, e.g. not when the king is under check.
	 * @param castle The castle option(s) to remove
	 */
	public void removeCastleOption(Castle... castle)
	{
		possibleCastles.removeAll(List.of(castle));
	}

	/**
	 * Determin whether this King is currently checked.
	 * @return <code>true</code> if the king is under check
	 */
	public boolean isChecked()
	{
		return getColor() == Color.WHITE ? getCheckUtility().isWhiteChecked() : getCheckUtility().isBlackChecked();
	}
}
