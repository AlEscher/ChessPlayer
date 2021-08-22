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
        let targetObj = null;
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile")
        {
            targetObj = e.target;
        }
        else if (e.target.className === "piece")
        {
            targetObj = e.target.parentElement;
        }
        else
        {
            return;
        }

        // Toggle border on the tile
        if (!targetObj.style.border)
        {
            targetObj.style.border = "4px solid red";
        }
        else
        {
            targetObj.style.border = "";
        }
    }
    else if (e.button === 0) // Left click
    {
        // Reset all tile borders if we click on a tile
        if (e.target.classList.length > 0 && e.target.classList[0] === "tile")
        {
            const tiles = Array.from(document.getElementsByClassName("tile"));
            tiles.forEach((element) => {
                // eslint-disable-next-line no-param-reassign
                element.style.border = "";
            });
        }
    }
}
