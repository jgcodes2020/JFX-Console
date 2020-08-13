package jgcodes.jfxconsole;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * A class holding references to the default system I/O streams.
 */
public class SystemIO {
  public static final InputStream in = System.in;
  public static final PrintStream out = System.out;
  public static final PrintStream err = System.err;
}
