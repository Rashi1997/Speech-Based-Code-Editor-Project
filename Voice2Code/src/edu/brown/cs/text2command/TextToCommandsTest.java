package test.java.edu.brown.cs;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import edu.brown.cs.text2code.TextToCommands;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Unit tests for text to commands
 */
public class TextToCommandsTest {

    TextToCommands textToCommands = new TextToCommands();


    @Test
    public void testFormatting() {
        TextToCommands t = new TextToCommands();
        List<String> s = new ArrayList<String>();

        assertEquals(t.getVariableString(s), "");
        assertEquals(t.getAllCapsString(s), "");
        assertEquals(t.getNormalCase(s), "");

        s.add("this");

        assertEquals(t.getVariableString(s), "this");
        assertEquals(t.getAllCapsString(s), "THIS");
        assertEquals(t.getNormalCase(s), "This");

        s.add("is");
        s.add("a");
        s.add("var");

        assertEquals(t.getVariableString(s), "thisIsAVar");
        assertEquals(t.getAllCapsString(s), "THIS_IS_A_VAR");
        assertEquals(t.getNormalCase(s), "ThisIsAVar");
    }

    @Test
    public void testCommandMatching() {
        TextToCommands t = new TextToCommands();
        List<String> s1 = new ArrayList<String>();
        List<String> s2 = new ArrayList<String>();
        s1.add("next");
        s1.add("line");
        s1.add("something");

        List<String> command = t.matchTerm(s1, t.commands);
        assertTrue(command != null);
        for (String s : command) {
            System.out.println(s);
        }
    }

    @Test
    public void testSentence() {
        TextToCommands t = new TextToCommands();
        List<String> s1 = new ArrayList<String>();
        List<String> s2 = new ArrayList<String>();
        s1.add("next");
        s1.add("line");
        s1.add("something");
        s1.add("less");
        s1.add("than");
        s1.add("declare");
        s1.add("var");
        s1.add("some");
        s1.add("variable");
        s1.add("end");
        s1.add("declare");
        s1.add("declare");
        s1.add("caps");
        s1.add("var");
        s1.add("all");
        s1.add("caps");
        s1.add("var");
        s1.add("end");
        s1.add("declare");
        s1.add("declare");
        s1.add("normal");
        s1.add("var");
        s1.add("third");
        s1.add("thing");
        s1.add("end");
        s1.add("declare");
        s1.add("other");
        s1.add("words");
        s1.add("from");
        s1.add("user");

        t.process(s1);
    }


