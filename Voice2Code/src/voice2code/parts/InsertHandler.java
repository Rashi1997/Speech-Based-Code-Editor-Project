package voice2code.parts;

import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import edu.brown.cs.voice2text.ResponseObserverClass;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class InsertHandler {
	public InsertHandler() {}
	
	public void insertText(String text) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
			  	IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			  	IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor)part;
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());
				
				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				int offset = styledText.getCaretOffset();
				ResponseObserverClass roc = new ResponseObserverClass();
				try {
					if("down".equals(roc.tokenize(text)))
					{
//						System.out.println("Current count: " + styledText.getCharCount());
//						System.out.println("Current line: " + styledText.getLineAtOffset(styledText.getCaretOffset()));
						document.replace(offset, 0, text.replace("down", ";"));
						styledText.setSelection(styledText.getCharCount());
						
//						System.out.println("New count: " + styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            String textAtLine = styledText.getLine(currentLine);
//			            int spaces = getLeadingSpaces(textAtLine);
			            styledText.insert("\n");
			            styledText.setCaretOffset(styledText.getCaretOffset());
//			            for (int i = 0; i < spaces; i++) {
//			            	styledText.append(" ");
//			            }
//			            System.out.println("Final offset: " + styledText.getCaretOffset());
//			            System.out.println("Final line: " + styledText.getLineAtOffset(styledText.getCaretOffset()));
			            
			            styledText.setCaretOffset(offset + text.length() + 1);
					}
					else if("up".equals(roc.tokenize(text)))
					{
						
						document.replace(offset, 0, text.replace("up", ""));
						
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
						System.out.println("Current offset: " + styledText.getCaretOffset());
						System.out.println("Current line: " + currentLine);
						int newLoc = styledText.getOffsetAtLine(currentLine - 1);
//			            String textAtLine = styledText.getLine(currentLine);
			            //int spaces = getLeadingSpaces(textAtLine);
//			            styledText.insert("\n");
//						styledText.setSelection(newLoc);
			            styledText.setCaretOffset(newLoc);
			            System.out.println("Final offset: " + newLoc);
			            System.out.println("Final line: " + styledText.getLineAtOffset(newLoc));
					} else if ("next".equals(roc.tokenize(text))) {
						document.replace(offset, 0, text.replace("next", ""));
						
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
						System.out.println("Current offset: " + styledText.getCaretOffset());
						System.out.println("Current line: " + currentLine);
						int newLoc = styledText.getOffsetAtLine(currentLine + 1);
//			            String textAtLine = styledText.getLine(currentLine);
			            //int spaces = getLeadingSpaces(textAtLine);
//			            styledText.insert("\n");
//						styledText.setSelection(newLoc);
			            styledText.setCaretOffset(newLoc);
			            System.out.println("Final offset: " + newLoc);
			            System.out.println("Final line: " + styledText.getLineAtOffset(newLoc));
					}
					else {
						styledText.setCaretOffset(offset + text.length() + 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				styledText.setCaretOffset(offset + text.length() + 1);
				
				
				//Try while loop
			}
			
		});
	}
}
