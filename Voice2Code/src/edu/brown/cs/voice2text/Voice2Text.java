package edu.brown.cs.voice2text;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import com.google.cloud.speech.v1p1beta1.StreamingRecognizeRequest;

import voice2code.parts.InsertHandler;

public class Voice2Text implements Runnable{
	private StreamingRecognition streamingRecognition;
	private ResponseObserverClass responseObserver;
	private AudioInputStream audio;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private Microphone microphone;
	
	public Voice2Text() throws Exception{
		streamingRecognition = new StreamingRecognition(responseObserver);
        responseObserver = new ResponseObserverClass();
        microphone = new Microphone();
        microphone.checkMicrophone();
	}
	
	public Voice2Text(InsertHandler ih) throws Exception{
		streamingRecognition = new StreamingRecognition(responseObserver);
        responseObserver = new ResponseObserverClass(ih);
        microphone = new Microphone();
        microphone.checkMicrophone();
	}
	
	public void start() {
		System.out.println("V2T Start");
		running.set(true);
		Thread t = new Thread(this);
		t.start();
	}
	public void stop() {
		System.out.println("V2T Stop");
		running.set(false);
	}

	@Override
	public void run() {
		
		streamingRecognition.startClientStream(responseObserver);
		streamingRecognition.setRecognitionConfig();
		streamingRecognition.setStreamingRecognition();
		StreamingRecognizeRequest request = streamingRecognition.setStreamingConfig();

        streamingRecognition.sendRequest(request);
		
        microphone.open();
        microphone.start();
        System.out.println("Mic and CloudStream initialized");
        // Audio Input Stream
        audio = microphone.getAudio();
        
        while (running.get()) {
            byte[] data = new byte[6400];
            try {
				audio.read(data);
				request = streamingRecognition.setAudioContent(data);
                streamingRecognition.sendRequest(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // close clientStream
        streamingRecognition.closeSend();
        microphone.close();
	}
	
	// For unit testing purposes
	public StreamingRecognition getStreamingRecognition() {
		return streamingRecognition;
	}

	public ResponseObserverClass getResponseObserver() {
		return responseObserver;
	}

	public AudioInputStream getAudio() {
		return audio;
	}

	public Microphone getMicrophone() {
		return microphone;
	}

	public AtomicBoolean getRunning() {
		return running;
	}
	
	
	
}