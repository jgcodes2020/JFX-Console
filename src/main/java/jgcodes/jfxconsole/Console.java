package jgcodes.jfxconsole;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jgcodes.jfxconsole.resources.ResourceFinder;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.wellbehaved.event.InputHandler.Result;
import org.fxmisc.wellbehaved.event.InputMap;
import static org.fxmisc.wellbehaved.event.EventPattern.*;
import org.fxmisc.wellbehaved.event.Nodes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console extends InlineCssTextArea {
  public static final int START_OF_OUTPUT_CP = 0xFF100;
  public static final String START_OF_OUTPUT = new String(Character.toChars(Console.START_OF_OUTPUT_CP));
  public static final int END_OF_OUTPUT_CP = 0xFF101;
  public static final String END_OF_OUTPUT = new String(Character.toChars(Console.END_OF_OUTPUT_CP));
  public static final Pattern ESCAPE_CHARS = Pattern.compile("[\\x{FF100}\\x{FF101}]+");

  private StringBuffer inputBuffer = new StringBuffer();

  @Override
  public String getUserAgentStylesheet() {
    return ResourceFinder.getResource("console.css").toString();
  }

  public Console() {
    textProperty().addListener((observable, previous, current) -> {
      Matcher matcher = ESCAPE_CHARS.matcher(current);
      while (matcher.find()) {
        if (matcher.group().equals(END_OF_OUTPUT + START_OF_OUTPUT)) {
          super.deleteText(matcher.start(), matcher.end());
        }
        else {
          this.setStyle(matcher.start(), matcher.end(), "-fx-font-family: CChars");
        }
      }
    });
    Nodes.addInputMap(this, InputMap.process(anyOf(
      keyTyped(),
      keyPressed(code -> code.equals(KeyCode.BACK_SPACE) || code.equals(KeyCode.DELETE))
    ), (KeyEvent event) -> {
      if (event.getCharacter().equals(KeyEvent.CHAR_UNDEFINED)) {
        if (inputBuffer.length() > 0) {
          inputBuffer.deleteCharAt(inputBuffer.length() - 1);
        }
      }
      else {
        inputBuffer.append(event.getCharacter());
      }
      return Result.PROCEED;
    }));

  }

  /**
   * Tries to delete or replace text as if a user had tried to.
   * @param start the beginning of the range to replace
   * @param end the end of the range to replace
   * @param text the text said range is to be replaced with, use an empty string to delete text
   */
  @Override
  public void replaceText(int start, int end, String text) {
    final String
      deletedText = getText().substring(start, end);
    final int
      leftStartPos = getText().lastIndexOf(START_OF_OUTPUT_CP, start),
      leftEndPos = getText().lastIndexOf(END_OF_OUTPUT_CP, start);
    int
      rightStartPos = getText().indexOf(START_OF_OUTPUT_CP, end),
      rightEndPos = getText().indexOf(END_OF_OUTPUT_CP, end);

    if (rightStartPos < 0) rightStartPos = Integer.MAX_VALUE;
    if (rightEndPos < 0) rightEndPos = Integer.MAX_VALUE;

    if (leftStartPos <= leftEndPos && rightStartPos <= rightEndPos && !ESCAPE_CHARS.matcher(deletedText).find()) {
      super.replaceText(start, end, text);
    }
  }

  /**
   * This method is overridden to control input.
   * @inheritDoc
   */
  @Override
  public void insertText(int position, String text) {
    final int startLoc = this.getText().lastIndexOf(START_OF_OUTPUT_CP, position);
    final int endLoc = this.getText().lastIndexOf(END_OF_OUTPUT_CP, position);
    if (endLoc >= startLoc) super.insertText(position, text);
  }

  /**
   * Clears the console.
   */
  public void cls() {
    super.deleteText(0, this.getText().length());
  }

  /**
   * Writes some string of text to the console.
   * @param output the text to write to console.
   */
  public void writeOutput(String output) {
    String sanitized = ESCAPE_CHARS.matcher(output).replaceAll("");
    super.appendText(START_OF_OUTPUT + sanitized + END_OF_OUTPUT);
  }

  /**
   * The string buffer containing the currently inputted data.
   * @return the buffer containing input data for this console
   */
  public StringBuffer getInputBuffer() {
    return inputBuffer;
  }


}
