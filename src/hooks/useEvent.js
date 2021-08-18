import { useEffect } from 'react';

// Registers a window event listener which will automatically clean itself up
export default function useEvent(event, handler) {
  useEffect(() => {
    window.addEventListener(event, handler);

    // Remove the event listener when the component is re-rendered
    return function cleanup() { window.removeEventListener(event, handler); };
  });
}
