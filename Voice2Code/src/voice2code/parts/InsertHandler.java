package voice2code.parts;

import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import edu.brown.cs.voice2text.ResponseObserverClass;

import org.eclipse.jface.text.BadLocationException;
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
						document.replace(offset, 0, text.replace("down", ";"));
						styledText.setSelection(styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            String textAtLine = styledText.getLine(currentLine);
			            //int spaces = getLeadingSpaces(textAtLine);
			            styledText.insert("\n");
			            styledText.setCaretOffset(styledText.getCaretOffset());
					} else if("back".equals(roc.tokenize(text)))
					{
						System.out.println("orig " + offset);
						styledText.setSelection(styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            currentLine = Integer.max(0, currentLine - 1);
			            int lineOffset = getOffsetWithinLine(currentLine, offset, document);
						String textAtLine = styledText.getLine(currentLine);
						String textBefore = textAtLine.substring(0, lineOffset);
						System.out.println(moveBack(textBefore));
						System.out.println(offset);
						System.out.println(offset + moveBack(textBefore));
			            styledText.setCaretOffset(offset + moveBack(textBefore));
			            
					} else if("forward".equals(roc.tokenize(text)))
					{
						
						styledText.setSelection(styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            currentLine = Integer.max(0, currentLine - 1);
			            int lineOffset = getOffsetWithinLine(currentLine, offset, document);
						String textAtLine = styledText.getLine(currentLine);
						String textAfter = textAtLine.substring(lineOffset);
						System.out.println(moveForward(textAfter));
			            styledText.setCaretOffset(offset + moveForward(textAfter));
					} else if ("left".equals(roc.tokenize(text))){
						int newOffset = Integer.max(0, offset - 1);
			            styledText.setCaretOffset(newOffset);
			            
					} else if("right".equals(roc.tokenize(text)))
					{
						int newOffset = Integer.min(styledText.getCharCount(), offset + 1);
			            styledText.setCaretOffset(newOffset);
					} else if("line".equals(roc.tokenize(text)))
					{
						styledText.setSelection(styledText.getCharCount());
						
						int lineToGoTo = 5;
						lineToGoTo = Integer.min(lineToGoTo, styledText.getLineCount());
						System.out.println(styledText.getLineCount());
						lineToGoTo = Integer.max(lineToGoTo, 0);
			            int lineOffset = getOffsetOfLine(lineToGoTo, document);
			            styledText.setCaretOffset(lineOffset);
					}
					
					
					else if (roc.tokenize(text) != null) {
						System.out.println("x: " + text);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// commented out
				//styledText.setCaretOffset(offset + text.length() + 1);
				
				//Try while loop
			}
			
		});
	}
	
	private int getOffsetWithinLine(int lineNumber, int offset, IDocument doc) throws BadLocationException {
		int sum = 0;
        for (int i = 0 ; i < lineNumber; i++) {
        	sum += doc.getLineLength(i);
        }
        return offset - sum;
	}
	
	private int getOffsetOfLine(int lineNumber,IDocument doc) throws BadLocationException {
		int sum = 0;
        for (int i = 0 ; i < lineNumber; i++) {
        	sum += doc.getLineLength(i);
        }
        return sum;
	}
	
	// Returns offset of last word
	private int moveBack(String text) {
		
		int index = text.length() - 1;
		int start = index;
		// Move back past any letters/nums
		while (index >= 0) {
			char c = text.charAt(index);
			if (Character.isDigit(c) || Character.isLetter(c)) {
				index--;
			} else {
				break;
			}
		}
		// Move back past any non letters/nums
		while (index >= 0) {
			char c = text.charAt(index);
			if (!(Character.isDigit(c) || Character.isLetter(c))) {
				index--;
			} else {
				break;
			}
		}
		return index - start;
	}
	
	    // Returns offset of next word
		private int moveForward(String text) {
			
			int index = 0;
			int start = index;
			// Move forward past any letters/nums
			while (index < text.length()) {
				char c = text.charAt(index);
				if (Character.isDigit(c) || Character.isLetter(c)) {
					index++;
				} else {
					break;
				}
			}
			// Move forward past any non letters/nums
			while (index < text.length()) {
				char c = text.charAt(index);
				if (!(Character.isDigit(c) || Character.isLetter(c))) {
					index++;
				} else {
					break;
				}
			}
			return index - start;
		}
	
}
	
//	private void moveCursorToLineNumber(int lineNumber) {
//		
//		int lineOffset;
//		
//		try {
//			lineOffset = this.document.getLineLength(lineNumber);
//			this.editor.selectAndReveal(lineOffset, 0);
//		} catch (BadLocationException e) {
//			// calm down
//		}
//		
//	}

