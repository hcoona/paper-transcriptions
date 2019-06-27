package io.github.hcoona;

import java.io.IOException;

public class IOWrapperRuntimeException extends RuntimeException {

  public IOWrapperRuntimeException(IOException cause) {
    super(cause);
  }

  @Override
  public synchronized IOException getCause() {
    return (IOException) super.getCause();
  }
}
