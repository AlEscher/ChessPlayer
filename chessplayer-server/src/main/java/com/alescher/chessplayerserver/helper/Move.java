package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import org.jetbrains.annotations.Nullable;

import java.awt.Point;

/**
 * Represents a move on the chessboard
 *
 * @author AlEscher
 */
public class Move
{
	private Point from;
	private Point to;
	private ChessPiece capturedPiece;

	public Point getFrom()
	{
		return from;
	}

	public Point getTo()
	{
		return to;
	}

	public ChessPiece getCapturedPiece()
	{
		return capturedPiece;
	}

	public Move(Point from, Point to, @Nullable ChessPiece capturedPiece)
	{
		this.from = from;
		this.to = to;
		this.capturedPiece = capturedPiece;
	}
}
