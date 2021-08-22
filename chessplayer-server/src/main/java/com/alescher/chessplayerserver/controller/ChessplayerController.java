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
import java.util.stream.Collectors;

@RestController
public class ChessplayerController
{
	private ChessBoard board;

	public static Logger logger = LoggerFactory.getLogger(ChessplayerController.class);

	public ChessplayerController()
	{
		this.board = new ChessBoard();
	}

	@GetMapping(path="/")
	public ModelAndView index()
	{
		logger.info("Setting up new ChessBoard");
		this.board = new ChessBoard();
		return new ModelAndView("index.html");
	}

	@PutMapping (path="/make-move", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity makeMove(@RequestBody MoveRequestEntity moveRequest)
	{
		logger.info("Received request to make move: " + moveRequest);

		boolean isLegal = board.isLegalMove(moveRequest.getFromTile(), moveRequest.getToTile());
		MoveResponseEntity moveResponse = new MoveResponseEntity(moveRequest.getFromTile(), moveRequest.getToTile(),
				moveRequest.getPieceID(), isLegal, null);
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
				.map(p -> ChessPositionConverter.convertPointToTile(p))
				.collect(Collectors.toList());

		MoveResponseEntity moveResponse = new MoveResponseEntity(fromTile, null, pieceID, true, possibleMoves);
		logger.info("Sending response: " + moveResponse);

		return moveResponse;
	}
}
