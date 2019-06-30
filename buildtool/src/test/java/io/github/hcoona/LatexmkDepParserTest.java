package io.github.hcoona;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LatexmkDepParserTest {

  @Test
  void testParse() throws IOException {
    try (FileSystem jimfs = Jimfs.newFileSystem(Configuration.unix())) {
      Files.copy(
          Objects.requireNonNull(
              LatexmkDepParser.class.getClassLoader().getResourceAsStream("fig-1-1.dep")),
          jimfs.getPath("/fig-1-1.dep")
      );

      LatexmkDepParser parser = new LatexmkDepParser(jimfs, "/fig-1-1.dep");
      Map<Path, Set<Path>> result = parser.parse();

      assertThat(result)
          .hasSize(1);
      Set<Path> r2 = result.values().stream()
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Impossible"));
      assertThat(r2)
          .hasSize(128);
    }
  }
}