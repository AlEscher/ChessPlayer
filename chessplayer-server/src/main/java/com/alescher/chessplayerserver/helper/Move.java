package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;
import com.alescher.chessplayerserver.model.Color;
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
	private Color moveColor;

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

	public Color getMoveColor()
	{
		return moveColor;
	}

	public Move(Point from, Point to, @Nullable ChessPiece capturedPiece, Color moveColor)
	{
		this.from = from;
		this.to = to;
		this.capturedPiece = capturedPiece;
		this.moveColor = moveColor;
	}
}
