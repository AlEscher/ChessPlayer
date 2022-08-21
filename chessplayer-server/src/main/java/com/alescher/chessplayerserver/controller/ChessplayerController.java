package com.alescher.chessplayerserver.controller;

import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import com.alescher.chessplayerserver.model.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.Point;
import java.util.List;

@RestController
public class ChessplayerController
{
	private ChessBoard board;

	public static final Logger logger = LoggerFactory.getLogger(ChessplayerController.class);

	public ChessplayerController()
	{
		this.board = new ChessBoard();
	}

	@GetMapping(path="/")
	public ModelAndView index()
	{
		// TODO Send setup of gameBoard to webapp
		logger.info("Setting up new ChessBoard");
		this.board = new ChessBoard();
		return new ModelAndView("index.html");
	}

	@PutMapping (path="/make-move", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity makeMove(@RequestBody MoveRequestEntity moveRequest)
	{
		logger.info("Received request to make move: " + moveRequest);

		boolean isLegal = board.isLegalMove(moveRequest.getFromTile(), moveRequest.getToTile());
		if (isLegal)
		{
			board.performMove(moveRequest.getFromTile(), moveRequest.getToTile());
		}
		MoveResponseEntity moveResponse = MoveResponseEntity.create(moveRequest, isLegal, null, board);
		logger.info("Sending move response: " + moveResponse);

		return moveResponse;
	}

	@GetMapping(path="/get-moves", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity getAllMoves(@RequestParam String fromTile, @RequestParam(required = false) String pieceID)
	{
		logger.info(String.format("Received request to generate moves for %s on %s", pieceID, fromTile));

		Point moveFrom = ChessPositionConverter.convertTileToPoint(fromTile);
		// Get all legal moves and convert them to chess coordinates
		List<String> possibleMoves = board.getLegalMoves(moveFrom)
				.stream()
				.map(ChessPositionConverter::convertPointToTile)
				.toList();

		MoveResponseEntity moveResponse = MoveResponseEntity.create(fromTile, pieceID, possibleMoves, board);
		logger.info("Sending response: " + moveResponse);

		return moveResponse;
	}
}
