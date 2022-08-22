package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece
{
	public Knight(Color color, Point position, ChessGame game)
	{
		super(color, position, game);
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (Math.abs(position.x - moveTo.x) == 2 && Math.abs(position.y - moveTo.y) == 1)
			return true;
		if (Math.abs(position.x - moveTo.x) == 1 && Math.abs(position.y - moveTo.y) == 2)
			return true;

		return false;
	}

	@Override
	public List<Point> calculatePossibleMoves()
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

		removeImpossibleMoves(possibleMoves);
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
