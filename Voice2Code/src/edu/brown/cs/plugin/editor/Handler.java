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
				System.out.println("Inserting text: " + text);
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

					document.replace(offset, 0, text);
					styledText.setSelection(styledText.getCharCount());
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
	
	public void moveCursorOneCharRight() {
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
				int newOffset = Integer.min(styledText.getCharCount(), offset);
	            styledText.setCaretOffset(newOffset); // TODO: not sure why don't need +1
			}
		});
	}
	
	// Moves left one word
	public void moveCursorOneCharLeft() {
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
				int newOffset = Integer.max(0, offset - 2);
	            styledText.setCaretOffset(newOffset); // TODO: not sure why need -2
			}
		});
	}
	
	public void moveCursorToNextLine() {
		
	}
	
	public void moveCursorToEndOfLine() {
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
				
				System.out.println("offset: " + offset);
				int lineNumber = styledText.getLineAtOffset(offset);
				System.out.println("Line number: " + lineNumber);
				int lineEndOffset = styledText.getOffsetAtLine(lineNumber) + styledText.getLine(lineNumber).length();
				styledText.setCaretOffset(lineEndOffset);
				System.out.println("Set caret offset to end of line");
			}
			
		});
	}
	
	public void moveCursorToStartOfLine() {
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
				
				System.out.println("offset: " + offset);
				int lineNumber = styledText.getLineAtOffset(offset);
				System.out.println("Line number: " + lineNumber);
				styledText.setCaretOffset(styledText.getOffsetAtLine(lineNumber));
				System.out.println("Set caret offset to start of line");
			}
			
		});
	}
	
	public void moveCursorToBeginningOfFile() {
		
	}
	
	public void moveCursorToEndOfFile() {
		
	}
	
	
	public void moveOneWordRight() {
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
				
				styledText.setSelection(styledText.getCharCount());
				int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
		        currentLine = Integer.max(0, currentLine - 1);
		        int lineOffset;
				try {
					lineOffset = getOffsetWithinLine(currentLine, offset, document);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				String textAtLine = styledText.getLine(currentLine);
				String textAfter = textAtLine.substring(lineOffset - 1);// TODO: not sure why -1
				System.out.println(moveForward(textAfter));
		        styledText.setCaretOffset(offset + moveForward(textAfter) - 1); // Not sure why - 1
			}
			
		});
	
	}
	
	public void moveOneWordLeft() {
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
				
				styledText.setSelection(styledText.getCharCount());
				int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
		        currentLine = Integer.max(0, currentLine - 1);
		        int lineOffset;
				try {
					lineOffset = getOffsetWithinLine(currentLine, offset, document);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				String textAtLine = styledText.getLine(currentLine);
				String textBefore = textAtLine.substring(0, lineOffset - 1); // TODO: bugging without -1
				System.out.println("BEFORE: " + textBefore);
				System.out.println(moveBack(textBefore));
				System.out.println(offset);
				System.out.println(offset + moveBack(textBefore));
		        styledText.setCaretOffset(offset + moveBack(textBefore) -1); // TODO: bugging without -1
			}
			
		});
	}
		
		public void goToLine(int lineNum) {
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
					
					styledText.setSelection(styledText.getCharCount());
					int num = lineNum - 1; // User will say what they want based on 1-indexing
				    num = Integer.min(num, styledText.getLineCount());
					System.out.println(styledText.getLineCount());
					num = Integer.max(num, 0);
		            int lineOffset;
					try {
						lineOffset = getOffsetOfLine(num, document);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
		            styledText.setCaretOffset(lineOffset);
				
			}});
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
//}
