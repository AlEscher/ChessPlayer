package com.alescher.chessplayerserver.model;

import java.awt.Point;
import java.util.List;

public class King implements ChessPiece
{
	private Color color;

	public King(Color color)
	{
		this.color = color;
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (Math.abs(moveFrom.x - moveTo.x) > 1 || Math.abs(moveFrom.y - moveTo.y) > 1)
			return false;

		return true;
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
		return "K";
	}
}
