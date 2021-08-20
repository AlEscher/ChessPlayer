package com.alescher.chessplayerserver.controller;

/**
 * Wrapper class that represents a chess move
 */
public class MoveResponseEntity
{
	private String fromTile;
	private String toTile;
	private String pieceID;
	private boolean legal;

	@Override
	public String toString()
	{
		return String.format("{ fromTile: %s, toTile: %s, pieceID: %s, legal: %b }" ,fromTile, toTile, pieceID, legal);
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

	public MoveResponseEntity(String fromTile, String toTile, String pieceID, boolean legal)
	{
		this.fromTile = fromTile;
		this.toTile = toTile;
		this.pieceID = pieceID;
		this.legal = legal;
	}
}
