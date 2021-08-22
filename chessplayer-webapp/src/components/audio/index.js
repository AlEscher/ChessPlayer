import setupAudioContext from "./soundEffects";

// eslint-disable-next-line import/no-mutable-exports
let audioPlayer = null;

export default function getAudioPlayer() {
    if (audioPlayer == null) audioPlayer = setupAudioContext();

    return audioPlayer;
}

/**
 * Picks a random sound from the specified soundClass and plays it
 * @param {String} soundClass The audio element's className
 */
export function playRandomSound(soundClass)
{
    const audioElements = document.getElementsByClassName(soundClass);
    const index = Math.floor(Math.random() * audioElements.length);
    console.log(`Playing ${soundClass}: ${index}`);
    audioElements[index].play();
}
