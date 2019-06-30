package io.github.hcoona;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class LatexmkDepParser {

  FileSystem fileSystem;
  String depFilePathStr;

  protected LatexmkDepParser() {
  }

  public LatexmkDepParser(FileSystem fileSystem, String depFilePathStr) {
    this.fileSystem = fileSystem;
    this.depFilePathStr = depFilePathStr;
  }

  public Map<Path, Set<Path>> parse() throws IOException {
    final Map<Path, Set<Path>> result = new LinkedHashMap<>();
    final Path depFilePath = fileSystem.getPath(depFilePathStr);

    Path currentTarget = null;
    Set<Path> currentDependents = null;
    try (BufferedReader bufferedReader = Files.newBufferedReader(depFilePath)) {
      while (true) {
        final String line = bufferedReader.readLine();
        if (line == null) {
          if (currentTarget != null) {
            result.put(currentTarget, currentDependents);
          }
          break;
        }

        if (line.startsWith("#")) {
          continue;
        }

        if (line.startsWith("    ") || line.startsWith("\t")) {
          assert currentDependents != null;
          currentDependents.add(
              depFilePath.resolveSibling(
                  StringUtils.stripEnd(
                      StringUtils.stripStart(line, " \t"),
                      "\\")));
        } else {
          if (currentTarget != null) {
            result.put(currentTarget, currentDependents);
          }

          currentTarget = depFilePath.resolveSibling(
              StringUtils.stripEnd(line, " :\\"));
          currentDependents = new LinkedHashSet<>();
        }
      }
    }

    return result;
  }
}
