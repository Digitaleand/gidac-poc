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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * This api is design to achieve common task with the file system.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public interface FsManager {

  /**
   * Indicates the location on the working directory.
   *
   * @version 0.0.1
   * @since 0.0.1
   */
  public enum FsLocation {
    ROOT( 1 ),
    INPUT( 2 ),
    OUTPUT( 3 );

    private final int value;

    FsLocation (int value) {
      this.value = value;
    }

    public int getValue () {
      return value;
    }

    @Override
    public String toString () {
      return Integer.toString( value );
    }
  }

  /**
   * Get the request id associate to the manage.
   *
   * @return the instance of the request id.
   */
  RequestId getRequestId ();

  /**
   * Get the absolute path to the working directory.
   *
   * @return the absolute path to the working directory
   */
  Path getWorkingDirectory ();

  /**
   * Get the input directory.
   *
   * <p>This is the directory used by the process to put data extracted from the archive to process.</p>
   *
   * @return the input directory.
   */
  Path getInputDirectory ();

  /**
   * Get the output directory.
   *
   * <p>This is the directory where all outputs files (generated pdfs) will be stored before processing.<br/>The
   * archive to send to the the client may be also stored in this directory if the system is not processed
   * in memory.</p>
   *
   * @return the output directory.
   */
  Path getOutputDirectory ();

  /**
   * Initialize the working directory.
   *
   * @throws IOException raise if something goes wrong during the preparation of the file system.
   */
  void init () throws IOException;

  /**
   * Clean up the working directory.
   *
   * @throws IOException raise if something goes wrong during the preparation of the file system.
   */
  void cleanup () throws IOException;

  /**
   * Store a content in a file with name {@code name} at a given location in the working directory.
   *
   * @param content  the content to save.
   * @param name     the name of the file to save to.
   * @param location the location to save the content.
   */
  void storeContent (byte[] content, String name, FsLocation location);

  /**
   * Store a string content in a file with name {@code name} at a given location in the working directory.
   *
   * @param content  the content to save
   * @param name     the name of the file to save to
   * @param location the location of the content.
   */
  default void storeContent (final String content, final String name, final FsLocation location) {
    storeContent( content.getBytes( StandardCharsets.UTF_8 ), name, location );
  }

  /**
   * Mark the working directory as locked.
   *
   * @throws IllegalStateException if the directory is already locked
   */
  void lock();

  /**
   * Mark the working directory as unlocked.
   */
  void unlock();

  /**
   * Check if the working directory is locked or not.
   *
   * @return {@code true} if the working directory is locked, {@code false} otherwise.
   */
  boolean isLocked();
}
