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

package ch.vd.gidac.domain.core.compress;

import java.io.File;
import java.nio.file.Path;

/**
 * Definition of an object which deals with archive.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public interface ZipManager {
  /**
   * Zip the content of a directory / file.
   *
   * @param path the path to zip the content.
   *
   * @throws RuntimeException thrown if an error occurred during the process.
   */
  void zip (Path path);

  /**
   * Zip the content of a file.
   *
   * @param file the file to zip.
   *
   * @throws RuntimeException thrown if an error occurred during the process.
   */
  void zip (File file);

  /**
   * Unzip a given content to a given path.
   *
   * @param content the content to unzip
   * @param path    the path to unzip the content to
   *
   * @throws RuntimeException thrown if an error occurred during the process.
   */
  void unzip (byte[] content, Path path);

  /**
   * unzip the content of a file into a given path.
   *
   * @param file the file to unzip
   * @param path the path to unzip the content to
   *
   * @throws RuntimeException thrown if an error occurred during the process.
   */
  void unzip (File file, Path path);
}
