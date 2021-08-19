import React from 'react';
import {
 useEvent, handleDragOver, handleDragStart, handleDragEnter, handleDragLeave, handleDrop,
} from '../../hooks';

export default function Engine() {
    // Register our hooks
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
