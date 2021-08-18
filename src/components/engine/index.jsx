import React from 'react';
import { useEvent } from '../../hooks';

export default function Engine() {
    const handleDragOver = (e) => {
        // Prevent default browser behaviour do allow drag
        e.preventDefault();
    };
    const handleDragStart = (e) => {
        // Add the id of the piece that the user just grabbed
        e.dataTransfer.setData("Text", e.target.id);
    };
    const handleDragEnter = (e) => {
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
            e.target.style.border = "2px solid red";
        }
    };
    const handleDrop = (e) => {
        e.preventDefault();
        // Check that we are dropping something onto a tile
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
            // Get the data we added in drag start (the piece's id)
            const data = e.dataTransfer.getData("Text");
            e.target.appendChild(document.getElementById(data));
            // Reset the border of the tile we are dropping the piece into
            e.target.style.border = "";
        }
    };
    const handleDragLeave = (e) => {
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
            e.target.style.border = "";
        }
    };

    useEvent("dragover", handleDragOver);
    useEvent("dragstart", handleDragStart);
    useEvent("dragenter", handleDragEnter);
    useEvent("dragleave", handleDragLeave);
    useEvent("drop", handleDrop);

    return (
      <h1>
          Chess Player
      </h1>
    );
}
