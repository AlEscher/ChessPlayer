package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Bishop extends ChessPiece
{

	public Bishop(Color color, Point position)
	{
		super(color, position);
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
	public int getValue()
	{
		return 3;
	}

	@Override
	public String toString()
	{
		return "B";
	}
}
