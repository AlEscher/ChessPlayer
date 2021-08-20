package com.alescher.chessplayerserver.model;

import java.awt.*;
import java.util.List;

public class Pawn implements ChessPiece
{
	private boolean didMove = false;

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (moveFrom.equals(moveTo))
			return false;

		if (moveFrom.x == moveTo.x) // Pawn is moving forward
		{
			final int distance = Math.abs(moveFrom.y - moveTo.y);

			if (distance > 2) // Pawns can move at most 2 tiles forward
				return false;
			if (distance == 2 && didMove) // Pawns can only move 2 tiles if it is their first move
				return false;
			if (gameBoard[moveFrom.y + 1][moveFrom.x] != null) // Cannot capture when moving forward
				return false;
			if (distance == 2 && gameBoard[moveFrom.y + 2][moveFrom.x] != null)
				return false;

			didMove = true;
			return true; // Legal move forward
		}
		else if (Math.abs(moveFrom.x - moveTo.x) == 1) // Pawn is capturing
		{
			if (Math.abs(moveFrom.y - moveTo.y) > 1)
				return false;
			if (gameBoard[moveTo.y][moveTo.x] == null) // We can only move diagonally if we are capturing a piece
				return false;

			didMove = true;
			return true; // Legal capture
		}

		return false;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "P";
	}
}
