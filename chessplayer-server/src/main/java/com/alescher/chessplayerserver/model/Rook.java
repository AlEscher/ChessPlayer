package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Rook extends ChessPiece
{
	private Color color;

	public Rook(Color color, Point position)
	{
		super(color, position);
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (!BoardUtility.isHorizontalOrVertical(moveFrom, moveTo))
			return false;
		if (!BoardUtility.checkPathUnobstructed(moveFrom, moveTo, gameBoard))
			return false;

		return true;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		// Create directional vectors in vertical and horizontal directions
		List<Point> directions = List.of(
				new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0)
		);

		return BoardUtility.generateLegalMoves(moveFrom, directions, this, gameBoard);
	}

	@Override
	public int getValue()
	{
		return 5;
	}

	@Override
	public String toString()
	{
		return "R";
	}
}
