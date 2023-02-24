package com.alescher.chessplayerserver;

import com.alescher.chessplayerserver.model.ChessGame;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FenTest
{
	private ChessGame game;

	@Test
	public void testToFenPosition()
	{
		game = new ChessGame();
		game.performMove("E2", "E4");
		game.performMove("E7", "E5");
		game.performMove("B1", "C3");
		game.performMove("F7", "F5");
		game.performMove("F1", "D3");
		game.performMove("G8", "F6");
		game.performMove("D1", "F3");
		game.performMove("B8", "A6");
		String actualFen = game.toFEN();
		String expectedFen = "r1bqkb1r/pppp2pp/n4n2/4pp2/4P3/2NB1Q2/PPPP1PPP/R1B1K1NR w KQkq - - 5";
		assertThat(actualFen).isEqualTo(expectedFen);
	}

	@Test
	public void testFromFenPosition()
	{
		game = ChessGame.fromFEN("2kr1b1r/p1p1ppp1/1pnq3p/8/3PP3/2P3PN/PP1BQ1P1/RN2K2R b KQ - 0 13");
		String expectedString = """

				_________________________________
				|   |   | k | r |   | b |   | r |
				| p |   | p |   | p | p | p |   |
				|   | p | n | q |   |   |   | p |
				|   |   |   |   |   |   |   |   |
				|   |   |   | P | P |   |   |   |
				|   |   | P |   |   |   | P | N |
				| P | P |   | B | Q |   | P |   |
				| R | N |   |   | K |   |   | R |
				---------------------------------""";
		String actualString = game.toString();
		assertThat(actualString).isEqualTo(expectedString);
	}
}
