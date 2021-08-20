package com.alescher.chessplayerserver.model;

import java.awt.Point;

public class ChessBoard
{
	private final ChessPiece[][] gameBoard = new ChessPiece[8][8];

	public boolean isLegalMove(int xFrom, int yFrom, int xTo, int yTo)
	{
		Point moveFrom = new Point(xFrom, yFrom);
		Point moveTo = new Point(xTo, yTo);

		if (gameBoard[moveFrom.y][moveFrom.x] == null)
			return false;

		return gameBoard[moveFrom.y][moveFrom.x].isLegalMove(moveFrom, moveTo, gameBoard);
	}

	@Override
	public String toString()
	{
		return "";
	}
}
