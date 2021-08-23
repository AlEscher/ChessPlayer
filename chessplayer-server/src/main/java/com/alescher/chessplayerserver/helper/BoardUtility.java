package com.alescher.chessplayerserver.helper;

import com.alescher.chessplayerserver.model.ChessPiece;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions for checking things on our chessboard
 * @author AlEscher
 */
public class BoardUtility
{
	/**
	 * Given a list of directional vectors, generates all legal moves a piece can make
	 * @param moveFrom The starting position
	 * @param directions The directional vectors (of length 1)
	 * @param piece The piece to be moved
	 * @param gameBoard The chessboard
	 * @return A list of all legal generated moves
	 */
	public static List<Point> generateLegalMoves(Point moveFrom, List<Point> directions, ChessPiece piece, ChessPiece[][] gameBoard)
	{
		List<Point> possibleMoves = new ArrayList<>();
		// For each directional vector, create new positions until we reach an obstacle
		for (Point direction : directions)
		{
			for (int i = 1; i < gameBoard.length; i++)
			{
				// Move a single step in the current direction
				Point p = new Point(moveFrom.x + i * direction.x, moveFrom.y + i * direction.y);
				// If we reach an obstacle (out of bounds or friendly piece) then there is no need to continue in this direction
				if (!BoardUtility.checkBounds(p) || (gameBoard[p.y][p.x] != null && gameBoard[p.y][p.x].getColor() == piece.getColor()))
				{
					break;
				}
				// If we reach an enemy piece we have reached the last viable position in this direction
				else if ((gameBoard[p.y][p.x] != null && gameBoard[p.y][p.x].getColor() != piece.getColor()))
				{
					possibleMoves.add(p);
					break;
				}
				else
				{
					possibleMoves.add(p);
				}
			}
		}

		return possibleMoves;
	}
	/**
	 * Removes all illegal moves from a given set of moves.
	 * possibleMoves will only contain legal moves after this method is called.
	 * @param possibleMoves The positions we can move to
	 * @param piece The piece we want to move
	 * @param moveFrom The positions we are moving from
	 * @param gameBoard The chessboard
	 */
	public static void removeIllegalMoves(List<Point> possibleMoves, ChessPiece piece, Point moveFrom, ChessPiece[][] gameBoard)
	{
		possibleMoves.removeIf(p -> (
				!BoardUtility.checkBounds(p)
				|| !piece.isLegalMove(moveFrom, p, gameBoard)
				|| BoardUtility.checkFriendlyFire(moveFrom, p, gameBoard)));
	}
	/**
	 * Checks every tile from (moveFrom, moveTo) and returns whether
	 * there is no piece (enemy or friendly) on any of the tiles. The first & last tile are not checked.
	 * This method assumes that moveFrom and moveTo are both within the bounds of the gameBoard.
	 * @param moveFrom The starting point (not inclusive)
	 * @param moveTo The end point (not inclusive)
	 * @param gameBoard The chessboard
	 * @return True if there is no piece on this path, false otherwise
	 */
	public static boolean checkPathUnobstructed(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		Point direction = new Point(moveTo.x - moveFrom.x, moveTo.y - moveFrom.y);
		// If we are walking diagonally, direction.x and direction.y will be the length.
		// If we are walking horizontally / vertically, then the value != 0 will be the length.
		double length = Math.max(Math.abs(direction.x), Math.abs(direction.y));
		direction.x /= length;
		direction.y /= length;

		for (int i = 1; i < length; i++)
		{
			if (gameBoard[moveFrom.y + direction.y * i][moveFrom.x + direction.x * i] != null)
				return false;
		}

		return true;
	}

	/**
	 * Checks if a piece is about to capture a friendly piece
	 * @param moveFrom The current position of the piece
	 * @param moveTo The position the piece should move to
	 * @param gameBoard The chessboard
	 * @return True if moveTo contains a piece of the same color, false otherwise
	 */
	public static boolean checkFriendlyFire(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard)
	{
		if (gameBoard[moveFrom.y][moveFrom.x] != null && gameBoard[moveTo.y][moveTo.x] != null)
			return gameBoard[moveFrom.y][moveFrom.x].getColor().equals(gameBoard[moveTo.y][moveTo.x].getColor());

		return false;
	}

	/**
	 * Checks whether a given point is within the bounds of our chessboard
	 * @param pt The point to check
	 * @return True if the point is within the bounds of a chessboard, false otherwise
	 */
	public static boolean checkBounds(Point pt)
	{
		return pt.x >= 0 && pt.y >= 0 && pt.x < 8 && pt.y < 8;
	}

	/**
	 * Checks if a given move is a diagonal movement
	 * @param moveFrom Start point
	 * @param moveTo End point
	 * @return True if the move is diagonal, false otherwise
	 */
	public static boolean isDiagonal(Point moveFrom, Point moveTo)
	{
		return Math.abs(moveFrom.x - moveTo.x) == Math.abs(moveFrom.y - moveTo.y);
	}

	/**
	 * Checks if a given move is a horizontal or vertical movement
	 * @param moveFrom Start point
	 * @param moveTo End point
	 * @return True if the move is horizontal or vertical, false otherwise
	 */
	public static boolean isHorizontalOrVertical(Point moveFrom, Point moveTo)
	{
		return moveFrom.x == moveTo.x || moveFrom.y == moveTo.y;
	}
}
