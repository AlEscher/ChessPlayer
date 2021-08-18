import React from 'react';
import TileRow from './TileRow';

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
    const tiles = document.getElementsByClassName("tile");
    const piece = document.createElement("p");
    piece.innerText = "Hi";
    piece.draggable = true;
    piece.classList.add("piece");
    tiles[2].appendChild(piece);
}
