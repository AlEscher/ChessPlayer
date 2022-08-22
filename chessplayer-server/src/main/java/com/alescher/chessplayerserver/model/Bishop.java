package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Bishop extends ChessPiece
{
	public Bishop(Color color, Point position, ChessGame game)
	{
		super(color, position, game);
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (!BoardUtility.isDiagonal(getPosition(), moveTo))
			return false;
		if (!checkPathUnobstructed(getPosition(), moveTo))
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

		return generatePossibleMoves(directions);
	}

	@Override
	public int getValue()
	{
		return 3;
	}

	@Override
	public String toString()
	{
		return getColor() == Color.WHITE ? "B" : "b";
	}
}
