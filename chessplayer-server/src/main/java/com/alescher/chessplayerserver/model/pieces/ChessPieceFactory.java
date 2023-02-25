package com.alescher.chessplayerserver.model.pieces;

import com.alescher.chessplayerserver.model.ChessGame;
import com.alescher.chessplayerserver.model.Color;

import java.awt.*;

public interface ChessPieceFactory
{
	ChessPiece create(Color color, Point position, ChessGame game);
}
