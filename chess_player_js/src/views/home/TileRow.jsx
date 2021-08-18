import React from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import PropTypes from 'prop-types';
import { getTileColor, getTileId } from '../../helpers';

export default function TileRow(props)
{
    const tileRow = [];
    for (let j = 0; j < 8; j++)
    {
        // In row 0, 2, 4, etc... the tile in column 0, 2, 4, etc... has to be white
        const tileClass = getTileColor(props.row, j);
        const tileId = getTileId(props.row, j);

        // Fix error message about duplicate keys
        const tileKey = props.row.toString() + j.toString();
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

TileRow.defaultProps = {
    row: -1,
};
TileRow.propTypes = {
    row: PropTypes.number,
};
