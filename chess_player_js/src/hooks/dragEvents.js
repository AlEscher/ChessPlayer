import getAudioPlayer from "../components/audio";

// eslint-disable-next-line import/no-extraneous-dependencies
const $ = require("jquery");

function movePiece(data)
{
    // Get the data we added in drag start (the piece's id)
    if (data.legal)
    {
        const sourceId = data.pieceID;
        const target = document.getElementById(data.toTile);
        target.appendChild(document.getElementById(sourceId));

        // Play move sound
        const audioPlayer = getAudioPlayer();
        audioPlayer.moveSound.audioElement.play();
    }
}

export function handleDragOver(e) {
    // Prevent default browser behaviour to allow drop
    e.preventDefault();
    console.log(`Drag over: ${e.target.id}`);
}

export function handleDragStart(e) {
    console.log(`Drag start: ${e.target.id}`);
    console.log(`Drag start: ${e.target}`);
    // Add the id of the piece that the user just grabbed
    if (e.target.className === "piece")
    {
        e.dataTransfer.setData("pieceID", e.target.id);
        e.dataTransfer.setData("fromTile", e.target.parentElement.id);
    }
    else
    {
        // Prevent tiles etc... from being dragged (draggable="false" does not work)
        e.preventDefault();
    }
}

export function handleDragEnter(e) {
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        e.target.style.border = "2px solid red";
    }
}

export function handleDrop(e) {
    e.preventDefault();
    // Check that we are dropping something onto a tile
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        const data = { fromTile: e.dataTransfer.getData("fromTile"), toTile: e.target.id, pieceID: e.dataTransfer.getData("pieceID") };
        $.ajax({
            url: "make-move",
            type: "PUT",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(data),
            success: movePiece,
        });
        // Reset the border of the tile we were dropping the piece into
        e.target.style.border = "";
    }
}

export function handleDragLeave(e) {
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        e.target.style.border = "";
    }
}
