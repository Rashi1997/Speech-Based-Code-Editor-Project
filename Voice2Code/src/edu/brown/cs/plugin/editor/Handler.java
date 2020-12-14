package edu.brown.cs.plugin.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class Handler {
	public Handler() {
	}

	public void insertText(String text) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				System.out.println("Inserting text: " + text);
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();

				FileEditorInput iei = ((FileEditorInput) editor.getEditorInput());
				iei.getFile().getLocation().toFile();

				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				int offset = styledText.getCaretOffset();
				try {

					document.replace(offset, 0, text);
					styledText.setSelection(styledText.getCharCount());
				} catch (Exception e) {
					e.printStackTrace();
				}

				styledText.setCaretOffset(offset + text.length());

			}

		});
	}

	public void moveCursorUp() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
//				int newLoc = styledText.getOffsetAtLine(currentLine - 1);
				int newLoc;
				try {
					newLoc = getOffsetOfLine(currentLine - 1, document);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				styledText.setCaretOffset(newLoc);
				System.out.println("Moved cursor up to line " + styledText.getLineAtOffset(newLoc));
			}
		});
	}

	public void moveCursorDown() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				int currentLine = styledText.getLineAtOffset(styledText.getCaretOffset());
//				int newLoc = styledText.getOffsetAtLine(currentLine + 1);
				int newLoc = styledText.getOffsetAtLine(currentLine + 1);
				try {
					newLoc = getOffsetOfLine(currentLine + 1, document);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				styledText.setCaretOffset(newLoc);
				System.out.println("Moved cursor down to line " + styledText.getLineAtOffset(newLoc));
			}
		});
	}

	public void moveCursorOneCharRight() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
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
				ITextEditor editor = (ITextEditor) part;
				//editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				System.out.println("text");
				styledText.print();
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
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
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
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
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
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
//				int offset = styledText.getCaretOffset();
//				
//				System.out.println("offset: " + offset);
//				int lineNumber = styledText.getLineAtOffset(offset);
//				System.out.println("Line number: " + lineNumber);
				styledText.setCaretOffset(styledText.getOffsetAtLine(0));
				System.out.println("Set caret offset to beginning of file");
			}

		});
	}

	public void moveCursorToEndOfFile() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
