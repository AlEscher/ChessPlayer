package com.alescher.chessplayerserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessplayerController
{
	private Logger logger = LoggerFactory.getLogger(ChessplayerController.class);

	@PutMapping (path="/make-move", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public MoveResponseEntity makeMove(@RequestBody MoveRequestEntity moveRequest)
	{
		final String fromTile = moveRequest.getFromTile();
		final String toTile = moveRequest.getToTile();
		final String pieceID = moveRequest.getPieceID();
		logger.info("Received move request: " + moveRequest);

		final MoveResponseEntity moveResponse = new MoveResponseEntity(fromTile, toTile, pieceID, true);
		logger.info("Sending move response: " + moveResponse);

		return moveResponse;
	}
}
