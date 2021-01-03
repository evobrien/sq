package com.squarespace.interview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Convenience test utilities.
 */
public class TestUtils {

  /**
   * Provides an input stream for the given resource path.
   */
  public static InputStream loadTestResource(String path) throws IOException {
    InputStream stream;
    URL url = TestUtils.class.getClassLoader().getResource(path);
    if (null == url) {
      throw new FileNotFoundException("unable to locate path on resource: " + path);
    }
    try {
      stream = Files.newInputStream(Paths.get(url.toURI()));
    } catch (URISyntaxException e) {
      throw new FileNotFoundException("unable to adapt path name to URI: " + e.getMessage());
    }
    return stream;
  }
}
