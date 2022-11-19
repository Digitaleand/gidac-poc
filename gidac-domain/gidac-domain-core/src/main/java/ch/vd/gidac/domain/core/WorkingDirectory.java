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

package ch.vd.gidac.domain.core;

import ch.vd.gidac.domain.core.specifications.ValidWorkingDirectorySpecification;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static ch.vd.gidac.domain.manifest.ManifestDecorator.MANIFEST_FILE_NAME;

/**
 * Represent a working directory structure.
 *
 * @param root            the root of the working directory.
 * @param inputDirectory  the directory to store incoming data.
 * @param outputDirectory the directory to store outgoing data.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public record WorkingDirectory( Path root, Path inputDirectory, Path outputDirectory ) {

  private static final String OUTPUT_DIRECTORY = "output";

  private static final String INPUT_DIRECTORY = "input";

  private static final String DIRTY_FILENAME = ".dirty";

  private static final String LOCK_FILE_NAME = "file.lock";

  public boolean isDirty () {
    return Files.exists( root.resolve( DIRTY_FILENAME ) );
  }

  public boolean isLocked () {
    return Files.exists( root.resolve( LOCK_FILE_NAME ) );
  }

  /**
   * Clean up the working directory by removing files from the filesystem.
   *
   * @return the current instance of the working directory.
   *
   * @throws IOException thrown if something goes wrong during the process.
   */
  public WorkingDirectory cleanup () throws IOException {
    if (isDirty()) {
      throw new IOException( "The working directory is dirty, it can't be deleted" );
    }
    Files.deleteIfExists( outputDirectory );
    Files.deleteIfExists( inputDirectory );
    Files.deleteIfExists( root );
    return this;
  }

  /**
   * Resolve the path to a file. If the file does not exists, it will be created before returning the path to it.
   *
   * @param filename the name of the file to get or create
   *
   * @return the path to that file
   *
   * @throws IOException thrown if the file cannot be created if it does not exists.
   */
  private Path getFileOrCreate (final String filename) throws IOException {
    final var p = root.resolve( filename );
    if (Files.exists( p )) {
      return p;
    }
    return Files.createFile( root.resolve( filename ) );
  }

  /**
   * Retreive the file which indicates the working directory is dirty.
   *
   * @return the path to the dirty file.
   *
   * @throws IOException raise if the file cannot be created in case it does not exists.
   */
  private Path getDirtyFile () throws IOException {
    return getFileOrCreate( DIRTY_FILENAME );
  }

  /**
   * Retrieve the file which indicates the working directory is locked.
   *
   * @return the path to the lock file.
   *
   * @throws IOException raise if the file cannot be created in case it does not exists.
   */
  private Path getLockFile () throws IOException {
    return getFileOrCreate( LOCK_FILE_NAME );
  }

  /**
   * Resolve a path relative to a given path in the working directory (root, input, output).
   *
   * @param base         the base path to resolve from
   * @param relativePath the relative path to the file to resolve
   * @param mustExists   flag to indicate if the file must exist.
   *
   * @return the path to the file
   *
   * @throws IllegalStateException if the files does not exist but the request expects that it exist.
   */
  private Path resolveFile (final Path base, final String relativePath, boolean mustExists) {
    final var p = base.resolve( relativePath );
    if (!Files.exists( p ) && mustExists) {
      throw new IllegalStateException( "The path " + relativePath + " must exists but it does not" );
    }
    return p;
  }

  /**
   * Resolve a relative path to the file from the root directory.
   *
   * @param relativePath the relative path to the file.
   * @param mustExists   indicate if the directory / file must exist {@code true} or not {@code false}
   *
   * @return the path to the directory / file.
   *
   * @throws IllegalStateException if the files does not exist but the request expects that it exist.
   */
  public Path resolveFromRoot (final String relativePath, boolean mustExists) {
    return resolveFile( root, relativePath, mustExists );
  }

  /**
   * Mark the working directory dirty.
   *
   * @return the current instance of the working directory.
   *
   * @throws DirtyDirectoryException raise if the dirty file cannot be created.
   */
  public WorkingDirectory markDirty () {
    if (isDirty()) {
      throw new DirtyDirectoryException( "The directory is yet dirty" );
    }
    try {
      final var dirtyFile = getDirtyFile();
      FileUtils.write( dirtyFile.toFile(), "1", Charset.defaultCharset() );
      return this;
    } catch (final IOException ioException) {
      throw new DirtyDirectoryException( ioException );
    }
  }

  public void markClean () throws IOException {
    final var dirtyFile = getDirtyFile();
    Files.delete( dirtyFile );
  }

  public void lock () throws IOException {
    final var lockFile = getLockFile();
    FileUtils.write( lockFile.toFile(), "1", Charset.defaultCharset() );
  }

  public void unlock () throws IOException {
    final var lockFile = getLockFile();
    Files.delete( lockFile );
  }

  public Path getManifestPath () {
    return inputDirectory.resolve( MANIFEST_FILE_NAME );
  }

  public File getManifestFile () {
    return getManifestPath().toFile();
  }

  /**
   * List files in the output directory.
   *
   * @return the list of output files.
   *
   * @throws IOException thrown if something goes wrong when reading the output directory.
   */
  public List<Path> listOutputFiles () throws IOException {
    try (final var s = Files.list( outputDirectory )) {
      return s.toList();
    }
  }

  /**
   * Create a working directory from an existing layout.
   *
   * @param root            the root directory
   * @param inputDirectory  the directory to store input data
   * @param outputDirectory the directory to store output data
   *
   * @return a valid instance of a working directory.
   *
   * @throws IOException thrown if the working directory layout is not valid.
   */
  public static WorkingDirectory create (final Path root, final Path inputDirectory, final Path outputDirectory)
      throws IOException {
    final var specification = new ValidWorkingDirectorySpecification();
    if (!specification.isSatisfiedBy( Arrays.asList( root, inputDirectory, outputDirectory ) )) {
      throw new IllegalStateException( "All directories must exists in order to create a Working directory" );
    }
    return new WorkingDirectory( root, inputDirectory, outputDirectory );
  }

  /**
   * Create a new working directory from a request id.
   *
   * @param requestId the requestId to create the working directory for.
   *
   * @return the working directory
   *
   * @throws IOException thrown if something goes wrong during the creation of the working directory layout.
   */
  public static WorkingDirectory create (final RequestId requestId) throws IOException {
    final var rootDirectory = Files.createTempDirectory( requestId.value().toString() );
    final var inputDirectory = Files.createDirectory( rootDirectory.resolve( INPUT_DIRECTORY ) );
    final var outputDirectory = Files.createDirectory( rootDirectory.resolve( OUTPUT_DIRECTORY ) );
    return create( rootDirectory, inputDirectory, outputDirectory );
  }
}
