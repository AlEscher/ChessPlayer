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
