package io.github.hcoona;

import static org.assertj.core.api.Assertions.assertThat;

class FilenameUtilsTest {

  @org.junit.jupiter.api.Test
  void testGetFilenameWithoutExtension() {
    assertThat(FilenameUtils.getFilenameWithoutExtension("main.tex"))
        .isEqualTo("main");
  }
}