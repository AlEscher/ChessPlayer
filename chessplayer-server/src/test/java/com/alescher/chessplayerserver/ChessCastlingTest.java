package com.alescher.chessplayerserver;

import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import com.alescher.chessplayerserver.model.*;
import com.alescher.chessplayerserver.model.pieces.ChessPiece;
import com.alescher.chessplayerserver.model.pieces.King;
import com.alescher.chessplayerserver.model.pieces.Rook;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChessCastlingTest
{
	private ChessGame game;

	@Nested
	class ValidCastles
	{
		@Test
		public void testWhiteKingSideCastle()
		{
			game = ChessGame.fromFEN("r1bqk1nr/pppp1ppp/2n5/2b1p3/4P3/3B1N2/PPPP1PPP/RNBQK2R w KQkq - 4 4");
			MoveResult result = game.performMove("E1", "G1");
			// Test that castling is allowed in this instance
			assertThat(result.legal()).isTrue();
			assertThat(result.extraMoves()).isPresent();
			// Test that the board is in the correct state after castling
			ChessPiece king = game.getPiece("G1");
			assertThat(king.getClass()).isEqualTo(King.class);
			assertThat(((King)king).getPossibleCastles()).isEmpty();
			assertThat(game.getPiece("F1").getClass()).isEqualTo(Rook.class);
		}

		@Test
		public void testBlackKingSideCastle()
		{
			game = ChessGame.fromFEN("r1bqk2r/pppp1ppp/2n2n2/2b1p3/4P3/3B1N1P/PPPP1PP1/RNBQ1RK1 b kq - 0 5");
			MoveResult result = game.performMove("E8", "G8");
			assertThat(result.legal()).isTrue();
			assertThat(result.extraMoves()).isPresent();
			ChessPiece king = game.getPiece("G8");
			assertThat(king.getClass()).isEqualTo(King.class);
			assertThat(((King)king).getPossibleCastles()).isEmpty();
			assertThat(game.getPiece("F8").getClass()).isEqualTo(Rook.class);
		}

		@Test
		public void testWhiteQueenSideCastle()
		{
			game = ChessGame.fromFEN("r2qkbnr/pppb1ppp/2n1p3/3p4/3P1B2/2NQ4/PPP1PPPP/R3KBNR w KQkq - 4 5");
			MoveResult result = game.performMove("E1", "C1");
			assertThat(result.legal()).isTrue();
			assertThat(result.extraMoves()).isPresent();
			ChessPiece king = game.getPiece("C1");
			assertThat(king.getClass()).isEqualTo(King.class);
			assertThat(((King)king).getPossibleCastles()).isEmpty();
			assertThat(game.getPiece("D1").getClass()).isEqualTo(Rook.class);
		}

		@Test
		public void testBlackQueenSideCastle()
		{
			game = ChessGame.fromFEN("r3kbnr/pppb1ppp/2n1pq2/3p4/3P1B2/P1NQ4/1PP1PPPP/2KR1BNR b kq - 0 6");
			MoveResult result = game.performMove("E8", "C8");
			assertThat(result.legal()).isTrue();
			assertThat(result.extraMoves()).isPresent();
			ChessPiece king = game.getPiece("C8");
			assertThat(king.getClass()).isEqualTo(King.class);
			assertThat(((King)king).getPossibleCastles()).isEmpty();
			assertThat(game.getPiece("D8").getClass()).isEqualTo(Rook.class);
		}
	}

	@Nested
	class InvalidCastles
	{
		@Test
		public void testCastleThroughCheck()
		{
			game = ChessGame.fromFEN("rn1qkbnr/ppp2ppp/3p4/4p3/2b1P3/5NPB/PPPP1P1P/RNBQK2R w KQkq - 4 5");
			MoveResult result = game.performMove("E1", "G1");
			assertThat(result.legal()).isFalse();
			assertThat(result.extraMoves()).isEmpty();
			List<String> possibleMoves = game.getLegalMoveTiles(ChessPositionConverter.tileToPoint("E1"));
			assertThat(possibleMoves).isEmpty();
		}

		@Test
		public void testCastleWhileChecked()
		{
			game = ChessGame.fromFEN("r1bqk2r/1ppp1ppp/2n2n2/p3p3/1b2P3/3P1NPB/PPP2P1P/RNBQK2R w KQkq - 2 6");
			MoveResult result = game.performMove("E1", "G1");
			assertThat(result.legal()).isFalse();
			assertThat(result.extraMoves()).isEmpty();
			List<String> possibleMoves = game.getLegalMoveTiles(ChessPositionConverter.tileToPoint("E1"));
			assertThat(possibleMoves).hasSize(2);
		}

		@Test
		public void testCastleAfterKingMove()
		{
			game = ChessGame.fromFEN("r3k2r/pppq1ppp/n2p1n2/2b1p3/4P1b1/BPNB1N2/P1PPQPPP/R3K2R w KQkq - 4 8");
			assertThat(game.getWhiteKing().getPossibleCastles()).contains(Castle.KINGSIDE, Castle.QUEENSIDE);
			MoveResult result = game.performMove("E1", "D1");
			assertThat(game.getWhiteKing().getPossibleCastles()).isEmpty();
			assertThat(game.getBlackKing().getPossibleCastles()).contains(Castle.KINGSIDE, Castle.QUEENSIDE);
		}

		@Test
		public void testCastleAfterRookMove()
		{
			game = ChessGame.fromFEN("1r2k2r/ppp1ppb1/2nqbnpp/3p4/1P1P1BP1/P1NQ1N1P/2P1PP2/2KR1B1R b k - 0 10");
			assertThat(game.getWhiteKing().getPossibleCastles()).isEmpty();
			assertThat(game.getBlackKing().getPossibleCastles()).containsExactly(Castle.KINGSIDE);
			assertThat(game.isLegalMove("E8", "G8")).isTrue();
			game.performMove("H8", "H7");
			assertThat(game.getBlackKing().getPossibleCastles()).isEmpty();
			assertThat(game.isLegalMove("E8", "G8")).isFalse();
		}
	}
}
