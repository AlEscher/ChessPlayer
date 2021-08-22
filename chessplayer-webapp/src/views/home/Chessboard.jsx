import React from 'react';
import TileRow from './TileRow';
import { colMappings } from '../../helpers';
import { playRandomSound } from '../../components/audio';

/**
 * Creates and returns the chessboard that will be displayed to the user
 * @returns A list of rows, each containing tiles
 */
export default function ChessBoard() {
    const chessboard = [];

    for (let i = 0; i < 8; i++)
    {
       chessboard.push(<TileRow row={i} key={i} />);
    }
    return (
        <div>{ chessboard }</div>
    );
}

/**
 * Add the chess pieces to the chessboard
 */
export function setupChessboard()
{
    setupBlackPieces();
    setupWhitePieces();
}

/**
 * Make the move specified by the server's response, if it is allowed
 * @param {*} data A JSON object containing the server's response
 */
export function movePiece(data) {
    if (data.legal)
    {
        const sourceId = data.pieceID;
        // Tile we are dropping the piece onto
        const target = document.getElementById(data.toTile);
        // Check if we are capturing
        if (target.hasChildNodes())
        {
            target.removeChild(target.childNodes[0]);
            playRandomSound("captureSound");
        }
        else
        {
            // Play move sound
            playRandomSound("moveSound");
        }
        target.appendChild(document.getElementById(sourceId));
    }
}

export function handleMoves(data) {
    console.log(data);
}

function setupBlackPieces()
{
    const rookL = createPiece("R", "b_rook_l", "dimgrey");
    const rookR = createPiece("R", "b_rook_r", "dimgrey");
    const knightR = createPiece("N", "b_knight_r", "dimgrey");
    const knightL = createPiece("N", "b_knight_l", "dimgrey");
    const bishopL = createPiece("B", "b_bishop_l", "dimgrey");
    const bishopR = createPiece("B", "b_bishop_r", "dimgrey");
    const queen = createPiece("Q", "b_queen", "dimgrey");
    const king = createPiece("K", "b_king", "dimgrey");

    document.getElementById("A8").appendChild(rookL);
    document.getElementById("B8").appendChild(knightL);
    document.getElementById("C8").appendChild(bishopL);
    document.getElementById("D8").appendChild(king);
    document.getElementById("E8").appendChild(queen);
    document.getElementById("F8").appendChild(bishopR);
    document.getElementById("G8").appendChild(knightR);
    document.getElementById("H8").appendChild(rookR);

    setupPawns("dimgrey", 7);
}

function setupWhitePieces()
{
    const rookL = createPiece("R", "w_rook_l", "white");
    const rookR = createPiece("R", "w_rook_r", "white");
    const knightR = createPiece("N", "w_knight_r", "white");
    const knightL = createPiece("N", "w_knight_l", "white");
    const bishopL = createPiece("B", "w_bishop_l", "white");
    const bishopR = createPiece("B", "w_bishop_r", "white");
    const queen = createPiece("Q", "w_queen", "white");
    const king = createPiece("K", "w_king", "white");

    document.getElementById("A1").appendChild(rookL);
    document.getElementById("B1").appendChild(knightL);
    document.getElementById("C1").appendChild(bishopL);
    document.getElementById("D1").appendChild(king);
    document.getElementById("E1").appendChild(queen);
    document.getElementById("F1").appendChild(bishopR);
    document.getElementById("G1").appendChild(knightR);
    document.getElementById("H1").appendChild(rookR);

    setupPawns("white", 2);
}

function createPiece(name, id, color)
{
    const piece = document.createElement("p");
    piece.innerText = name;
    piece.id = id;
    piece.style.color = color;
    piece.draggable = true;
    piece.classList.add("piece");
    return piece;
}

function setupPawns(color, row)
{
    const prefix = color === "white" ? "w_" : "b_";
    for (let i = 0; i < 8; i++)
    {
        document.getElementById(`${colMappings[i]}${row}`).appendChild(createPiece("P", `${prefix}pawn${i}`, color));
    }
}
