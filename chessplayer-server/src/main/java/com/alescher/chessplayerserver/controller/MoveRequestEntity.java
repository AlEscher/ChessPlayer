package com.alescher.chessplayerserver.controller;

/**
 * Represents a move to be made, e.g. to check whether it is a legal move
 *
 * @author AlEscher
 */
public class MoveRequestEntity
{
	private String fromTile;
	private String toTile;
	private String pieceID;

	@Override
	public String toString()
	{
		return String.format("{ fromTile: %s, toTile: %s, pieceID: %s }" ,fromTile, toTile, pieceID);
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

	public MoveRequestEntity(String fromTile, String toTile, String pieceID)
	{
		this.fromTile = fromTile;
		this.toTile = toTile;
		this.pieceID = pieceID;
	}
}
