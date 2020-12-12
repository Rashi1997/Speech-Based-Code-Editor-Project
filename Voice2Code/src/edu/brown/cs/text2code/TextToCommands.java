package edu.brown.cs.text2code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import edu.brown.cs.plugin.editor.Handler;

public class TextToCommands {


	// Store key words
	Map<String, String> keywords = new HashMap<String, String>();
	Set<String> commands = new HashSet<String>();
	Map<String, Runnable> commandsToFuncs = new HashMap<String, Runnable>();
	Set<String> termDeclarators = new HashSet<String>();
	int maxCommandLength;
	
	Handler editorHandler;


	public TextToCommands() {
		initializeKeyWords();
		initializeCommands();
		intiliazeDeclarators();
		
		editorHandler = new Handler();
	}

	/**
	 * Initializes key words to look for.
	 * If detected, these words will be translated into their
	 * corresponding symbols and added where the user's cursor is.
	 * (Additional things can be done here)
	 */
	private void initializeKeyWords() {
		keywords.put("space", " ");
		keywords.put("open parenthesis", "(");
		keywords.put("close parenthesis", ")");
		keywords.put("equals", "=");
		keywords.put("plus", "+");
		keywords.put("minus", "-");
		keywords.put("divide", "/");
		keywords.put("multiply", "*");
		keywords.put("mod", "%");
		keywords.put("colon", ":");
		keywords.put("semicolon", ";");
		keywords.put("tab", "	");
		keywords.put("quote", "'");
		keywords.put("double", "\"");
		keywords.put("comment", "//");
		keywords.put("less than", "<");
		keywords.put("greater than", ">");
		keywords.put("integer", "int");
		keywords.put("string", "String");
		keywords.put("print", "System.out.println(");
		keywords.put("main function", "public static void main(String[] args) {");
		
	}

  /**
	  Initialize a list of commands
		(can be updated/changed later)
	*/
	private void initializeCommands() {
		// TODO: add more, just testing with a few for now
		commands.add("up");
		commands.add("down");
		commands.add("end line");
		commands.add("next line");
		commands.add("start line");
		commands.add("end file");
		commands.add("start file");
		commands.add("right");
		commands.add("left");
		commands.add("declare var");
		commands.add("forward word");
		commands.add("backward word");
		commands.add("compile");
	}

  /*
	 Commands to declare terms, can be ended with 'end declare'
	 ex: "declare var  this is a var end declare"

	*/
	private void intiliazeDeclarators() {

		termDeclarators.add("declare var");
		termDeclarators.add("declare normal var");
		termDeclarators.add("declare caps var");
	}

	/**
	 *
	 * @param s, in the form "this", "is", "a", "var"
	 * @return	the input list in form "thisIsAVar"
	 */
	public String getVariableString(List<String> s) {
		if (s.size() == 0) {
			return "";
		}

		String result = "";

		result = result.concat(s.get(0).toLowerCase());
		for (int i = 1; i < s.size(); i++) {
			String current = s.get(i).toLowerCase();
			current  = current.substring(0, 1).toUpperCase() + current.substring(1, current.length());
			result = result.concat(current);
		}

		return result;
	}


	/**
	 * @param s, in the form "this", "is", "a", "var"
	 * @return the  input list in the form "THIS_IS_A_VAR"
	 */
	public String getAllCapsString(List<String> s) {

		String result = "";
		for (int i = 0; i < s.size(); i++) {
			result = result.concat(s.get(i).toUpperCase());
			if (i != s.size() - 1) {
				result = result.concat("_");
			}
		}
		return result;
	}


	/**
	* @param s: input list in form "this", "is", "a", "var"
	* @return: the input in the form "ThisIsAVar"
	*/
	public String getNormalCase(List<String> s) {
		String result = "";
		for (int i = 0; i < s.size(); i++) {
			String current = s.get(i).toLowerCase();
			current  = current.substring(0, 1).toUpperCase() + current.substring(1, current.length());
			result = result.concat(current);
		}

		return result;
	}

	public String getFirstNAsString(int n, List<String> s) {
		assert(n <= s.size());
		String result = "";
		for (int j = 0; j < n; j++) {
			result += s.get(j);
			if (j != n - 1) {
				result += " ";
			}
		}
		return result;
	}

	public List<String> getFirstN(int n, List<String> s) {
		assert(n <= s.size());
		List<String> result = new ArrayList<String>();
		for (int j = 0; j < n; j++) {
			result.add(s.get(j));
		}
		return result;
	}

	public int getLargestInSet(Set<String> terms) {
		int maxLength = -1;
		for (String s : terms) {
			int size = s.split(" ").length;
			if (size > maxLength) {
				maxLength = size;
			}
		}
		return maxLength;
	}

