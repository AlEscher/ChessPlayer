package com.alescher.chessplayerserver.controller;

import org.jetbrains.annotations.Nullable;

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
	private List<String> possibleMoves = new ArrayList<>();

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

	public List<String> getPossibleMoves()
	{
		return possibleMoves;
	}

	public MoveResponseEntity(String fromTile, String toTile, String pieceID, boolean legal, @Nullable List<String> possibleMoves)
	{
		this.fromTile = fromTile;
		this.toTile = toTile;
		this.pieceID = pieceID;
		this.legal = legal;
		if (possibleMoves != null)
			this.possibleMoves = possibleMoves;
	}

	/**
	 * Creates a response to the given request, including additional information that the server computed
	 * @param requestEntity The corresponding request
	 * @param legal Whether the requested move is legal
	 * @param possibleMoves A list of possible moves
	 * @return The response entity
	 */
	public static MoveResponseEntity create(MoveRequestEntity requestEntity, boolean legal, @Nullable List<String> possibleMoves)
	{
		return new MoveResponseEntity(requestEntity.getFromTile(), requestEntity.getToTile(),
				requestEntity.getPieceID(), legal, possibleMoves);
	}

	/**
	 * Creates a response to the given request, including additional information that the server computed
	 * @param fromTile The tile for which the request was made
	 * @param pieceID The id of the piece for which the request was made
	 * @param possibleMoves A list of possible moves that the piece can make
	 * @return The response entity
	 */
	public static MoveResponseEntity create(String fromTile, String pieceID, List<String> possibleMoves)
	{
		return new MoveResponseEntity(fromTile, null, pieceID, true, possibleMoves);
	}
}
