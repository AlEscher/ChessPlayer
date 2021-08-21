package com.alescher.chessplayerserver.controller;

import com.alescher.chessplayerserver.model.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessplayerController
{
	private ChessBoard board = new ChessBoard();

	public static Logger logger = LoggerFactory.getLogger(ChessplayerController.class);

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
