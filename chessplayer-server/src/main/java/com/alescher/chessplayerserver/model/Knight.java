package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece
{
	private Color color;

	public Knight(Color color, Point position)
	{
		super(color, position);
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (Math.abs(moveFrom.x - moveTo.x) == 2 && Math.abs(moveFrom.y - moveTo.y) == 1)
			return true;
		if (Math.abs(moveFrom.x - moveTo.x) == 1 && Math.abs(moveFrom.y - moveTo.y) == 2)
			return true;

		return false;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		// All the moves a knight can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(moveFrom.x + 2, moveFrom.y + 1));
		possibleMoves.add(new Point(moveFrom.x + 2, moveFrom.y - 1));
		possibleMoves.add(new Point(moveFrom.x - 2, moveFrom.y + 1));
		possibleMoves.add(new Point(moveFrom.x - 2, moveFrom.y - 1));
		possibleMoves.add(new Point(moveFrom.x + 1, moveFrom.y + 2));
		possibleMoves.add(new Point(moveFrom.x + 1, moveFrom.y - 2));
		possibleMoves.add(new Point(moveFrom.x - 1, moveFrom.y + 2));
		possibleMoves.add(new Point(moveFrom.x - 1, moveFrom.y - 2));

		BoardUtility.removeIllegalMoves(possibleMoves, this, moveFrom, gameBoard);
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
