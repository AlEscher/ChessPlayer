import React from 'react';
import TileRow from './TileRow';
import { colMappings } from '../../helpers';

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

export function setupChessboard() {
    const rookL = createPiece("R", "rook_l");
    const rookR = createPiece("R", "rook_r");
    const knightR = createPiece("N", "knight_r");
    const knightL = createPiece("N", "knight_l");
    const bishopL = createPiece("B", "bishop_l");
    const bishopR = createPiece("B", "bishop_r");
    const queen = createPiece("Q", "queen");
    const king = createPiece("K", "king");

    document.getElementById("A8").appendChild(rookL);
    document.getElementById("B8").appendChild(knightL);
    document.getElementById("C8").appendChild(bishopL);
    document.getElementById("D8").appendChild(king);
    document.getElementById("E8").appendChild(queen);
    document.getElementById("F8").appendChild(bishopR);
    document.getElementById("G8").appendChild(knightR);
    document.getElementById("H8").appendChild(rookR);

    setupPawns();
}

function createPiece(name, id)
{
    const piece = document.createElement("p");
    piece.innerText = name;
    piece.id = id;
    piece.draggable = true;
    piece.classList.add("piece");
    return piece;
}

function setupPawns()
{
    for (let i = 0; i < 8; i++)
    {
        document.getElementById(`${colMappings[i]}${7}`).appendChild(createPiece("P", `pawn${i}`));
    }
}
