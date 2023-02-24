package com.alescher.chessplayerserver.model;

import java.util.Map;
import java.util.Optional;

public record MoveResult(
		boolean legal, Optional<Map<String, String>> extraMoves
)
{}
