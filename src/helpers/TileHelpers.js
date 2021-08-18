export function getTileColor(row, column)
{
    let tileClass = "tile ";
    if (row % 2 === 0)
    {
        if (column % 2 === 0)
        {
            tileClass += "white-tile"; // Row and column index are even
        }
        else
        {
            tileClass += "black-tile"; // Row index is even, column index is uneven
        }
    }
    else if (column % 2 !== 0)
    {
        tileClass += "white-tile"; // Row and colum index are uneven
    }
    else
    {
        tileClass += "black-tile"; // Row index is uneven, column index is even
    }

    return tileClass;
}

export const colMappings = {
 0: "A", 1: "B", 2: "C", 3: "D", 4: "E", 5: "F", 6: "G", 7: "H",
};

// Given grid coordinates, return the corresponding chess coordinate (e.g. 00 => A0)
export function getTileId(row, column)
{
    return `${colMappings[column]}${8 - row}`;
}
