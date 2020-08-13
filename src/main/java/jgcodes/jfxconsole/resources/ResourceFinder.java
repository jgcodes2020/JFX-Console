package jgcodes.jfxconsole.resources;

import java.io.InputStream;
import java.net.URL;

public class ResourceFinder {
  public static URL getResource(String path) {
    return Thread.currentThread().getContextClassLoader().getResource(path);
  }
  public static InputStream getResourceStream(String path) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
  }
}
