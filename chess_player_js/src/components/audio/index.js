import setupAudioContext from "./soundEffects";

// eslint-disable-next-line import/no-mutable-exports
let audioPlayer = null;

export default function getAudioPlayer() {
    if (audioPlayer == null) audioPlayer = setupAudioContext();

    return audioPlayer;
}
