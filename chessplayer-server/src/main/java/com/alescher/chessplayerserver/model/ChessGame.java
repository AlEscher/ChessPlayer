package com.alescher.chessplayerserver.model;

import com.alescher.chessplayerserver.helper.BoardUtility;
import com.alescher.chessplayerserver.helper.CheckUtility;
import com.alescher.chessplayerserver.helper.ChessPositionConverter;
import com.alescher.chessplayerserver.helper.Move;
import com.alescher.chessplayerserver.model.pieces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.awt.Point;
import java.util.*;

/**
 * Represents the chessboard and its current state
 *
 * @author AlEscher
 */
public class ChessGame
{
	// TODO:
	//  - Castling:
	//      - Move rook (Frontend)
	//  - FEN support
	//      - fromFEN (Frontend & Backend)
	//  - Tests
	//  - Promoting
	//  - Stalemate
	//  - En passant
	private final ChessPiece[][] gameBoard;
	private final Stack<Move> pastMoves;
	private final CheckUtility checkUtility;
	private Color currentTurn;
	private Color checkMated = null;
	private boolean gameOver = false;
	private King whiteKing;
	private King blackKing;
	private static final Logger logger = LoggerFactory.getLogger(ChessGame.class);

	public ChessGame()
	{
		this.gameBoard = new ChessPiece[8][8];
		setupBoard();
		this.pastMoves = new Stack<>();
		this.checkUtility = new CheckUtility(this, gameBoard);
		this.currentTurn = Color.WHITE;
	}

	/**
	 * Generate a FEN string that represents the current state of the game.
	 * @return The FEN string
	 * @see {@link <a href="https://www.chess.com/terms/fen-chess">FEN</a>}
	 */
	public String toFEN()
	{
		StringBuilder fen = new StringBuilder();
		for (int i = 0; i < 8; i++)
		{
			int emptyTiles = 0;
			for (int j = 0; j < 8; j++)
			{
				ChessPiece piece = getPiece(j, i);
				if (piece == null)
				{
					emptyTiles++;
				} else
				{
					if (emptyTiles != 0)
					{
						fen.append(emptyTiles);
					}
					fen.append(piece);
					emptyTiles = 0;
				}
			}
			if (emptyTiles != 0)
			{
				fen.append(emptyTiles);
			}
			if (i < 7)
			{
				fen.append('/');
			}
		}
		fen.append(' ');
		fen.append(currentTurn == Color.WHITE ? 'w' : 'b');
		fen.append(' ');
		if (whiteKing.getPossibleCastles().contains(Castle.KINGSIDE))
			fen.append('K');
		if (whiteKing.getPossibleCastles().contains(Castle.QUEENSIDE))
			fen.append('Q');
		if (blackKing.getPossibleCastles().contains(Castle.KINGSIDE))
			fen.append('k');
		if (blackKing.getPossibleCastles().contains(Castle.QUEENSIDE))
			fen.append('q');
		fen.append(' ');
		// En passant not implemented yet
		fen.append('-');
		fen.append(' ');
		// Halfmove clock not implemented yet
		fen.append('-');
		fen.append(' ');
		// Fullmoves
		fen.append(1 + pastMoves.stream().filter(move -> move.getMoveColor() == Color.BLACK).count());

		return fen.toString();
	}

