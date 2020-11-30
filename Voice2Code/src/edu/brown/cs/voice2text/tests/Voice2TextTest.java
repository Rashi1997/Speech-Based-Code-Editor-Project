package edu.brown.cs.voice2text.tests;



import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.brown.cs.voice2text.ResponseObserverClass;
import edu.brown.cs.voice2text.Voice2Text;

public class Voice2TextTest {
	
	
	@Test
	public void Test() throws Exception {
		Voice2Text v2t1 = Mockito.mock(Voice2Text.class);
		v2t1.start();
		v2t1.stop();
		verify(v2t1, times(1)).start();
		
	}

	
	@Test
	public void ROCNotNullTest() {
		ResponseObserverClass ro2 = new ResponseObserverClass();
		ResponseObserverClass ro = new ResponseObserverClass(null);
		assertNotNull(ro);
		assertNotNull(ro2);
	}
	
//	@Test
//	public void NotNullTest() throws Exception {
//		Voice2Text v2t = new Voice2Text();
//		assertNotNull(v2t);
//		
//		v2t.start();
//		assertEquals(v2t.getRunning().get(), true);
//		assertNotNull(v2t.getStreamingRecognition());
//		assertNotNull(v2t.getResponseObserver());
////		assertNotNull(v2t.getAudio());
//		assertNotNull(v2t.getMicrophone());
//		assertNotNull(v2t.getRunning());
//		v2t.stop();
//		assertEquals(v2t.getRunning().get(), false);
//	}

}


