package edu.brown.cs.plugin.editor;

import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import edu.brown.cs.voice2text.ResponseObserverClass;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class Handler {
	public Handler() {}
	
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
					//if("down".equals(roc.tokenize(text)))
					//{
						document.replace(offset, 0, text.replace("down", ";"));
						styledText.setSelection(styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            String textAtLine = styledText.getLine(currentLine);
			            //int spaces = getLeadingSpaces(textAtLine);
			            styledText.insert("\n");
			            styledText.setCaretOffset(styledText.getCaretOffset());
					//}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				styledText.setCaretOffset(offset + text.length() + 1);
				
				//Try while loop
			}
			
		});
	}
	
	public void moveCursorUp() {
		
	}
	
	public void moveCursorDown() {
		
	}
	
	// Moves right one word
	public void moveCursorRight() {
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
					styledText.setSelection(styledText.getCharCount());
					int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			        currentLine = Integer.max(0, currentLine - 1);
			        int lineOffset = getOffsetWithinLine(currentLine, offset, document);
					String textAtLine = styledText.getLine(currentLine);
					String textAfter = textAtLine.substring(lineOffset);
					System.out.println(moveForward(textAfter));
			        styledText.setCaretOffset(offset + moveForward(textAfter));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Moves left one word
	public void moveCursorLeft() {
		/*
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
        styledText.setCaretOffset(offset + moveBack(textBefore));*/
	}
	
	public void moveCursorToEndOfLine() {
		
	}
	
	public void moveCursorToStartOfLine() {
		
	}
	
	public void moveCursorToBeginningOfFile() {
		
	}
	
	public void moveCursorToEndOfFile() {
		
	}
	
	private int getOffsetWithinLine(int lineNumber, int offset, IDocument doc) throws BadLocationException {
		int sum = 0;
        for (int i = 0 ; i < lineNumber; i++) {
        	sum += doc.getLineLength(i);
        }
        return offset - sum;
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
//}