	/**
	 * Create a ChessGame instance from a FEN string
	 * @param fen The FEN string
	 * @return A ChessGame instance derived from the FEN
	 * @throws IllegalArgumentException in case of malformed FEN string
	 * @see {@link <a href="https://www.chess.com/terms/fen-chess">FEN</a>}
	 */
	public static ChessGame fromFEN(@NotNull String fen) throws IllegalArgumentException
	{
		ChessGame game = new ChessGame();
		// rank -> y; file -> x
		int rank = 0, file = 0;
		String[] sections = fen.split(" ");
		Assert.state(sections.length == 6, "Malformed FEN string");
		// Parse positions
		for (char c : sections[0].toCharArray())
		{
			if (file > 7)
			{
				file = 0;
			}
			if (Character.isDigit(c))
			{
				int count = Character.getNumericValue(c);
				for (int i = 0; i < count; i++) game.gameBoard[rank][file++] = null;
			} else if (c == '/')
			{
				file = 0;
				rank++;
			} else
			{
				Color color = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
				Point position = new Point(file, rank);
				ChessPieceFactory pieceFactory = switch(Character.toUpperCase(c))
				{
					case 'B' -> Bishop::new;
					case 'K' -> King::new;
					case 'N' -> Knight::new;
					case 'P' -> Pawn::new;
					case 'Q' -> Queen::new;
					case 'R' -> Rook::new;
					default -> throw new IllegalArgumentException("Unknown piece specifier " + c);
				};
				ChessPiece piece = pieceFactory.create(color, position, game);
				game.gameBoard[rank][file] = piece;
				game.whiteKing = (piece instanceof King && piece.getColor() == Color.WHITE) ? (King) piece : game.whiteKing;
				game.whiteKing = (piece instanceof King && piece.getColor() == Color.BLACK) ? (King) piece : game.whiteKing;
				file++;
			}
		}
		// Parse current turn
		game.currentTurn = sections[1].equals("w") ? Color.WHITE : Color.BLACK;
		// Parse castling rights
		if (!sections[2].contains("K")) game.whiteKing.removeCastleOption(Castle.KINGSIDE);
		if (!sections[2].contains("Q")) game.whiteKing.removeCastleOption(Castle.QUEENSIDE);
		if (!sections[2].contains("k")) game.blackKing.removeCastleOption(Castle.KINGSIDE);
		if (!sections[2].contains("q")) game.blackKing.removeCastleOption(Castle.QUEENSIDE);

		return game;
	}

	/**
	 * Get all legal moves for a specified chess piece
	 *
	 * @param moveFrom The position of the chess piece
	 * @return A list of points where the piece can move to
	 */
	public List<Point> getLegalMoves(@NotNull Point moveFrom)
	{
		return getLegalMoves(gameBoard[moveFrom.y][moveFrom.x]);
	}

	/**
	 * Get all legal moves for a specified chess piece
	 *
	 * @param piece The chess piece
	 * @return A list of points where the piece can move to
	 */
	public List<Point> getLegalMoves(@Nullable ChessPiece piece)
	{
		if (piece == null) return new ArrayList<>();

		return piece.getPossibleMoves()
				.stream()
				.filter(move -> isLegalMove(piece.getPosition(), move, false))
				.toList();
	}

	public List<String> getLegalMoveTiles(@NotNull Point moveFrom)
	{
		return getLegalMoves(moveFrom).stream().map(ChessPositionConverter::pointToTile).toList();
	}

	/**
	 * Performs a move on the Chessboard. Assumes that the moves was previously checked to be legal.
	 * After the move is performed, it is checked whether the move resulted in a checkmate.
	 * @param fromTile The starting tile
	 * @param toTile The destination tile
	 * @return An optional map of additional moves that need to be performed (e.g. for a castle)
	 */
	public MoveResult performMove(@NotNull String fromTile, @NotNull String toTile)
	{
		Point from = ChessPositionConverter.tileToPoint(fromTile);
		Point to = ChessPositionConverter.tileToPoint(toTile);
		boolean isLegal = isLegalMove(fromTile, toTile);
		Optional<Map<String, String>> extraMove = Optional.empty();
		if (isLegal)
		{
			extraMove = handleCastle(from, to);
			simulateMove(from, to);
			swapTurn();
			checkUtility.detectCheckMate().ifPresent(this::handleCheckMate);
		}
		return new MoveResult(isLegal, extraMove);
	}

	/**
	 * Checks whether a move can be performed on the chessboard.
	 *
	 * @param fromTile The tile a piece is being moved from, e.g. "A1", "B3", etc...
	 * @param toTile   The tile a piece is being moved to, e.g. "A1", "B3", etc...
	 * @return True if the move is allowed to be performed, false otherwise
	 */
	public boolean isLegalMove(@NotNull String fromTile, @NotNull String toTile)
	{
		if (gameOver) return false;

		Point fromPoint = ChessPositionConverter.tileToPoint(fromTile);
		Point toPoint = ChessPositionConverter.tileToPoint(toTile);

		logger.info(String.format("Checking move from %s to %s", fromPoint, toPoint));
		// Check bounds
		if (!BoardUtility.checkBounds(fromPoint) || !BoardUtility.checkBounds(toPoint))
			return false;

		boolean isLegal = isLegalMove(fromPoint, toPoint);

		logger.info(String.format("Move checked. Legal: %b", isLegal));
		return isLegal;
	}

