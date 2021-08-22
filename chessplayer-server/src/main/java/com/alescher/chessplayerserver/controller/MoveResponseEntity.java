package com.alescher.chessplayerserver.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class that represents a chess move
 */
public class MoveResponseEntity
{
	private String fromTile;
	private String toTile;
	private String pieceID;
	private boolean legal;
	private List<Point> possibleMoves = new ArrayList<>();

	@Override
	public String toString()
	{
		return String.format("{ fromTile: %s, toTile: %s, pieceID: %s, legal: %b, possibleMoves: %s }",
				fromTile, toTile, pieceID, legal, possibleMoves.toString());
	}

	public String getFromTile()
	{
		return fromTile;
	}

	public String getToTile()
	{
		return toTile;
	}

	public String getPieceID()
	{
		return pieceID;
	}

	public boolean isLegal()
	{
		return legal;
	}

	public List<Point> getPossibleMoves()
	{
		return possibleMoves;
	}

	public MoveResponseEntity(String fromTile, String toTile, String pieceID, boolean legal, List<Point> possibleMoves)
	{
		this.fromTile = fromTile;
		this.toTile = toTile;
		this.pieceID = pieceID;
		this.legal = legal;
		if (possibleMoves != null)
			this.possibleMoves = possibleMoves;
	}
}
