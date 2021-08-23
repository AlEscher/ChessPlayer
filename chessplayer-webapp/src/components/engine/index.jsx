import {
 useEvent, handleDragOver, handleDragStart, handleDragEnter,
 handleDragLeave, handleDrop, handleClick,
} from '../../hooks';

/**
 * Sets up our hooks for drag and drop events.
 * @returns A place holder element for now
 */
export default function Engine() {
    // Register our hooks
    useEvent("dragover", handleDragOver);
    useEvent("dragstart", handleDragStart);
    useEvent("dragenter", handleDragEnter);
    useEvent("dragleave", handleDragLeave);
    useEvent("drop", handleDrop);

    useEvent("mousedown", handleClick);

    return null;
}
