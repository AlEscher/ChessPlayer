import React from 'react';
import TileRow from './TileRow';
import { getTileId } from '../../helpers';
import { playRandomSound } from '../../components/audio';

const black = "dimgrey";
const white = "white";

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
        if (data.extraMoves && Object.keys(data.extraMoves).length > 0)
        {
            const from = Object.keys(data.extraMoves)[0];
            const to = data.extraMoves[from];
            const fromTile = document.getElementById(from);
            const toTile = document.getElementById(to);
            toTile.appendChild(fromTile.childNodes[0]);
        }
    }
}

export function handleMoves(data) {
    data.possibleMoves.forEach(
        (tileID) => {
            const tile = document.getElementById(tileID);
            const preview = document.createElement("span");
            preview.classList.add("movePreview");
            tile.appendChild(preview);
        },
    );
}

/**
 * Add the chess pieces to the chessboard
 */
export function setupChessboard()
{
    const fenString = sessionStorage.game_fen;
    parseFen(fenString);
}

function parseFen(fen)
{
    const positionSection = fen.split(" ")[0];
    // rank -> row; file -> column
    let rank = 0; let file = 0;
    // eslint-disable-next-line one-var-declaration-per-line
    let bishops = 0; let knights = 0; let pawns = 0; let rooks = 0;
    // eslint-disable-next-line no-restricted-syntax
    for (const c of positionSection)
    {
        if (file > 7)
        {
            file = 0;
        } if (!Number.isNaN(parseInt(c, 10)))
        {
            file += parseInt(c, 10);
        } else if (c === '/')
        {
            file = 0;
            rank += 1;
        } else
        {
            const color = c.toUpperCase() === c ? white : black;
            const position = getTileId(rank, file);
            let id = "";
            const prefix = color === white ? "w" : "b";
            switch (c.toUpperCase())
            {
                case 'B':
                    id = `${prefix}_bishop${bishops++}`;
                    break;
                case 'K':
                    id = `${prefix}_king`;
                    break;
                case 'N':
                    id = `${prefix}_knight${knights++}`;
                    break;
                case 'P':
                    id = `${prefix}_pawn${pawns++}`;
                    break;
                case 'Q':
                    id = `${prefix}_queen`;
                    break;
                case 'R':
                    id = `${prefix}_rook${rooks++}`;
                    break;
                default:
                    console.log("Invalid piece identifier!");
                    return;
            }
            document.getElementById(position).appendChild(createPiece(c.toUpperCase(), id, color));
            file += 1;
        }
    }
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
