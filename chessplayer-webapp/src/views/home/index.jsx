import React from 'react';
import Engine from '../../components/engine';
import ChessBoard, { setupChessboard, movePiece, handleMoves } from './Chessboard';

export function Home() {
    return (<Engine />);
}

export {
 ChessBoard, setupChessboard, movePiece, handleMoves,
};
