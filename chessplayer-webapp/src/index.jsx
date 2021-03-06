import React from 'react';
import ReactDOM from 'react-dom';
import './globals.scss';
import { ChessBoard, Home, setupChessboard } from './views/home';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(<Home />, document.getElementById('root'));

ReactDOM.render(<ChessBoard />, document.getElementById("chessboard"));

setupChessboard();

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
