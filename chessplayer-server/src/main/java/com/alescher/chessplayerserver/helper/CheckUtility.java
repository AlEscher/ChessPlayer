package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;

/**
 * Keep track of which player is currently under check
 * @author AlEscher
 */
public class CheckUtility
{
	private final ChessPiece[][] gameBoard;

	public CheckUtility(ChessPiece[][] gameBoard)
	{
		this.gameBoard = gameBoard;
	}

	public boolean isUnderCheck()
	{
		return false;
	}
}
