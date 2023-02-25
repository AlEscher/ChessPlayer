package com.alescher.chessplayerserver.model.pieces;

import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Pawn figure in the game of chess
 *
 * @author AlEscher
 */
public class Pawn extends ChessPiece
{
	private boolean didMove = false;
	private final Direction direction;

	public Pawn(com.alescher.chessplayerserver.model.Color color, Point position, ChessGame game)
	{
		super(color, position, game);
		this.direction = (color == com.alescher.chessplayerserver.model.Color.WHITE) ? Direction.UP : Direction.DOWN;
	}

	/**
	 * In which direction are pawns allowed to move
	 */
	public enum Direction
	{
		UP, DOWN
	}

	@Override
	public boolean checkMove(Point moveTo)
	{
		updateDidMove();
		// Check that we are moving in the correct direction
		if (direction == Direction.DOWN && getPosition().y >= moveTo.y)
			return false;
		if (direction == Direction.UP && getPosition().y <= moveTo.y)
			return false;

		if (getPosition().x == moveTo.x) // Pawn is moving forward
		{
			return checkMoveForward(moveTo, getGameBoard());
		}
		else if (Math.abs(getPosition().x - moveTo.x) == 1) // Pawn is capturing
		{
			return checkCapture(moveTo, getGameBoard());
		}

		return false;
	}

	@Override
	public List<Point> calculatePossibleMoves()
	{
		int direction = (this.direction == Direction.DOWN) ? 1 : -1;
		Point position = getPosition();
		// All the moves a pawn can generally make
		List<Point> possibleMoves = new ArrayList<>();
		possibleMoves.add(new Point(position.x, position.y + direction));
		possibleMoves.add(new Point(position.x, position.y + 2 * direction));
		possibleMoves.add(new Point(position.x + 1, position.y + direction));
		possibleMoves.add(new Point(position.x - 1, position.y + direction));

		removeImpossibleMoves(possibleMoves);
 		return possibleMoves;
	}

	// Checks whether the pawn can move forward, considering the direction he is facing
	private boolean checkMoveForward(Point to, ChessPiece[][] gameBoard)
	{
		final int distance = Math.abs(getPosition().y - to.y);

		if (distance > 2) // Pawns can move at most 2 tiles forward
			return false;
		if (distance == 2 && didMove) // Pawns can only move 2 tiles if it is their first move
			return false;
		if (gameBoard[to.y][to.x] != null) // Cannot capture when moving forward
			return false;

		return true; // Allowed move forward
	}

	// Checks whether the pawn can capture a piece, considering the direction he is facing
	private boolean checkCapture(Point to, ChessPiece[][] gameBoard)
	{
		if (Math.abs(getPosition().y - to.y) > 1) // Must move vertically when capturing
			return false;
		if (gameBoard[to.y][to.x] == null) // We cannot capture on an empty tile
			return false;

		return true; // Allowed capture
	}

	// Check if this pawn has moved before
	private void updateDidMove()
	{
		// The row that this pawn was at the beginning of the game
		int startingRow = (this.direction == Direction.DOWN) ? 1 : 6;
		this.didMove = getPosition().y != startingRow;
	}

	@Override
	public int getValue()
	{
		return 1;
	}

	@Override
	public String toString()
	{
		return getColor() == Color.WHITE ? "P" : "p";
	}
}
