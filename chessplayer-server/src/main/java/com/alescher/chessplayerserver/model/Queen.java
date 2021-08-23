package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Queen implements ChessPiece
{
	private Color color;

	public Queen(Color color)
	{
		this.color = color;
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (!BoardUtility.isDiagonal(moveFrom, moveTo) && !BoardUtility.isHorizontalOrVertical(moveFrom, moveTo))
			return false;
		if (!BoardUtility.checkPathUnobstructed(moveFrom, moveTo, gameBoard))
			return false;

		return true;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		// Create directional vectors in diagonal, vertical and horizontal directions
		List<Point> directions = List.of(
				new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1),
				new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0)
		);

		return BoardUtility.generateLegalMoves(moveFrom, directions, this, gameBoard);
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	@Override
	public int getValue()
	{
		return 9;
	}

	@Override
	public String toString()
	{
		return "Q";
	}
}
