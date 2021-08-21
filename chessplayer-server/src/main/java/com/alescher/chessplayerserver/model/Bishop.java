package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Bishop implements ChessPiece
{
	private Color color;

	public Bishop(Color color)
	{
		this.color = color;
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (Math.abs(moveFrom.x - moveTo.x) != Math.abs(moveFrom.y - moveTo.y)) // Check diagonal
			return false;
		if (!BoardUtility.checkPathUnobstructed(moveFrom, moveTo, gameBoard))
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
		return "B";
	}
}
