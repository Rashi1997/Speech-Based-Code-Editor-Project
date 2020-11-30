package edu.brown.cs.voice2text;


import static edu.brown.cs.voice2text.RecognitionConfiguration.*;

import java.io.IOException;

import com.google.api.gax.rpc.ClientStream;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1p1beta1.StreamingRecognizeRequest;
import com.google.protobuf.ByteString;

public class StreamingRecognition {
	private ClientStream<StreamingRecognizeRequest> clientStream;
	private RecognitionConfig recognitionConfig;
	private StreamingRecognitionConfig streamingRecognitionConfig;
	private SpeechClient client;

	public StreamingRecognition() throws Exception {
		
	}
	
	public boolean createClient() throws IOException {
		client = SpeechClient.create();
		return client!=null;
	}
	public boolean startClientStream(ResponseObserverClass responseObserver) {

		// start/restart clientStream
        clientStream =
                client.streamingRecognizeCallable().splitCall(responseObserver);
        return clientStream!=null;
        
	}
	
	public RecognitionConfig setRecognitionConfig() {
        recognitionConfig =
                RecognitionConfig.newBuilder()
                    .setEncoding(encoding)
                    .setLanguageCode(languageCode)
                    .setModel(model)
                    .setSampleRateHertz(sampleRateHertz)
                    .build();
        return recognitionConfig;
	}
	
	public StreamingRecognitionConfig setStreamingRecognition() {
        streamingRecognitionConfig =
                StreamingRecognitionConfig.newBuilder()
                	.setConfig(recognitionConfig)
                	.setInterimResults(interimResults)
                	.build();
        return streamingRecognitionConfig;
	}
	
	public void sendRequest(StreamingRecognizeRequest request) {
		clientStream.send(request);
	}

	public StreamingRecognizeRequest setAudioContent(byte[] data) {
		return StreamingRecognizeRequest.newBuilder()
	        .setAudioContent(ByteString.copyFrom(data))
	        .build();
	}

	public StreamingRecognizeRequest setStreamingConfig() {
		return StreamingRecognizeRequest.newBuilder()
                .setStreamingConfig(streamingRecognitionConfig)
                .build();
	}

	public void closeSend() {
		clientStream.closeSend();
	}
}
