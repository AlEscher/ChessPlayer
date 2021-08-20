package com.alescher.chessplayerserver.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a single tile on the Chessboard.
 * It holds a single {@Link ChessPiece}, which will be null if the tile is empty
 *
 * @author AlEscher
 */
public class Tile
{
	private ChessPiece piece = null;

	public void moveTo(@NotNull Tile target)
	{
		target.setPiece(this.piece);
		this.piece = null;
	}

	public ChessPiece getPiece()
	{
		return piece;
	}

	public void setPiece(@Nullable ChessPiece piece)
	{
		this.piece = piece;
	}
}
