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

import ch.vd.gidac.domain.core.policies.ExistingPathPolicy;
import ch.vd.gidac.domain.core.specifications.ApplicationDirectoryCreationSpecification;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Aggregate which represents the working directory of the application.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public record ApplicationWorkingDirectory( Path path, String name ) {

  /**
   * Create the working directory of the application if it does not exist.
   *
   * @throws ApplicationDirectoryCreationException thrown if the application directory cannot be created
   * @throws IllegalStateException                 thrown if the application directory exists but is not valid.
   */
  public void createIfNotExists () {
    final var p = path.resolve( name );
    if (!Files.exists( p )) {
      try {
        Files.createDirectories( p );
      } catch (final IOException ioException) {
        throw new ApplicationDirectoryCreationException();
      }
    }
    if (!Files.isDirectory( p )) {
      throw new IllegalStateException( "The working directory of the application is not a valid directory" );
    }
  }

  /**
   * Check if the application directory is valid.
   *
   * @return {@code true} if the application directory is valid, {@code false} otherwise.
   */
  public boolean isValid () {
    final var policy = new ExistingPathPolicy();
    return policy.test( path );
  }

  /**
   * Clean up the application workding directory.
   *
   * @return {@code true} if the process succeeds, {@code false} otherwise.
   */
  public boolean cleanup () {
    return true;
  }

  /**
   * Get the root path of the application working directory.
   *
   * @return the path to the working directory.
   */
  public Path getRoot () {
    return path.resolve( name );
  }

  /**
   * Create a new instance of the {@code ApplicationWorkingDirectory}.
   *
   * @param appName              the name of the application.
   * @param rootDir              the root directory.
   * @param useNativeTemporaryFs indicates if the system will use the native API.
   *
   * @return a new instance of the workding directory
   *
   * @throws InvalidApplicationDirectoryException thrown if the application workding directory cannot be created.
   */
  public static ApplicationWorkingDirectory create (final String appName,
                                                    final String rootDir,
                                                    final boolean useNativeTemporaryFs) {
    final var specification = new ApplicationDirectoryCreationSpecification();
    Path path;
    if (useNativeTemporaryFs) {
      path = Path.of( FileUtils.getTempDirectoryPath() );
    } else {
      path = Path.of( rootDir );
    }
    if (specification.isSatisfiedBy( Pair.with( appName, path ) )) {
      return new ApplicationWorkingDirectory( path, appName );
    }
    throw new InvalidApplicationDirectoryException( "The provided arguments cannot be used to create a " +
        "valid application working directory" );
  }
}
