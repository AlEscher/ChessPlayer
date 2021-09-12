package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import com.alescher.chessplayerserver.model.Color;

/**
 * Keep track of which player is currently checked
 * @author AlEscher
 */
public class CheckUtility
{
	private ChessPiece[][] gameBoard;
	private Color color;

	public CheckUtility(ChessPiece[][] gameBoard, Color color)
	{
		this.gameBoard = gameBoard;
		this.color = color;
	}
}
