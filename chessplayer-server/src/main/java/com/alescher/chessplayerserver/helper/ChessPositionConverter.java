package com.alescher.chessplayerserver.helper;

import org.jetbrains.annotations.NotNull;

import java.awt.Point;

/**
 * Converts chess tiles (e.g. "A1, B2, etc..." into coordinates (x, y)
 *
 * @author AlEscher
 */
public class ChessPositionConverter
{
	/**
	 * Converts a string denoting a chess tile into a 2D coordinate.
	 * The origin is in the top left of the board. X represents the column and Y the row,
	 * so e.g. "A1" becomes (0, 7) and "A8" becomes (0, 0)
	 * @param chessTile The tile in chess notation, e.g. "A1"
	 * @return The corresponding 2D coordinate
	 * @throws IllegalArgumentException
	 */
	public static Point convertTileToPoint(@NotNull String chessTile) throws IllegalArgumentException
	{
		if (chessTile.length() != 2)
			throw new IllegalArgumentException("A chess tile should only have 2 characters, e.g. \"A1\"");
		if (!Character.isLetter(chessTile.charAt(0)) || !Character.isDigit(chessTile.charAt(1)))
			throw new IllegalArgumentException(String.format("Malformed chess tile: %s", chessTile));

		int x = convertColumn(chessTile.charAt(0));
		int column = Character.getNumericValue(chessTile.charAt(1));
		// Mirror the column index (Our board's top left corner is (0, 0) but on a chessboard it is A8)
		// 8 => 0; 7 => 1; ...; 4 => 4; 3 => 5; etc...
		int y = column - (column - 4) * 2;

		return new Point(x, y);
	}

	/**
	 * Transforms a 2D point into chess coordinates
	 * @param point The 2D point
	 * @return The corresponding chess coordinate, e.g. "A1"
	 */
	public static String convertPointToTile(@NotNull Point point)
	{
		StringBuilder tile = new StringBuilder();
		tile.append((char)(point.x + 65));  // A => 65, H => 72
		// Mirror the column index (Our board's top left corner is (0, 0) but on a chessboard it is A8)
		tile.append(Character.forDigit((point.y - (point.y - 4) * 2), 10));
		return tile.toString();
	}

	private static int convertColumn(char col) throws IllegalArgumentException
	{
		int value = (int)Character.toUpperCase(col) - 65; // A => 65, H => 72
		if (value < 0 || value > 7)
			throw new IllegalArgumentException("Column identifier should be between 'A' and 'H'");

		return value;
	}
}
