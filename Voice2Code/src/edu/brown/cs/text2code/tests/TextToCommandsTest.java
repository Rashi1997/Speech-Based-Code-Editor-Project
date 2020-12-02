package edu.brown.cs.text2code.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.brown.cs.text2code.TextToCommands;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for text to commands
 */
public class TextToCommandsTest
{

    @Test
    public void testFormatting()
    {
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
    public void testCommandMatching()
    {
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

}