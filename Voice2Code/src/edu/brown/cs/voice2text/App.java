package edu.brown.cs.voice2text;

/**
 * Example Usage of Voice2Text
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        Voice2Text v2t = new Voice2Text();
		v2t.start();
        Thread.sleep(5000); // 20s
        v2t.stop();
        Thread.sleep(5000); // 10s
        v2t.start();
        Thread.sleep(20000); // 20s
        v2t.stop();
    }
}
