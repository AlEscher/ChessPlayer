package com.alescher.chessplayerserver.model.pieces;

import com.alescher.chessplayerserver.helper.BoardUtility;
import com.alescher.chessplayerserver.model.Castle;
import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;

import java.awt.Point;
import java.util.List;

public class Rook extends ChessPiece
{
	// The side on which this rook is
	private final Castle side;
	public Rook(Color color, Point position, ChessGame game)
	{
		super(color, position, game);
		side = getPosition().x == 0 ? Castle.QUEENSIDE : Castle.KINGSIDE;
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (!BoardUtility.isHorizontalOrVertical(getPosition(), moveTo))
			return false;
		if (!checkPathUnobstructed(getPosition(), moveTo))
			return false;

		return true;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		// Create directional vectors in vertical and horizontal directions
		List<Point> directions = List.of(
				new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0)
		);

		return generatePossibleMoves(directions);
	}

	@Override
	public int getValue()
	{
		return 5;
	}

	@Override
	public String toString()
	{
		return getColor() == Color.WHITE ? "R" : "r";
	}

	public Castle getSide()
	{
		return side;
	}
}
