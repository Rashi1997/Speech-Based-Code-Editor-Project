package edu.brown.cs.voice2text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

public class Voice2Text implements Runnable{
	private SpeechClient client;
	private ResponseObserver<StreamingRecognizeResponse> responseObserver;
	private ClientStream<StreamingRecognizeRequest> clientStream;
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private AudioInputStream audio;
	private Thread thread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public Voice2Text() throws Exception{
		client = SpeechClient.create();
        responseObserver =
        	new ResponseObserver<StreamingRecognizeResponse>() {
	        	public void onStart(StreamController controller) {}
	
	        	public void onResponse(StreamingRecognizeResponse response) {
	        		System.out.println("Transcript: " + response.getResults(0).getAlternatives(0).getTranscript());
	        		System.out.println("Final: " + response.getResults(0).getIsFinal());
			      
	        		//TODO call Text2Code here to send response
	        	}
	        	public void onComplete() {}
	
	        	public void onError(Throwable t) {
	        		System.out.println(t);
	        	}
	        };

        // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
        // bigEndian: false
        // TODO some examples set bigEndian as true on Mac. strange
        audioFormat = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info targetInfo =
            new Info(
                TargetDataLine.class,
                audioFormat);

        if (!AudioSystem.isLineSupported(targetInfo)) {
          System.out.println("Microphone not supported");
          System.exit(0);
        }
        
        // Target data line captures the audio stream the microphone produces.
        targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
	}
	
	public void start() {
		System.out.println("V2T Start");
		thread = new Thread(this);
		thread.start();
	}
	public void stop() {
		System.out.println("V2T Stop");
		running.set(false);
	}

	@Override
	public void run() {
		running.set(true);
		
		// start/restart clientStream
        clientStream =
                client.streamingRecognizeCallable().splitCall(responseObserver);
        
        RecognitionConfig recognitionConfig =
                RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-US")
                    .setModel("default")
                    .setSampleRateHertz(16000)
                    .build();
        
        StreamingRecognitionConfig streamingRecognitionConfig =
            StreamingRecognitionConfig.newBuilder()
            	.setConfig(recognitionConfig)
            	.build();

        // send config to server
        StreamingRecognizeRequest request =
            StreamingRecognizeRequest.newBuilder()
                .setStreamingConfig(streamingRecognitionConfig)
                .build();
        clientStream.send(request);
		
        try {
			targetDataLine.open(audioFormat);
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
        targetDataLine.start();
        System.out.println("Mic and CloudStream initialized");
        // Audio Input Stream
        audio = new AudioInputStream(targetDataLine);
        
        while (running.get()) {
            byte[] data = new byte[6400];
            try {
				audio.read(data);
                request =
                        StreamingRecognizeRequest.newBuilder()
                            .setAudioContent(ByteString.copyFrom(data))
                            .build();
                clientStream.send(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // close clientStream
        clientStream.closeSend();
	}
}
