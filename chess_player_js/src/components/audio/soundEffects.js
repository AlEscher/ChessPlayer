// for legacy browsers
const AudioContext = window.AudioContext || window.webkitAudioContext;

// The object that holds our loaded sounds
const audioPlayer = {
    context: null,
    moveSound: {
        audioElement: null,
        track: null,
    },
};

export default function setupAudioContext() {
    audioPlayer.context = new AudioContext();
    audioPlayer.moveSound.audioElement = document.querySelector("audio");
    // eslint-disable-next-line max-len
    audioPlayer.moveSound.track = audioPlayer.context.createMediaElementSource(audioPlayer.moveSound.audioElement);
    audioPlayer.moveSound.track.connect(audioPlayer.context.destination);

    return audioPlayer;
}