    @Test
    public void getVariableString() {
        List<String> inputs = Arrays.asList("this", "is", "a", "var");
        String expectedOutput = "thisIsAVar";
        String output = textToCommands.getVariableString(inputs);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getAllCapsString() {
        List<String> inputs = Arrays.asList("this", "is", "a", "var");
        String expectedOutput = "THIS_IS_A_VAR";
        String output = textToCommands.getAllCapsString(inputs);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getNormalCase() {
        List<String> inputs = Arrays.asList("this", "is", "a", "var");
        String expectedOutput = "ThisIsAVar";
        String output = textToCommands.getNormalCase(inputs);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void getFirstNAsString() {

        List<String> inputs = Arrays.asList("this", "is", "a", "var");
        String expectedOutput = "this is a var";
        int inputNumberFirst = 4;
        String output = textToCommands.getFirstNAsString(inputNumberFirst, inputs);
        Assert.assertEquals(expectedOutput, output);

        int inputNumberSecond = 2;
        String expectedOutputSecond = "this is";
        output = textToCommands.getFirstNAsString(inputNumberSecond, inputs);
        Assert.assertEquals(expectedOutputSecond, output);
    }

    @Test
    public void getFirstN() {
        List<String> inputs = Arrays.asList("this", "is", "a", "var");
        int inputNumber = 3;
        List<String> expectedArray = Arrays.asList("this", "is", "a");
        List<String> outputArray = textToCommands.getFirstN(inputNumber, inputs);
        Assert.assertEquals(expectedArray.size(), outputArray.size());
        for (int index = 0; index < expectedArray.size(); index++) {
            Assert.assertEquals(expectedArray.get(index), outputArray.get(index));
        }
    }

    @Test
    public void getLargestInSet() {
        Set<String> inputs = new HashSet<String>() {{
            add("t h i s");
            add("i s");
            add("a");
            add("v ar");
        }};
        int expected = 4;
        int actual = textToCommands.getLargestInSet(inputs);
        Assert.assertEquals(expected, actual);
    }

    private <T> void assertArrays(List<T> firstInput, List<T> secondInput) {
        Assert.assertEquals(firstInput.size(), secondInput.size());
        for (int index = 0; index < firstInput.size(); index++) {
            Assert.assertEquals(firstInput.get(index), secondInput.get(index));
        }
    }

    @Test
    public void matchTerm() {

        String variable = "this";
        String variableAnother = "is";

        Set<String> inputTerms = new HashSet<String>() {{
            add("this is");
            add("cc");
            add("var");
        }};
        List<String> inputs = Arrays.asList(variable, variableAnother, "aaa", "bbbb");
        List<String> expected = Arrays.asList(variable, variableAnother);
        List<String> actual = textToCommands.matchTerm(inputs, inputTerms);
        Assert.assertNotNull(actual);
        assertArrays(expected, actual);

    }


    @Test
    public void listToString() {
        List<String> inputs = Arrays.asList("this", "is", "a", "test");
        String expected = "this is a test";
        String actual = textToCommands.listToString(inputs);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeFirstN() {
        List<String> inputs = new ArrayList<>();
        inputs.add("this");
        inputs.add("is");
        inputs.add("a");
        inputs.add("variable");
        int removeIndex = 1;
        int originSize = inputs.size();
        textToCommands.removeFirstN(removeIndex, inputs);
        Assert.assertEquals(inputs.size(), originSize - removeIndex);
    }

    @Test
    public void checkForCommand() {
        List<String> correctCommand = new LinkedList<>();
        correctCommand.add("next");
        correctCommand.add("line");
        correctCommand.add("up");
        boolean result = textToCommands.checkForCommand(correctCommand);
        Assert.assertTrue(result);

        correctCommand.clear();
        correctCommand.add("upp");
        correctCommand.add("downmmm");
        result = textToCommands.checkForCommand(correctCommand);
        Assert.assertFalse(result);


    }

    @Test
    public void checkForKeyWords() {
        List<String> correctWords = new LinkedList<>();
        correctWords.add("open");
        correctWords.add("parenthesis");
        boolean actual = textToCommands.checkForKeyWords(correctWords);
        Assert.assertTrue(actual);

        List<String> wrongWords = new LinkedList<>();
        wrongWords.add("test");
        wrongWords.add("unknown");
        actual = textToCommands.checkForKeyWords(wrongWords);
        Assert.assertFalse(actual);
    }

    @Test
    public void buildVariable() {
        List<String> correctWords = new LinkedList<>();
        String msg = "this is a var end declare something else";
        String[] words = msg.split(" ");
        for (String word : words
        ) {
            correctWords.add(word);
        }
        List<String> expected = Arrays.asList("this", "is", "a", "var");
        List<String> actual = textToCommands.buildVariable(correctWords);
        assertArrays(expected, actual);
    }

    @Test
    public void checkForDeclarations() {
        List<String> correctWords = new LinkedList<>();
        correctWords.add("declare");
        correctWords.add("var");
        correctWords.add("variable a");
        correctWords.add("end");
        correctWords.add("declare");
        Boolean result = textToCommands.checkForDeclarations(correctWords);
        Assert.assertTrue(result);
        correctWords.add("test");
        result = textToCommands.checkForDeclarations(correctWords);
        Assert.assertFalse(result);
    }

}
