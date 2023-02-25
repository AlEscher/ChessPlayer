package com.alescher.chessplayerserver;

import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckMateTest
{
	private ChessGame game;

	@Test
	public void testFoolsMate()
	{
		game = new ChessGame();
		assertThat(game.performMove("F2", "F3").legal()).isTrue();
		assertThat(game.performMove("E7", "E5").legal()).isTrue();
		assertThat(game.performMove("G2", "G4").legal()).isTrue();
		assertThat(game.performMove("D8", "H4").legal()).isTrue();
		assertThat(game.isGameOver()).isTrue();
		assertThat(game.getCheckMated()).isEqualTo(Color.WHITE);
	}
}
