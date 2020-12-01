package edu.brown.cs.voice2text;

public final class AudioFormatConfiguration {

    private AudioFormatConfiguration() {
            // restrict instantiation
    }

    public static final int sampleRate = 16000;
    public static final int sampleSizeInBits = 16;
    public static final int numberOfChannels = 1;
    public static final boolean signed = true;
    public static final boolean bigEndian = false;
}