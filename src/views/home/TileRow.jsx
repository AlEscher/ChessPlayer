import React from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import PropTypes from 'prop-types';

export default function TileRow(props)
{
    const tileRow = [];
    for (let j = 0; j < 8; j++)
    {
        // In row 0, 2, 4, etc... the tile in column 0, 2, 4, etc... has to be white
        const tileClass = getTileColor(props.row, j);

        // Fix error message about duplicate keys
        const tileKey = props.row.toString() + j.toString();
        tileRow.push(
            <div className={tileClass} key={tileKey} />,
        );
    }

    return (
        <div className="tile-row">
            {tileRow}
        </div>
    );
}

TileRow.defaultProps = {
    row: -1,
};
TileRow.propTypes = {
    row: PropTypes.number,
};

function getTileColor(row, column)
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