//				int offset = styledText.getCaretOffset();
//				
//				System.out.println("offset: " + offset);
//				int lineNumber = styledText.getLineAtOffset(offset);
//				System.out.println("Line number: " + lineNumber);
				styledText.setCaretOffset(styledText.getCharCount());
				System.out.println("Set caret offset to end of file");
			}

		});
	}

	public void compile() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				File launchableFile = null;
				boolean autoStartNewlyCreatedConfiguration = true;

				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();

				FileEditorInput iei = ((FileEditorInput) editor.getEditorInput());
				launchableFile = iei.getFile().getLocation().toFile();

				if (launchableFile != null && launchableFile.exists()) {
					ILaunchConfiguration configuration;
					try {
						configuration = createNewLaunchConfiguration(launchableFile);
						Display.getDefault().asyncExec(() -> {
							if (autoStartNewlyCreatedConfiguration) {
								DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
							} else {
								if (DebugUIPlugin.openLaunchConfigurationEditDialog(
										Display.getCurrent().getActiveShell(), configuration, DebugUITools
												.getLaunchGroup(configuration, ILaunchManager.RUN_MODE).getIdentifier(),
										null, true) == IDialogConstants.OK_ID) {
									DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
								}
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private ILaunchConfiguration createNewLaunchConfiguration(File file) throws CoreException {

		ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] lct = mgr.getLaunchConfigurations();
		return lct[0];
	}
	

	public void rename(String target, String replace_with) {
		System.out.println("Target: " + target);
		System.out.println("Replace with: " + replace_with);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
					IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor)part;
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());
	
					try {
	
					final FindReplaceDocumentAdapter finder = new FindReplaceDocumentAdapter(document);
						IRegion region;
							int i = 0;
							region = finder.find(0, target, true, true, false, false);
							while (region != null) {
									i = i + region.getLength();
									finder.replace(replace_with, false);
									region = finder.find(i, target, true, true, false, false);
							}
					} catch (final BadLocationException e1) {
							// Just ignore them
					}
			}
	
		});
	}


	public void format() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
			  	IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			  	IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor)part;
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());
				// take default Eclipse formatting options
				@SuppressWarnings("unchecked")
				Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();

				// initialize the compiler settings to be able to format 1.5 code
				options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
				options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
				options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);

				// change the option to wrap each enum constant on a new line
				options.put(
					DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
					DefaultCodeFormatterConstants.createAlignmentValue(
					true,
					DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
					DefaultCodeFormatterConstants.INDENT_ON_COLUMN));

				// instantiate the default code formatter with the given options
				final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
				final TextEdit textEdit =
				        codeFormatter.format(
				            CodeFormatter.K_COMPILATION_UNIT,
				            document.get(),
				            0,
				            document.get().length(),
				            0,
				            System.getProperty("line.separator"));
				if (textEdit != null) {
				      try {
						textEdit.apply(document);
					} catch (MalformedTreeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    } else {
				    }
			}
	
		});
	}
	
	public void moveOneWordRight() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				//editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());
			

					
				IFile ifile = (IFile) part.getEditorInput().getAdapter(IFile.class);
				File file = ifile.getLocation().toFile();
				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				
				//styledText.getLineAtOffset(offset);
				int offset = styledText.getCaretOffset();
				int currentLine = styledText.getLineAtOffset(offset);
		        currentLine = Integer.max(0, currentLine - 1);
				try {
				InputStream inputstream = new FileInputStream(file);
				
					int data = 0;
					int c = 0;
					// Read up to current
					while(data != -1 && c < offset - 1) {
					
					  data = inputstream.read();
					  System.out.println((char)data);
					  c++;
					}
					c = 0;
					// Keep going until find non alpha-numeric char
					while (data != -1) {
						data = inputstream.read();
						char dataChar = (char) data;
						if (Character.isDigit(dataChar) || Character.isLetter(dataChar)) {
							c++;
						} else {
							break;
						}
					}
					// Keep going until find alpha-numeric char
					while (data != -1) {
						data = inputstream.read();
						char dataChar = (char) data;
						if (!(Character.isDigit(dataChar) || Character.isLetter(dataChar))) {
							c++;
						} else {
							break;
						}
					}
					
					inputstream.close();
			        styledText.setCaretOffset(offset + c);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
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
				ITextEditor editor = (ITextEditor) part;
				//editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());
			

					
				IFile ifile = (IFile) part.getEditorInput().getAdapter(IFile.class);
				File file = ifile.getLocation().toFile();
				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				
				int offset = styledText.getCaretOffset();
				System.out.println(offset);
				int currentLine = styledText.getLineAtOffset(offset);
		        currentLine = Integer.max(0, currentLine - 1);
		        System.out.println(currentLine);
		        
				System.out.println(file.getName());
				try {
				InputStream inputstream = new FileInputStream(file);
				
					int data = 0;
					int c = 0;
					// p and p2 used to track last alphanumeric char before the one at offset
					int p = 0;
					int p2 = 0;
					// Read up to current
					while(data != -1 && c < offset - 1) {
					
					  data = inputstream.read();
					  char dataChar = (char) data;
					  if (Character.isDigit(dataChar) || Character.isLetter(dataChar)) {
						    p2 = c + 1;
						} else {
							// Wait to set p until hit non alphanumeric
							p = p2;
						}
					 
					  c++;
					}
					
					inputstream.close();
			        styledText.setCaretOffset(Integer.min(p, styledText.getCharCount()-1));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
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
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
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
					styledText.setCaretOffset(lineOffset);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				

			}
		});
	}

	public void createProject(String projectName) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IWorkspaceRoot root = workspace.getRoot();
				IProject project = root.getProject(projectName);
				try {
					project.create(new NullProgressMonitor());
					project.open(null);

					// set to Java nature
					IProjectDescription desc = project.getDescription();
					desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
					project.setDescription(desc, new NullProgressMonitor());

					// convert to Java Project
					IJavaProject javaProj = JavaCore.create(project);

					// src
					IFolder src = project.getFolder("src");
					src.create(true, true, null);
					IClasspathEntry srcEntry = JavaCore.newSourceEntry(src.getFullPath());

					// bin
					IFolder bin = project.getFolder("bin");
					bin.create(true, true, null);
					IPath binPath = bin.getFullPath();
					javaProj.setOutputLocation(binPath, null);

					// set build path
					IClasspathEntry[] jcp = org.eclipse.jdt.ui.PreferenceConstants.getDefaultJRELibrary();
					javaProj.setRawClasspath(new IClasspathEntry[] { srcEntry, jcp[0] }, new NullProgressMonitor());
					
					// create app.java
					IPackageFragment pack = javaProj.getPackageFragmentRoot(src).createPackageFragment("mypackage", false, null);
					StringBuffer buffer = new StringBuffer();
					buffer.append("package " + pack.getElementName() + ";\n");
					buffer.append("\n");
					String source = "public class App {\n\n}";
					buffer.append(source);
					ICompilationUnit cu = pack.createCompilationUnit("App.java", buffer.toString(), false, null);
					
					// expand src folder
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IViewPart viewPart = workbenchPage.findView("org.eclipse.ui.navigator.ProjectExplorer");
					((ISetSelectionTarget) viewPart).selectReveal(new StructuredSelection(cu));
					
					
					// open in texteditor
					// close current
					workbenchPage.closeAllEditors(true);
					
					// open App.java
					IFile file = (IFile) cu.getUnderlyingResource();
					IDE.openEditor(workbenchPage, file);
					
					// set focus to editor
					workbenchPage.getActiveEditor().setFocus();
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void deleteLeftCharacter() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage workbenchPage = iw.getActivePage();
				IEditorPart part = workbenchPage.getActiveEditor();
				ITextEditor editor = (ITextEditor) part;
				editor.setFocus();
				IDocumentProvider dp = editor.getDocumentProvider();
				IDocument document = dp.getDocument(editor.getEditorInput());

				Control control = editor.getAdapter(Control.class);
				StyledText styledText = (StyledText) control;
				int offset = styledText.getCaretOffset() - 1;
				
				try {
					document.replace(offset, 1, "");
					styledText.setCaretOffset(offset);
				} catch (BadLocationException e) {
					e.printStackTrace();
					return;
				}
				
			}
		});
	}
	
	public void deleteCurrentWord() {
		
	}
	
	public void undo() {
		
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
			IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbench workbench = iw.getWorkbench();
			IWorkbenchPage workbenchPage = iw.getActivePage();
			IEditorPart part = workbenchPage.getActiveEditor();
			ITextEditor editor = (ITextEditor) part;
			editor.setFocus();
			IDocumentProvider dp = editor.getDocumentProvider();
			IDocument document = dp.getDocument(editor.getEditorInput());
	
			Control control = editor.getAdapter(Control.class);
			IOperationHistory operationHistory = workbench.getOperationSupport().getOperationHistory();
			IUndoContext undoContext = workbench.getOperationSupport().getUndoContext();
			if (operationHistory.canUndo(undoContext)) {
				try {
					IStatus status = operationHistory.undo(undoContext, null, null);
					if (status.isOK()) {
						System.out.println("Successfully undid last operation");
						return;
					} else {
						System.out.println("Undo went wrong! " + status.getMessage());
						return;
					}
				} catch (ExecutionException e) {
					// handle the exception 
					e.printStackTrace();
					return;
				}
			} else {
				System.out.println("Nothing to undo in current context!");
			}
		}
		});
	}
	
	public void redo() {
		
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
			IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbench workbench = iw.getWorkbench();
			IWorkbenchPage workbenchPage = iw.getActivePage();
			IEditorPart part = workbenchPage.getActiveEditor();
			ITextEditor editor = (ITextEditor) part;
			editor.setFocus();
			IDocumentProvider dp = editor.getDocumentProvider();
			IDocument document = dp.getDocument(editor.getEditorInput());
	
			Control control = editor.getAdapter(Control.class);
			IOperationHistory operationHistory = workbench.getOperationSupport().getOperationHistory();
			IUndoContext undoContext = workbench.getOperationSupport().getUndoContext();
			if (operationHistory.canRedo(undoContext)) {
				try {
					IStatus status = operationHistory.redo(undoContext, null, null);
					if (status.isOK()) {
						System.out.println("Successfully undid last operation");
						return;
					} else {
						System.out.println("Undo went wrong! " + status.getMessage());
						return;
					}
				} catch (ExecutionException e) {
					// handle the exception 
					e.printStackTrace();
					return;
				}
			} else {
				System.out.println("Nothing to redo in current context!");
			}
		}
		});
	}
	public void createFile(String fileName) {
	}

	private int getOffsetWithinLine(int lineNumber, int offset, IDocument doc) throws BadLocationException {
		int sum = 0;
		for (int i = 0; i < lineNumber; i++) {
			sum += doc.getLineLength(i);
		}
		return offset - sum;
	}

	private int getOffsetOfLine(int lineNumber, IDocument doc) throws BadLocationException {
		int sum = 0;
		for (int i = 0; i < lineNumber; i++) {
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
	
	public void moveCursorToPosition(int line, int column) {
		
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if (editor instanceof ITextEditor) {
			ITextEditor textEditor =  ((ITextEditor) editor);
			IDocumentProvider dp = textEditor.getDocumentProvider();
			IDocument document = dp.getDocument(editor.getEditorInput());
			int offset = column;
			for (int curLine = 0; curLine < line; curLine++) {
				try {
					offset+=document.getLineLength(curLine);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}

			ISelectionProvider selectionProvider = textEditor.getSelectionProvider();
			selectionProvider.setSelection(new TextSelection(offset, 0));
			ITextSelection selection = (ITextSelection)selectionProvider.getSelection();
			System.out.println(selection.getOffset());
		}
		
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
