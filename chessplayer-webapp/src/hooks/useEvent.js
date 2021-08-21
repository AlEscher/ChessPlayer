import { useEffect } from 'react';

/**
 * Registers a window event listener which will automatically clean itself up
 * @param {*} event The event we want to hook
 * @param {*} handler The callback function
 */
export default function useEvent(event, handler) {
  useEffect(() => {
    window.addEventListener(event, handler);

    // Remove the event listener when the component is re-rendered
    return function cleanup() { window.removeEventListener(event, handler); };
  });
}
