package voice2code.parts;

import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import edu.brown.cs.voice2text.Voice2Text;

public class Voice2Code {
	private Button button;
	private boolean started;
	private Voice2Text v2t;
	private InsertHandler ih;
	
	@PostConstruct
	public void createPartControl(Composite parent) throws Exception {
		ih = new InsertHandler();
		
		System.out.println("Enter in Button postConstruct");
		v2t = new Voice2Text(ih);
		
		parent.setLayout(new FillLayout());
		
	    button = new Button(parent, SWT.PUSH);
	    
	    button.setImage(getMicrophoneImage("icons/microphone.png"));
	    
	    setButton2Start();
	    button.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
	          switch (e.type) {
	          case SWT.Selection:
	            if (started) {
	            	setButton2Start();
	            	
	            	v2t.stop();
	            }
	            else {
	            	setButton2Stop();
	            	
	            	v2t.start();
	            }
	            break;
	          }
	        }
	      });
	}
	
	private void setButton2Start() {
		started = false;
		button.setBackground(new Color(Display.getCurrent(), 152, 251, 152, 100));
		button.setText("Press to Speak");
	}
	
	private void setButton2Stop() {
		started = true;
		button.setBackground(new Color(Display.getCurrent(), 255, 40, 0, 100));
		button.setText("Press to Stop");
	}
	
	private Image getMicrophoneImage(String path) {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		final URL fullPathString = FileLocator.find(bundle, new Path(path), null);
	
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
	
		Image image = imageDesc.createImage();
		ImageData imageData = image.getImageData();
		imageData = imageData.scaledTo(100, 100);
		
		return new Image(Display.getCurrent(), imageData);
	}
}
