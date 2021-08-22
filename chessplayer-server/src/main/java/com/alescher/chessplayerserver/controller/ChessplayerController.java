package com.alescher.chessplayerserver.controller;

import com.alescher.chessplayerserver.model.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
		ModelAndView indexView = new ModelAndView();
		indexView.setViewName("index.html");
		return indexView;
	}

	@PutMapping (path="/make-move", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity makeMove(@RequestBody MoveRequestEntity moveRequest)
	{
		logger.info("Received move request: " + moveRequest);
		boolean isLegal = board.isLegalMove(moveRequest.getFromTile(), moveRequest.getToTile());

		MoveResponseEntity moveResponse = new MoveResponseEntity(moveRequest.getFromTile(), moveRequest.getToTile(),
				moveRequest.getPieceID(), isLegal);
		logger.info("Sending move response: " + moveResponse);

		return moveResponse;
	}
}
