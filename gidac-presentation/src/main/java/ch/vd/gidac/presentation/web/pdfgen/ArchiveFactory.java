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

package ch.vd.gidac.presentation.web.pdfgen;

import ch.vd.gidac.domain.core.Archive;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Factory to use to transform {@link org.springframework.web.multipart.MultipartFile} to
 * {@link ch.vd.gidac.domain.core.Archive}.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public class ArchiveFactory {

  /**
   * Transform a given multipart file which represents a zip to an archive representation in our model.
   *
   * @param file the file to transform.
   *
   * @return the archive representation of the file.
   *
   * @throws IOException raised if an error occurred when reading the file.
   */
  public Archive toArchive( final MultipartFile file ) throws IOException {
    return new Archive( file.getName(), file.getOriginalFilename(), file.getSize(),
        file.getBytes(), file.getContentType() );
  }
}
