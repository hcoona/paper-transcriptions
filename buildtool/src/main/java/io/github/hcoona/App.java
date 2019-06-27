package io.github.hcoona;

import java.io.IOException;
import java.nio.file.FileSystems;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {
    LatexBuildTool buildTool = new LatexBuildTool(
        FileSystems.getDefault(),
        args[0]);
    buildTool.run();
  }
}
