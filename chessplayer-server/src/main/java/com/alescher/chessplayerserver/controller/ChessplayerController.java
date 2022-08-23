package com.alescher.chessplayerserver.controller;

import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import com.alescher.chessplayerserver.model.ChessGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.util.*;
import java.util.List;

@RestController
public class ChessplayerController
{
	private final Map<String, ChessGame> games;

	public static final Logger logger = LoggerFactory.getLogger(ChessplayerController.class);

	public ChessplayerController()
	{
		this.games = new HashMap<>();
	}

	/**
	 * The first landing page when a user visits the app. Creates a new game ID and redirects the user to it.
	 */
	@GetMapping(path="/")
	public ModelAndView index()
	{
		String gameID = UUID.randomUUID().toString();
		logger.info("New game request, redirecting to {}", gameID);
		return new ModelAndView(String.format("redirect:/game/%s/", gameID));
	}

	/**
	 * The page to get a specific game specified by its ID.
	 * @param id The id of the game
	 */
	@GetMapping(path="/game/{id}/")
	public ModelAndView gameIndex(@PathVariable String id)
	{
		if (!this.games.containsKey(id))
		{
			logger.info("Setting up new ChessBoard for Game-ID: {}", id);
			this.games.put(id, new ChessGame());
		}

		ModelAndView view = new ModelAndView("index.html");
		view.addObject("gameFen", this.games.get(id).toFEN());
		return view;
	}

	@PutMapping (path="/game/{id}/make-move", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity makeMove(@RequestBody MoveRequestEntity moveRequest, @PathVariable String id)
	{
		logger.info("Received request to make move: {}", moveRequest);
		ChessGame board = this.games.get(id);

		boolean isLegal = board.isLegalMove(moveRequest.getFromTile(), moveRequest.getToTile());
		Optional<Map<String, String>> extraMoves = Optional.empty();
		if (isLegal)
		{
			extraMoves = board.performMove(moveRequest.getFromTile(), moveRequest.getToTile());
		}
		MoveResponseEntity moveResponse = MoveResponseEntity.create(moveRequest, isLegal, null, board, extraMoves);
		logger.info("Sending move response: {}", moveResponse);

		return moveResponse;
	}

	@GetMapping(path="/game/{id}/get-moves", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity getAllMoves(@RequestParam String fromTile, @RequestParam(required = false) String pieceID, @PathVariable String id)
	{
		logger.info("Received request to generate moves for {} on {}", pieceID, fromTile);
		ChessGame board = this.games.get(id);

		Point moveFrom = ChessPositionConverter.tileToPoint(fromTile);
		// Get all legal moves and convert them to chess coordinates
		List<String> possibleMoves = board.getLegalMoves(moveFrom)
				.stream()
				.map(ChessPositionConverter::pointToTile)
				.toList();

		MoveResponseEntity moveResponse = MoveResponseEntity.create(fromTile, pieceID, possibleMoves, board);
		logger.info("Sending response: {}", moveResponse);

		return moveResponse;
	}

	@GetMapping(path="/game/{id}/fen")
	public String getFenCode(@PathVariable String id)
	{
		logger.info("Received request to generate FEN for {}", id);
		String fenString = this.games.get(id).toFEN();
		logger.info("Sending response: {}", fenString);
		return fenString;
	}
}
