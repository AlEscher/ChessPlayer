package com.alescher.chessplayerserver;

import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;

public class ChessPositionConverterTest
{
	@Test
	public void testTileToPoint()
	{
		Point expectedPoint = new Point(2, 3);
		Point actualPoint = ChessPositionConverter.tileToPoint("C5");
		assertThat(actualPoint).isEqualTo(expectedPoint);
	}

	@Test
	public void testPointToTile()
	{
		String expectedTile = "F7";
		String actualTile = ChessPositionConverter.pointToTile(new Point(5, 1));
		assertThat(actualTile).isEqualTo(expectedTile);
	}
}
