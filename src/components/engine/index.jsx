import React from 'react';
import { useEvent } from '../../hooks';

export default function Engine() {
    const handleKey = (e) => {
        if (e.key === " ") {
            console.log("Spacebar pressed!");
        }
    };

    useEvent("keyup", handleKey);

    return (
      <h1>
          Chess Player
      </h1>
    );
}