  /* Match terms with the beginning of s */
	public List<String> matchTerm(List<String> s, Set<String> terms) {
		int largestTermLength = Integer.min(getLargestInSet(terms), s.size());
		for (int i = largestTermLength; i >= 1; i--) {
			String currString = getFirstNAsString(i, s);
			List<String> current = getFirstN(i, s);
			// Check for match
			if (terms.contains(currString)) {
				System.out.println("Matched with: " + currString);
				return current;
			}
		}
		return null; // no match
	}


  // Very simple processor, just writes all the words to the screen
	// no commands
	public void simpleProcess(List<String> words) {
		for (int i = 0; i < words.size(); i++) {
			// TODO: Plugin.writeout(words.get(i))
			System.out.println(words.get(i));
		}
	}

	public String listToString(List<String> s) {
		String res = "";
		for (int i = 0; i < s.size(); i++) {
			res += s.get(i);
			if (i != s.size() - 1) {
				res += " ";
			}
		}
		return res;
	}


	public void removeFirstN(int n, List<String> words) {
		assert(n <= words.size());
		for (int i = 0; i < n; i++) {
			words.remove(0);
		}
	}
	
	public boolean checkForCommand(List<String> words) {
		// iterate over the words, matching for commands
		List<String> command = matchTerm(words, commands);

		if (command != null) {
			String commandString = getFirstNAsString(command.size(), words);
			removeFirstN(command.size(), words);
			switch (commandString) {
				case "up" :
					// Move cursor up one line
					editorHandler.moveCursorUp();
					break;
				case "down":
					// Move cursor down one line
					editorHandler.moveCursorDown();
					break;
				case "end line":
					// Move cursor to end of line
					editorHandler.moveCursorToEndOfLine();
					break;
				case "next line" :
					// Move cursor up one line
					editorHandler.moveCursorToNextLine();
					break;
				case "start line":
					// Move cursor to start of line
					editorHandler.moveCursorToStartOfLine();
					break;
				case "end file":
					// Move Cursor to end of file
					editorHandler.moveCursorToEndOfFile();
					break;
				case "start file":
					// Move cursor to start of file
					editorHandler.moveCursorToBeginningOfFile();
					break;
				case "right":
					// Move cursor one word right
					editorHandler.moveCursorRight();
					break;
				case "left":
					// Move cursor one word left
					editorHandler.moveCursorLeft();
					break;
				case "compile":
					editorHandler.compile();
					break;
				case "declare var":
					// Concatenation methods
					// ex: default var declaration
					System.out.println("Declaring var");
					break;
				default:
					System.out.println("no match");
					break;
			}
			return true;
		} else {
			return false;
		}
	}



	public boolean checkForKeyWords(List<String> words) {
		// iterate over the words, matching for commands
		List<String> keyword = matchTerm(words, keywords.keySet());

		if (keyword != null) {
			String keywordString = getFirstNAsString(keyword.size(), words);
			removeFirstN(keyword.size(), words);
			// TODO: call a function to write this to screen
			editorHandler.insertText(keywords.get(keywordString));
			return true;
		} else {
			return false;
		}
	}

	// get the words that will make up a variable (assumes a declarator has been used)
	// ex input: this is a var end declare something else
	//    output: 'this', 'is', 'a', 'var'
	public List<String> buildVariable(List<String> words) {
		List<String> result = new ArrayList<String>();
		int size = words.size();
		if (words.size() < 2) {
			throw new InvalidCommandException("A declaration was not followed by 'end declare'");
		}
		while(words.size() >= 2) {
			if (words.get(0).equals("end") && words.get(1).equals("declare")) {
				removeFirstN(2, words);
				return result;
			} else {
				result.add(words.get(0));
				removeFirstN(1, words);
			}
		}
		throw new InvalidCommandException("A declaration was not followed by 'end declare'");
	}

	public boolean checkForDeclarations(List<String> words) {
		List<String> declarator = matchTerm(words, termDeclarators);

		if (declarator != null) {

			String declaratorString = getFirstNAsString(declarator.size(), words);
			removeFirstN(declarator.size(), words);
			List<String> var = buildVariable(words);
			switch (declaratorString) {
				case "declare var":
					editorHandler.insertText(getVariableString(var));
					break;
				case "declare normal var":
					editorHandler.insertText(getNormalCase(var));
					break;
				case "declare caps var":
					editorHandler.insertText(getAllCapsString(var));
					break;
				default:
					break;
			}

			return true;
		} else {
			return false;
		}
	}

	public void printList(List<String> words) {
		for (int i = 0 ; i < words.size(); i ++) {
			System.out.print(words.get(i) + " ");
		}
		System.out.println();
	}

	// Process the current words into commands
	// Called at end of sentence
	public void process(List<String> words) {
		System.out.println("Processing: ");

		while (words.size() > 0) {
			//printList(words);
			if (checkForCommand(words)) {
				continue;
			} else if (checkForKeyWords(words)) {
				continue;
			}  else if(checkForDeclarations(words)) {
				continue;
			} else {
				// regular word
				editorHandler.insertText(words.get(0));
				removeFirstN(1, words);
			}
		}
	}





}
