package com.alescher.chessplayerserver.model;

import java.awt.Point;
import java.util.List;

public class Knight implements ChessPiece
{
	private Color color;

	public Knight(Color color)
	{
		this.color = color;
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
		return null;
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	@Override
	public String toString()
	{
		return "N";
	}
}
