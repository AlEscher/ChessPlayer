package com.alescher.chessplayerserver.controller;

import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
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
	private Color checkMated;
	private List<String> possibleMoves = new ArrayList<>();

	@Override
	public String toString()
	{
		return String.format("{ fromTile: %s, toTile: %s, pieceID: %s, legal: %b, possibleMoves: %s, checkMated: %s }",
				fromTile, toTile, pieceID, legal, possibleMoves.toString(), checkMated);
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

	public Color getCheckMated()
	{
		return checkMated;
	}

	public MoveResponseEntity(String fromTile, String toTile, String pieceID, boolean legal, @Nullable List<String> possibleMoves, Color checkMated)
	{
		this.fromTile = fromTile;
		this.toTile = toTile;
		this.pieceID = pieceID;
		this.legal = legal;
		this.checkMated = checkMated;
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
	public static MoveResponseEntity create(MoveRequestEntity requestEntity, boolean legal, @Nullable List<String> possibleMoves, ChessGame board)
	{
		return new MoveResponseEntity(requestEntity.getFromTile(), requestEntity.getToTile(),
				requestEntity.getPieceID(), legal, possibleMoves, board.getCheckMated());
	}

	/**
	 * Creates a response to the given request, including additional information that the server computed
	 * @param fromTile The tile for which the request was made
	 * @param pieceID The id of the piece for which the request was made
	 * @param possibleMoves A list of possible moves that the piece can make
	 * @return The response entity
	 */
	public static MoveResponseEntity create(String fromTile, String pieceID, List<String> possibleMoves, ChessGame board)
	{
		return new MoveResponseEntity(fromTile, null, pieceID, true, possibleMoves, board.getCheckMated());
	}
}
