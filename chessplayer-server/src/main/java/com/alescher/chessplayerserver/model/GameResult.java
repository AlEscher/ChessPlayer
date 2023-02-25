package com.alescher.chessplayerserver.model;

import java.util.Optional;

public class GameResult
{
	public enum Reason
	{
		RESIGNATION, TIMEOUT, CHECKMATE, STALEMATE, REPETITION;
	}

	/**
	 * The winner of the match. Empty if draw
	 */
	private final Optional<Color> winner;
	/**
	 * The reason for the game ending
	 */
	private final Reason reason;

	public GameResult(Reason reason, Color winner)
	{
		this.reason = reason;
		this.winner = Optional.of(winner);
	}

	public GameResult(Reason reason)
	{
		this.reason = reason;
		this.winner = Optional.empty();
	}

	public Optional<Color> getWinner()
	{
		return winner;
	}

	public Reason getReason()
	{
		return reason;
	}
}
