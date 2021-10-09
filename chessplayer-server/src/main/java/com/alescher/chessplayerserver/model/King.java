package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece
{
	public King(Color color, Point position, ChessPiece[][] gameBoard)
	{
		super(color, position, gameBoard);
	}

	@Override
	public boolean isLegalMove(Point moveTo)
	{
		if (Math.abs(position.x - moveTo.x) > 1 || Math.abs(position.y - moveTo.y) > 1)
			return false;

		return true;
	}

	@Override
	public List<Point> getLegalMoves()
	{
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

		BoardUtility.removeIllegalMoves(possibleMoves, this, position, gameBoard);
		return possibleMoves;
	}

	@Override
	public int getValue()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public String toString()
	{
		return "K";
	}
}
