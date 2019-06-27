package io.github.hcoona;

public final class FilenameUtils {

  private FilenameUtils() {
  }

  public static String getFilenameWithoutExtension(String filename) {
    int pos = filename.lastIndexOf('.');
    if (pos == -1) {
      return filename;
    } else {
      return filename.substring(0, pos);
    }
  }
}
