package com.alescher.chessplayerserver.model;

import java.awt.*;

interface ChessPieceFactory
{
	ChessPiece create(Color color, Point position, ChessGame game);
}
