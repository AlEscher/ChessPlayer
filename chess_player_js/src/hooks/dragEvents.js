import getAudioPlayer from "../components/audio";

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
        e.dataTransfer.setData("Text", e.target.id);
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
        // Get the data we added in drag start (the piece's id)
        const data = e.dataTransfer.getData("Text");
        e.target.appendChild(document.getElementById(data));
        // Reset the border of the tile we are dropping the piece into
        e.target.style.border = "";
        const audioPlayer = getAudioPlayer();
        audioPlayer.moveSound.audioElement.play();
    }
}

export function handleDragLeave(e) {
    if (e.target.classList.length > 0 && e.target.classList[0] === "tile") {
        e.target.style.border = "";
    }
}
