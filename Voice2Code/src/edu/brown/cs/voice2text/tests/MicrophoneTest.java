package edu.brown.cs.voice2text.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;

import org.junit.jupiter.api.Test;

import edu.brown.cs.voice2text.Microphone;


public class MicrophoneTest {
	
	@Test
	public void setAudioFormatTest() throws Exception {
		Microphone m = new Microphone();
		AudioFormat audioFormat = m.setAudioFormat();
		assertNotNull(audioFormat);
	}

	@Test
	public void setTargetInfoTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		Info targetInfo = m.setTargetInfo();
		assertNotNull(targetInfo);
	}

	@Test
	public void setTargetDataLineTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		TargetDataLine targetDataLine = m.setTargetDataLine();
		assertNotNull(targetDataLine);
	}

	@Test
	public void checkMicrophoneTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		boolean check = m.checkMicrophone();
		assertEquals(true, check);
	}

	@Test
	public void openMicrophoneTrueTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.setTargetDataLine();
		boolean check = m.open();
		assertEquals(true, check);
	}

	@Test
	public void openMicrophoneFalseTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		boolean check = m.open();
		assertEquals(false, check);
	}

	@Test
	public void startAudioCaptureTrueTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.setTargetDataLine();
		m.open();
		boolean check = m.start();
		assertEquals(true, check);
	}

	@Test
	public void startAudioCaptureFalseTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.open();
		boolean check = m.start();
		assertEquals(false, check);
	}

	@Test
	public void getAudioTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.setTargetDataLine();
		m.open();
		m.start();
		AudioInputStream audio = m.getAudio();
		assertNotNull(audio);
	}

	@Test
	public void closeAudioCaptureTrueTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.setTargetDataLine();
		m.open();
		m.start();
		boolean check = m.close();
		assertEquals(true, check);
	}

	@Test
	public void closeAudioCaptureFalseTest() throws Exception {
		Microphone m = new Microphone();
		m.setAudioFormat();
		m.setTargetInfo();
		m.open();
		m.start();
		boolean check = m.close();
		assertEquals(false, check);
	}
}
