package edu.brown.cs.voice2text;

import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1p1beta1.StreamingRecognizeResponse;

import edu.brown.cs.plugin.editor.Handler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import edu.brown.cs.text2code.*;

public class ResponseObserverClass implements ResponseObserver<StreamingRecognizeResponse>{
	
	private TextToCommands textToCommandHandler;
	
	public ResponseObserverClass() {
		textToCommandHandler = new TextToCommands();
	}
	

	@Override
	public void onStart(StreamController controller) {
		// TODO Auto-generated method stub		
	}


	@Override
	public void onResponse(StreamingRecognizeResponse response) {
		// TODO Auto-generated method stub
		String transcript = ((StreamingRecognizeResponse) response).getResults(0).getAlternatives(0).getTranscript();
		System.out.println("Transcript: " + transcript);
		System.out.println("Confidence: " + ((StreamingRecognizeResponse) response).getResults(0).getAlternatives(0).getConfidence());
		List<String> wordList = new ArrayList<String>(Arrays.asList(transcript.split(" ")));
		textToCommandHandler.process(wordList);
	}
	
 	public String tokenize(String token)
	{
 		StringTokenizer key = new StringTokenizer(token);
 		String command ="";
 		   while(key.hasMoreTokens())
 		   {
		      command = key.nextToken();
 		   }
		   return command;	
	}


	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		System.out.println(t);
		
	}


	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

}
