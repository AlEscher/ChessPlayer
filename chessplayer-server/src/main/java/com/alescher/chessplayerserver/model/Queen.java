package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.Point;
import java.util.List;

public class Queen extends ChessPiece
{
	public Queen(Color color, Point position, ChessPiece[][] gameBoard)
	{
		super(color, position, gameBoard);
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (!BoardUtility.isDiagonal(position, moveTo) && !BoardUtility.isHorizontalOrVertical(position, moveTo))
			return false;
		if (!BoardUtility.checkPathUnobstructed(position, moveTo, gameBoard))
			return false;

		return true;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		// Create directional vectors in diagonal, vertical and horizontal directions
		List<Point> directions = List.of(
				new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1),
				new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0)
		);

		return BoardUtility.generatePossibleMoves(position, directions, this, gameBoard);
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
