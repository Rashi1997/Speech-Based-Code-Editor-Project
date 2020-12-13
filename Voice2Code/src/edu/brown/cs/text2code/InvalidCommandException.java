package edu.brown.cs.text2code;

public class InvalidCommandException extends IllegalArgumentException {

  public InvalidCommandException(String errorMessage) {
    super(errorMessage);
  }
}
