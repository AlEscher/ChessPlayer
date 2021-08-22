package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.ArrayList;
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
		if (!BoardUtility.isDiagonal(moveFrom, moveTo))
			return false;
		if (!BoardUtility.checkPathUnobstructed(moveFrom, moveTo, gameBoard))
			return false;

		return true;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		// Create directional vectors in 4 diagonal directions
		List<Point> directions = List.of(
				new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1)
		);

		return BoardUtility.generateLegalMoves(moveFrom, directions, this, gameBoard);
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
