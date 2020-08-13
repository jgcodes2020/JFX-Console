package jgcodes.jfxconsole.io;

import jgcodes.jfxconsole.Console;

import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ConsoleWriter extends Writer {
  private final WeakReference<Console> consoleRef;
  private boolean closed = false;

  public ConsoleWriter(Console console) {
    this.consoleRef = new WeakReference<>(console);
  }

  private void checkOpen() throws IOException {
    if (closed) throw new IOException("Writer was already closed");
  }

  @Override
  public void write(int c) throws IOException {
    checkOpen();
    int charToWrite = (char) c & 0xFFFF;
    consoleRef.get().writeOutput(Character.toString(charToWrite));
  }

  @Override
  public void write(String str) throws IOException {
    checkOpen();
    consoleRef.get().writeOutput(str);
  }

  @Override
  public void write(char[] chars) throws IOException {
    checkOpen();
    consoleRef.get().writeOutput(new String(chars));
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    checkOpen();
    String charsToWrite = str.substring(off, off + len);
    consoleRef.get().writeOutput(charsToWrite);
  }

  @Override
  public void write(char[] chars, int off, int len) throws IOException {
    checkOpen();
    char[] charsToWrite = Arrays.copyOfRange(chars, off, off + len);
    consoleRef.get().writeOutput(new String(charsToWrite));
  }

  @Override
  public void flush() throws IOException {
    checkOpen();
  }

  @Override
  public void close() throws IOException {
    checkOpen();
    closed = true;
  }
}
