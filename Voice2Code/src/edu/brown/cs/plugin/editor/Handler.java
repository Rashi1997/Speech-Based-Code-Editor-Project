package edu.brown.cs.plugin.editor;

import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import edu.brown.cs.voice2text.ResponseObserverClass;

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
	
	public void moveCursorRight() {
		
	}
	
	public void moveCursorLeft() {
		
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
