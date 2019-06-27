package io.github.hcoona;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LatexBuildTool {

  private static final Logger LOG = LoggerFactory.getLogger(LatexBuildTool.class);
  private static final ConcurrentHashMap<Path, String> FILE_CHECKSUM_MAP =
      new ConcurrentHashMap<>();

  FileSystem fileSystem;
  String rootPathStr;

  protected LatexBuildTool() {
  }

  public LatexBuildTool(FileSystem fileSystem, String rootPathStr) {
    this.fileSystem = fileSystem;
    this.rootPathStr = rootPathStr;
  }

  public void run() throws IOException, InterruptedException {
    Path rootPath = fileSystem.getPath(rootPathStr).toAbsolutePath();
    MDC.put("BaseDirectory", rootPath.toString());

    final List<Path> otherSources;
    try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath, "*.tex")) {
      otherSources = StreamSupport.stream(ds.spliterator(), false)
          .filter(path -> !path.getFileName().toString().equalsIgnoreCase("main.tex"))
          .map(Path::toAbsolutePath)
          .collect(Collectors.toList());
    }

    final Set<Path> sourceFiles;
    try {
      sourceFiles = Stream
          .concat(
              otherSources.parallelStream().flatMap(file -> {
                try {
                  return build(file);
                } catch (IOException e) {
                  throw new IOWrapperRuntimeException(e);
                } catch (InterruptedException ignored) {
                  Thread.currentThread().interrupt();
                  return Stream.empty();
                }
              }),
              build(fileSystem.getPath("main.tex").toAbsolutePath()))
          .collect(Collectors.toSet());
    } catch (IOWrapperRuntimeException e) {
      throw e.getCause();
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Source files: {}", sourceFiles);
    }
  }

  Stream<Path> build(Path source) throws IOException, InterruptedException {
    LOG.info("Start building {}", source);
    String depFilename = FilenameUtils.getFilenameWithoutExtension(source.getFileName().toString())
        + ".dep";
    String[] command = new String[] {
        "latexmk",
        "-cd",
        "-deps-out=\"" + depFilename + "\"",
        "-xelatex",
        "-latexoption=\"-shell-escape\"",
        source.toString()
    };
    if (LOG.isDebugEnabled()) {
      LOG.debug("Command: {}", Arrays.toString(command));
    }

    Process process = new ProcessBuilder()
        .command(command)
        .inheritIO()
        .start();
    if (process.waitFor(5, TimeUnit.MINUTES)) {
      if (process.exitValue() == 0) {
        LOG.info("Successfully to build {}", source);
        // TODO: parse dep file & collect the source files.
        return Stream.empty();
      } else {
        String message = "Failed to build " + source.toString()
            + ". Exit code " + process.exitValue();
        LOG.error(message);
        throw new IllegalStateException(message);
      }
    } else {
      process.destroyForcibly();
      String message = "Failed to build " + source.toString()
          + ". Timeout after 5 minutes";
      LOG.error(message);
      throw new IllegalStateException(message);
    }
  }
}
