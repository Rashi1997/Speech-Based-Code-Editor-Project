package edu.brown.cs.voice2text;

import static edu.brown.cs.voice2text.AudioFormatConfiguration.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;

public class Microphone {
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private DataLine.Info targetInfo;
	
	public Microphone() throws Exception {
		
	}
	
	public AudioFormat setAudioFormat() {
        // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
        // bigEndian: false
        // TODO some examples set bigEndian as true on Mac. strange
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, numberOfChannels, signed, bigEndian);
        return audioFormat;
	}
	
	public Info setTargetInfo() {
        targetInfo =
                new Info(
                    TargetDataLine.class,
                    audioFormat);
        return targetInfo;
	}

	public TargetDataLine setTargetDataLine() throws Exception {
        // Target data line captures the audio stream the microphone produces.
        targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
        return targetDataLine;
	}
	
	public boolean checkMicrophone() {
		if (!AudioSystem.isLineSupported(targetInfo)) {
          System.out.println("Microphone not supported");
          System.exit(0);
        }
		return AudioSystem.isLineSupported(targetInfo);
	}
	
	public boolean open() {
		try {
			targetDataLine.open(audioFormat);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean start() {
		try {
			targetDataLine.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public AudioInputStream getAudio() {
		return new AudioInputStream(targetDataLine);
	}
	
	public boolean close() {
		try {
			targetDataLine.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