	private boolean isLegalMove(Point moveFrom, Point moveTo)
	{
		return isLegalMove(moveFrom, moveTo, true);
	}

	/**
	 * Checks whether a move can be performed on the chessboard.
	 * @param moveFrom The starting tile
	 * @param moveTo The destination tile
	 * @param keepState Whether to update the CheckUtility's state after checking this move
	 * @return True if the move is allowed to be performed, false otherwise
	 */
	private boolean isLegalMove(Point moveFrom, Point moveTo, boolean keepState)
	{
		if (gameBoard[moveFrom.y][moveFrom.x] == null)
			return false;
		if (moveFrom.equals(moveTo))
			return false;
		if (!checkTurn(moveFrom))
			return false;
		if (BoardUtility.checkFriendlyFire(moveFrom, moveTo, gameBoard))
			return false;
		if (!gameBoard[moveFrom.y][moveFrom.x].isPossibleMove(moveTo))
			return false;


		return checkUtility.isMoveLegal(moveFrom, moveTo, keepState);
	}

	/**
	 * Undoes the last move that was performed.
	 * Removes the undone move and sets the currentTurn back to the previous value
	 */
	public void undoMove()
	{
		Move move = pastMoves.pop();
		gameBoard[move.getFrom().y][move.getFrom().x] = gameBoard[move.getTo().y][move.getTo().x];
		BoardUtility.updatePiecePosition(move.getFrom(), gameBoard);
		gameBoard[move.getTo().y][move.getTo().x] = move.getCapturedPiece();
		BoardUtility.updatePiecePosition(move.getTo(), gameBoard);
	}

	private void handleCheckMate(Color checkMated)
	{
		this.checkMated = checkMated;
		this.gameOver = true;
	}

	/**
	 * Checks that the piece to be moved belongs to the player whose turn it currently is.
	 *
	 * @param moveFrom The piece to be moved
	 * @return True if it's the correct player's turn, false otherwise
	 */
	private boolean checkTurn(Point moveFrom)
	{
		return gameBoard[moveFrom.y][moveFrom.x].getColor() == currentTurn;
	}

