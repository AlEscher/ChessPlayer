import { handleMoves, movePiece } from "../views/home";

// eslint-disable-next-line import/no-extraneous-dependencies
const $ = require("jquery");

/**
 * A callback function for the "dragover" event.
 * Prevents the default browser action in order to allow
 * our chess pieces to be dragged and dropped.
 * @param {*} e The event that was triggered
 */
export function handleDragOver(e) {
    // Prevent default browser behaviour to allow drop
    e.preventDefault();
}

/**
 * A callback function for the "dragstart" event.
 * Collects and stores data regarding the piece that picked up
 * and the tile it is being dragged from.
 * @param {*} e The event that was triggered
 */
export function handleDragStart(e) {
    // Add the id of the piece that the user just grabbed
    if (e.target.className === "piece")
    {
        e.dataTransfer.setData("pieceID", e.target.id);
        e.dataTransfer.setData("fromTile", e.target.parentElement.id);

        const dataSend = {
            fromTile: e.target.parentElement.id,
            pieceID: e.target.id,
        };

        $.ajax({
            url: "get-moves",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: dataSend,
            success: handleMoves,
        });
    }
    else
    {
        // Prevent tiles etc... from being dragged (draggable="false" does not work)
        e.preventDefault();
    }
}

/**
 * A callback function for the "dragenter" event.
 * Adds a red outline to the tile that is currently being hovered.
 * @param {*} e The event that was triggered
 */
export function handleDragEnter(e) {
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        e.target.style.border = "2px solid gray";
    }
}

/**
 * A callback function for the "drop" event
 * Sends a request to the server to validate the move and plays it if the move is valid.
 * @param {*} e The event that was triggered
 */
export function handleDrop(e) {
    e.preventDefault();
    let targetObj = e.target;
    // Check that we are dropping something onto a tile or a piece
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") targetObj = e.target;
    else if (e.target.className === "piece" || e.target.className === "movePreview") targetObj = e.target.parentElement;
    else return;

    const data = {
        fromTile: e.dataTransfer.getData("fromTile"),
        toTile: targetObj.id,
        pieceID: e.dataTransfer.getData("pieceID"),
    };
    if (data.fromTile !== data.toTile) {
        $.ajax({
            url: "make-move",
            type: "PUT",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(data),
            success: movePiece,
        });
    }

    // Reset the border of the tile we were dropping the piece into
    targetObj.style.border = "";
    // Reset the move previews
    const movePreviews = document.getElementsByClassName("movePreview");
    Array.from(movePreviews).forEach(
        (preview) => {
            const tile = preview.parentElement;
            // Remove last child node
            tile.removeChild(tile.childNodes[tile.childNodes.length - 1]);
        },
    );
}

/**
 * A callback function for the "dragleave" event.
 * Resets the border of the tile that is no longer being hovered.
 * @param {*} e The event that was triggered
 */
export function handleDragLeave(e) {
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        e.target.style.border = "";
    }
}
