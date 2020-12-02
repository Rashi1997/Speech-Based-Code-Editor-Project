package edu.brown.cs.voice2text;

import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1p1beta1.StreamingRecognizeResponse;

import edu.brown.cs.v2cplugin.InsertHandler;

import java.util.StringTokenizer;

public class ResponseObserverClass implements ResponseObserver<StreamingRecognizeResponse>{
	private InsertHandler insertHandler;
	//public String transcript;
	public ResponseObserverClass() {
	}
	
	public ResponseObserverClass(InsertHandler ih) {
		insertHandler = ih;
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
		System.out.println("Final: " + ((StreamingRecognizeResponse) response).getResults(0).getIsFinal());
		StringTokenizer st = new StringTokenizer(transcript);
		while (st.hasMoreTokens()) {  
			String token = st.nextToken();
			System.out.println("Tokenize:" + token );
		}
//		insertHandler.insertText(transcript);
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
