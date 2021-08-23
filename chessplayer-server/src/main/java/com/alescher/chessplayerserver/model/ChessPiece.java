package com.alescher.chessplayerserver.model;

import java.awt.Point;
import java.util.List;

/**
 * An interface that every chess piece implements.
 * It offers functionality such as {@link ChessPiece#isLegalMove}
 * and {@link ChessPiece#getLegalMoves}
 *
 * @author AlEscher
 */
public interface ChessPiece
{
	/**
	 * Checks whether the move to be performed is allowed for this particular chess piece
	 * @param moveFrom The current position of the piece
	 * @param moveTo The position we want to move to
	 * @param gameBoard The gameboard representing the current state of the chessboard
	 * @return True if the move is allowed, False otherwise
	 */
	boolean isLegalMove(Point moveFrom, Point moveTo, ChessPiece[][] gameBoard);

	/**
	 * Calculates all moves that this piece is allowed to perform at the moment
	 * @param moveFrom The current position of the piece
	 * @param gameBoard The gameboard representing the current state of the chessboard
	 * @return A list of all tiles the piece can move to
	 */
	List<Point> getLegalMoves(Point moveFrom, ChessPiece[][] gameBoard);

	Color getColor();
	int getValue();
}
