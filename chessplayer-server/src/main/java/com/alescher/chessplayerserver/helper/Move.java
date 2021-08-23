package com.alescher.chessplayerserver.helper;

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

	public Point getFrom()
	{
		return from;
	}

	public Point getTo()
	{
		return to;
	}

	public Move(Point from, Point to)
	{
		this.from = from;
		this.to = to;
	}
}
