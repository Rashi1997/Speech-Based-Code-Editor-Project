package edu.brown.cs.v2cplugin;

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
						document.replace(offset, 0, text.replace("down", ";"));
						styledText.setSelection(styledText.getCharCount());
						int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
			            String textAtLine = styledText.getLine(currentLine);
			            //int spaces = getLeadingSpaces(textAtLine);
			            styledText.insert("\n");
			            styledText.setCaretOffset(styledText.getCaretOffset());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				styledText.setCaretOffset(offset + text.length() + 1);
				
				//Try while loop
			}
			
		});
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
