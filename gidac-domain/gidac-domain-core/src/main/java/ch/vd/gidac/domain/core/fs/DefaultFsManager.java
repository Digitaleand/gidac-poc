/*
 * Copyright(c) 2022 mehdi.lefebvre@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ch.vd.gidac.domain.core.fs;

import ch.vd.gidac.domain.core.RequestId;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Default implementation of the {@link FsManager}.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultFsManager implements FsManager {

  private static final String OUTPUT_DIRECTORY = "output";
  private static final String INPUT_DIRECTORY = "input";

  private final RequestId requestId;
  private Path workingDirectory;
  private Path inputDirectory;
  private Path outputDirectory;

  /**
   * Create a new instance of the fs manager dedicated to deal with fs during the current process.
   *
   * @param requestId the unique id of the process
   */
  public DefaultFsManager( final RequestId requestId ) {
    this.requestId = requestId;
  }

  @Override
  public RequestId getRequestId() {
    return requestId;
  }

  @Override
  public Path getWorkingDirectory() {
    return workingDirectory;
  }

  @Override
  public Path getInputDirectory() {
    return inputDirectory;
  }

  @Override
  public Path getOutputDirectory() {
    return outputDirectory;
  }

  @Override
  public void init() throws IOException {
    workingDirectory = Files.createTempDirectory( requestId.value().toString() );
    inputDirectory = Files.createDirectory( workingDirectory.resolve( INPUT_DIRECTORY ) );
    outputDirectory = Files.createDirectory( workingDirectory.resolve( OUTPUT_DIRECTORY ) );
  }

  @Override
  public void cleanup() throws IOException {
    Files.deleteIfExists( outputDirectory );
    Files.deleteIfExists( inputDirectory );
    Files.deleteIfExists( workingDirectory );
  }

  @Override
  public void storeContent( byte[] content, String name, FsLocation location ) {
    // Check if the file exists.
    Path p;
    switch ( location ) {
      case INPUT -> p = inputDirectory;
      case OUTPUT -> p = outputDirectory;
      default -> p = workingDirectory;
    }
    final var file = p.resolve( name );
    if ( Files.exists( file ) ) {
      throw new IllegalStateException( "The name with name " + name + " already exists in " + p );
    }
    try ( final var fos = new FileOutputStream( file.toFile() ) ) {
      IOUtils.write( content, fos );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void lock() {
    // For now, we are generating a fresh working directory each time.
  }

  @Override
  public void unlock() {
    // For now, we are generating a fresh working directory each time.
  }

  @Override
  public boolean isLocked() {
    // For now, we are generating a fresh working directory each time.
    return false;
  }
}
