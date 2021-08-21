/**
 * A callback function that will be called when the user right clicks.
 * If he clicked on a tile, the tile will be marked with a border,
 * or if it is already marked the border will be removed
 * @param {*} e
 */
export default function handleClick(e) {
    if (e.button === 2) // Right click
    {
        // Check if we are clicking on a tile or on a piece that is in a tile
        let target = null;
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile")
        {
            target = e.target;
        }
        else if (e.target.className === "piece")
        {
            target = e.target.parentElement;
        }
        else
        {
            return;
        }

        // Toggle border on the tile
        if (!target.style.border)
        {
            target.style.border = "4px solid red";
        }
        else
        {
            target.style.border = "";
        }
    }
}
