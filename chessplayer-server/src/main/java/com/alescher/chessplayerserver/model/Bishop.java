package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Bishop extends ChessPiece
{
	public Bishop(Color color, Point position, ChessPiece[][] gameBoard)
	{
		super(color, position, gameBoard);
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (!BoardUtility.isDiagonal(position, moveTo))
			return false;
		if (!BoardUtility.checkPathUnobstructed(position, moveTo, gameBoard))
			return false;

		return true;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		// Create directional vectors in 4 diagonal directions
		List<Point> directions = List.of(
				new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1)
		);

		return BoardUtility.generateLegalMoves(position, directions, this, gameBoard);
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
