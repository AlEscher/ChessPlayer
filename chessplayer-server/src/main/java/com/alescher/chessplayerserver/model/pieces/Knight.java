package com.alescher.chessplayerserver.model.pieces;

import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece
{
	public Knight(com.alescher.chessplayerserver.model.Color color, Point position, ChessGame game)
	{
		super(color, position, game);
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		if (Math.abs(getPosition().x - moveTo.x) == 2 && Math.abs(getPosition().y - moveTo.y) == 1)
			return true;
		if (Math.abs(getPosition().x - moveTo.x) == 1 && Math.abs(getPosition().y - moveTo.y) == 2)
			return true;

		return false;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		Point position = getPosition();
		// All the moves a knight can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(position.x + 2, position.y + 1));
		possibleMoves.add(new Point(position.x + 2, position.y - 1));
		possibleMoves.add(new Point(position.x - 2, position.y + 1));
		possibleMoves.add(new Point(position.x - 2, position.y - 1));
		possibleMoves.add(new Point(position.x + 1, position.y + 2));
		possibleMoves.add(new Point(position.x + 1, position.y - 2));
		possibleMoves.add(new Point(position.x - 1, position.y + 2));
		possibleMoves.add(new Point(position.x - 1, position.y - 2));

		removeImpossibleMoves(possibleMoves);
		return possibleMoves;
	}

	@Override
	public int getValue()
	{
		return 3;
	}

	@Override
	public String toString()
	{
		return getColor() == Color.WHITE ? "N" : "n";
	}
}
