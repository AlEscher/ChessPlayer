import React from 'react';
import PropTypes from 'prop-types';
import { getTileColor, getTileId } from '../../helpers';

export default function TileRow({ row })
{
    const tileRow = [];
    for (let j = 0; j < 8; j++)
    {
        // In row 0, 2, 4, etc... the tile in column 0, 2, 4, etc... has to be white
        const tileClass = getTileColor(row, j);
        const tileId = getTileId(row, j);

        // Fix error message about duplicate keys
        const tileKey = row.toString() + j.toString();
        tileRow.push(
            <div className={tileClass} key={tileKey} id={tileId} draggable="false" />,
        );
    }

    return (
        <div className="tile-row">
            {tileRow}
        </div>
    );
}

TileRow.propTypes = {
    row: PropTypes.number.isRequired,
};
