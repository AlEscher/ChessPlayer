package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Pawn figure in the game of chess
 *
 * @author AlEscher
 */
public class Pawn implements ChessPiece
{
	private boolean didMove = false;
	private Color color;
	private Direction direction;

	public Pawn(Color color, Direction direction)
	{
		this.color = color;
		this.direction = direction;
	}

	/**
	 * In which direction are pawns allowed to move
	 */
	public enum Direction
	{
		UP, DOWN
	}

	@Override
	public boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		// Check that we are moving in the correct direction
		if (direction == Direction.DOWN && moveFrom.y >= moveTo.y)
			return false;
		if (direction == Direction.UP && moveFrom.y <= moveTo.y)
			return false;

		if (moveFrom.x == moveTo.x) // Pawn is moving forward
		{
			return checkMoveForward(moveFrom, moveTo, gameBoard);
		}
		else if (Math.abs(moveFrom.x - moveTo.x) == 1) // Pawn is capturing
		{
			return checkCapture(moveFrom, moveTo, gameBoard);
		}

		return false;
	}

	@Override
	public List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard)
	{
		int direction = (this.direction == Direction.DOWN) ? 1 : -1;
		// All the moves a pawn can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(moveFrom.x, moveFrom.y + direction));
		possibleMoves.add(new Point(moveFrom.x, moveFrom.y + 2 * direction));
		possibleMoves.add(new Point(moveFrom.x + 1, moveFrom.y + direction));
		possibleMoves.add(new Point(moveFrom.x - 1, moveFrom.y + direction));

		possibleMoves.removeIf(p -> !BoardUtility.checkBounds(p) || !isLegalMove(p, moveFrom, gameBoard));
		return possibleMoves;
	}

	// Checks whether the pawn can move forward, considering the direction he is facing
	private boolean checkMoveForward(Point from, Point to, ChessPiece[][] gameBoard)
	{
		final int distance = Math.abs(from.y - to.y);

		if (distance > 2) // Pawns can move at most 2 tiles forward
			return false;
		if (distance == 2 && didMove) // Pawns can only move 2 tiles if it is their first move
			return false;
		if (gameBoard[to.y][to.x] != null) // Cannot capture when moving forward
			return false;

		didMove = true;
		return true; // Legal move forward
	}

	// Checks whether the pawn can capture a piece, considering the direction he is facing
	private boolean checkCapture(Point from, Point to, ChessPiece[][] gameBoard)
	{
		if (Math.abs(from.y - to.y) > 1) // Must move vertically when capturing
			return false;
		if (gameBoard[to.y][to.x] == null) // We cannot capture on an empty tile
			return false;

		didMove = true;
		return true; // Legal capture
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	@Override
	public String toString()
	{
		return "P";
	}
}
