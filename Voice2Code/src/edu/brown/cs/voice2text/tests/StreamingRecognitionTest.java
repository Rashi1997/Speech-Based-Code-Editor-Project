package edu.brown.cs.voice2text.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1p1beta1.StreamingRecognizeRequest;

import edu.brown.cs.voice2text.ResponseObserverClass;
import edu.brown.cs.voice2text.StreamingRecognition;

public class StreamingRecognitionTest {

	@Test
	public void SRNotNullTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		assertNotNull(sr);
	}
	
	@Test
	public void setRecognitionConfigTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		RecognitionConfig rc = sr.setRecognitionConfig();
		assertNotNull(rc);
	}

	@Test
	public void setStreamingRecognitionTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		sr.setRecognitionConfig();
		StreamingRecognitionConfig src = sr.setStreamingRecognition();
		assertNotNull(src);
	}

	@Test
	public void setStreamingConfigTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		sr.setRecognitionConfig();
		sr.setStreamingRecognition();
		StreamingRecognizeRequest srr = sr.setStreamingConfig();
		assertNotNull(srr);
	}

	@Test
	public void setAudioContentTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		sr.setRecognitionConfig();
		sr.setStreamingRecognition();
		int n =16;
		StreamingRecognizeRequest srr = sr.setAudioContent(new byte[n]);
		String block = "\\000";
		String expected = "audio_content: \""
				+ block.repeat(n)
				+ "\"\n";
		assertEquals(srr.toString(),expected);
	}
	
	@Test
	public void createClientTest() throws Exception{
		StreamingRecognition sr = new StreamingRecognition();
		StreamingRecognition sr1 = Mockito.spy(sr);
		Mockito.doReturn(true).when(sr1).createClient();
		assertEquals(true, sr1.createClient());
	}

	@Test
	public void startClientStreamTest() throws Exception{
		ResponseObserverClass ro = new ResponseObserverClass();
		StreamingRecognition sr = new StreamingRecognition();
		StreamingRecognition sr1 = Mockito.spy(sr);
		Mockito.doReturn(true).when(sr1).startClientStream(ro);
		assertEquals(true, sr1.startClientStream(ro));
	}

	@Test
	public void sendRequestTest() throws Exception{
		StreamingRecognition sr = Mockito.mock(StreamingRecognition.class);
		sr.setRecognitionConfig();
		sr.setStreamingRecognition();
		StreamingRecognizeRequest srr = sr.setStreamingConfig();
		sr.sendRequest(srr);
		verify(sr, times(1)).sendRequest(srr);
	}
	
	@Test
	public void closeSendTest() throws Exception{
		StreamingRecognition sr = Mockito.mock(StreamingRecognition.class);
		sr.closeSend();
		verify(sr, times(1)).closeSend();
	}
}
