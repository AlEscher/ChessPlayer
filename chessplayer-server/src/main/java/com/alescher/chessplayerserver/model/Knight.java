package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece
{
	public Knight(Color color, Point position, ChessPiece[][] gameBoard)
	{
		super(color, position, gameBoard);
	}

	@Override
	public boolean isLegalMove(Point moveTo)
	{
		if (Math.abs(position.x - moveTo.x) == 2 && Math.abs(position.y - moveTo.y) == 1)
			return true;
		if (Math.abs(position.x - moveTo.x) == 1 && Math.abs(position.y - moveTo.y) == 2)
			return true;

		return false;
	}

	@Override
	public List<Point> getLegalMoves()
	{
		// All the moves a knight can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(position.x + 2, position.y + 1));
		possibleMoves.add(new Point(position.x + 2, position.y - 1));
		possibleMoves.add(new Point(position.x - 2, position.y + 1));
		possibleMoves.add(new Point(position.x - 2, position.y - 1));
		possibleMoves.add(new Point(position.x + 1, position.y + 2));
		possibleMoves.add(new Point(position.x + 1, position.y - 2));
		possibleMoves.add(new Point(position.x - 1, position.y + 2));
		possibleMoves.add(new Point(position.x - 1, position.y - 2));

		BoardUtility.removeIllegalMoves(possibleMoves, this, position, gameBoard);
		return possibleMoves;
	}

	@Override
	public int getValue()
	{
		return 3;
	}

	@Override
	public String toString()
	{
		return "N";
	}
}