	/** Updates the current turn to the next player */
	private void swapTurn()
	{
		currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	/**
	 * Updates the gameboard by performing the specified move. Also logs the
	 * updated gameboard to the console and updates the piece's positions.
	 * The simulated move can be undone by calling undoMove
	 *
	 * @param from The position of the piece to be moved
	 * @param to   The position where the piece should be moved to
	 * @param log  If true, the updated chessboard will be logged to the console
	 * @see ChessGame#undoMove() undoMove
	 */
	public void simulateMove(Point from, Point to, boolean log)
	{
		// TODO: Handle capture (points update, etc...)
		pastMoves.push(new Move(from, to, gameBoard[to.y][to.x], currentTurn));

		if (gameBoard[to.y][to.x] != null)
			gameBoard[to.y][to.x].setPosition(null);
		gameBoard[to.y][to.x] = gameBoard[from.y][from.x];
		gameBoard[from.y][from.x] = null;
		BoardUtility.updatePiecePosition(to, gameBoard);

		if (log) logger.info(String.valueOf(this));
	}

	private void simulateMove(Point from, Point to)
	{
		simulateMove(from, to, true);
	}

	private void setupBoard()
	{
		for (int i = 0; i < 8; i++)
		{
			gameBoard[6][i] = new Pawn(Color.WHITE, new Point(i, 6), this);
			gameBoard[1][i] = new Pawn(Color.BLACK, new Point(i, 1), this);
		}
		gameBoard[0][0] = new Rook(Color.BLACK, new Point(0, 0), this);
		gameBoard[0][1] = new Knight(Color.BLACK, new Point(1, 0), this);
		gameBoard[0][2] = new Bishop(Color.BLACK, new Point(2, 0), this);
		gameBoard[0][3] = new Queen(Color.BLACK, new Point(3, 0), this);
		gameBoard[0][4] = blackKing = new King(Color.BLACK, new Point(4, 0), this);
		gameBoard[0][5] = new Bishop(Color.BLACK, new Point(5, 0), this);
		gameBoard[0][6] = new Knight(Color.BLACK, new Point(6, 0), this);
		gameBoard[0][7] = new Rook(Color.BLACK, new Point(7, 0), this);

		gameBoard[7][0] = new Rook(Color.WHITE, new Point(0, 7), this);
		gameBoard[7][1] = new Knight(Color.WHITE, new Point(1, 7), this);
		gameBoard[7][2] = new Bishop(Color.WHITE, new Point(2, 7), this);
		gameBoard[7][3] = new Queen(Color.WHITE, new Point(3, 7), this);
		gameBoard[7][4] = whiteKing = new King(Color.WHITE, new Point(4, 7), this);
		gameBoard[7][5] = new Bishop(Color.WHITE, new Point(5, 7), this);
		gameBoard[7][6] = new Knight(Color.WHITE, new Point(6, 7), this);
		gameBoard[7][7] = new Rook(Color.WHITE, new Point(7, 7), this);
	}

	/** Get a piece from the chessboard. Assumes that the point is within bounds. */
	private ChessPiece getPiece(@NotNull Point point)
	{
		return getPiece(point.x, point.y);
	}

	/** Get a piece from the chessboard. Assumes that the point is within bounds. */
	private ChessPiece getPiece(int x, int y)
	{
		// Y is our vertical coordinate (file) and x horizontal (rank)
		return gameBoard[y][x];
	}

	/**
	 * Check whether a move is a castle. If so, move the rook accordingly and update the castle status.
	 * If a king or rook is moving, updates castling status accordingly.
	 * Assumes that the move is legal.
	 * @param from The point from where the piece is moving
	 * @param to The point to which the piece is moving
	 * @return An optional pair containing the tile from where the rook moved and to where it moved
	 */
	private Optional<Map<String, String>> handleCastle(Point from, Point to)
	{
		ChessPiece piece = getPiece(from);
		Optional<Map<String, String>> extraMoves = Optional.empty();
		if (piece instanceof King king)
		{
			if (to.x - from.x == 2)
			{
				// Move the king side rook 2 to the left
				Point fromRook = new Point(from.x + 3, from.y);
				Point toRook = new Point(from.x + 1, from.y);
				simulateMove(fromRook, toRook, false);
				extraMoves = Optional.of(Map.of(ChessPositionConverter.pointToTile(fromRook), ChessPositionConverter.pointToTile(toRook)));
			} else if (to.x - from.x == -2)
			{
				// Move the queen side rook 3 to the right
				Point fromRook = new Point(from.x - 4, from.y);
				Point toRook = new Point(from.x - 1, from.y);
				simulateMove(fromRook, toRook, false);
				extraMoves = Optional.of(Map.of(ChessPositionConverter.pointToTile(fromRook), ChessPositionConverter.pointToTile(toRook)));
			}
			// If the king has moved, castling is not possible anymore
			king.removeCastleOption(Castle.KINGSIDE, Castle.QUEENSIDE);
		} else if (piece instanceof Rook rook)
		{
			if (rook.getColor() == Color.WHITE)
			{
				whiteKing.removeCastleOption(rook.getSide());
			} else
			{
				blackKing.removeCastleOption(rook.getSide());
			}
		}

		return extraMoves;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("\n_________________________________\n");
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				String piece = (gameBoard[i][j] != null) ? gameBoard[i][j].toString() : " ";
				builder.append(String.format("| %s ", piece));
			}
			builder.append("|\n");
		}
		builder.append("---------------------------------");

		return builder.toString();
	}

	public Color getCurrentTurn()
	{
		return currentTurn;
	}

	public Color getCheckMated()
	{
		return checkMated;
	}

	public ChessPiece[][] getGameBoard()
	{
		return gameBoard;
	}

	public CheckUtility getCheckUtility()
	{
		return checkUtility;
	}

	public ChessPiece getPiece(String tile)
	{
		return getPiece(ChessPositionConverter.tileToPoint(tile));
	}

	public King getWhiteKing()
	{
		return whiteKing;
	}

	public King getBlackKing()
	{
		return blackKing;
	}

	public boolean isGameOver()
	{
		return gameOver;
	}

}
