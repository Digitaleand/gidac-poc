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

import ch.vd.gidac.domain.core.specifications.ArchiveCreationSpecification;
import org.javatuples.Quintet;

/**
 * Represent an archive to handle to generate the pdf.
 *
 * @param name         the name of the file.
 * @param originalName the original name of the file.
 * @param size         the size of the archive.
 * @param bytes        the content of the archive.
 * @param contentType  the MIME type of the archive.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public record Archive( String name, String originalName, long size, byte[] bytes, String contentType ) {

  /**
   * Create specifications to pass to the specification from arguments valid on the archive creation content.
   *
   * @param name         the name of the archive
   * @param originalName the original name of the archive
   * @param size         the size of the archive
   * @param content      the content of the archive
   * @param mime         the mime type of the archvie
   *
   * @return arguments to use with the specification.
   */
  public static Quintet<String, String, Long, byte[], String> getSpecificationArguments (final String name,
                                                                                         final String originalName,
                                                                                         final Long size,
                                                                                         final byte[] content,
                                                                                         final String mime) {
    return Quintet.with( name, originalName, size, content, mime );
  }

  /**
   * Create a new instance of an archive which is valid according to business rules.
   *
   * @param name         the name of the archive to create
   * @param originalName the original name of the archive
   * @param size         the size of the archive
   * @param content      the content of the archive
   * @param mimeType     the mime type of the archive
   *
   * @return a new instance of an archive.
   *
   * @throws InvalidArchiveCreationException thrown if the archive cannot be created with provided parameters.
   */
  public static Archive create (final String name, String originalName, final long size, final byte[] content,
                                final String mimeType) {
    final var spec = new ArchiveCreationSpecification();
    if (spec.test( getSpecificationArguments( name, originalName, size, content, mimeType ) )) {
      return new Archive( name, originalName, size, content, mimeType );
    }
    throw new InvalidArchiveCreationException( "The archive cannot be created since it is not compliant with business rules" );
  }

  /**
   * Create a new instance of an archive which is valid according to business rules.
   *
   * <p>If the original name is not present, the provided name will be used. The {@code size} of the archive is
   * calculated using the content provided.</p>
   *
   * @param name     the name of the archive to create
   * @param content  the content of the archive
   * @param mimeType the mime type of the archive
   *
   * @return a  new instance of an archive.
   *
   * @throws InvalidArchiveCreationException thrown if the archive cannot be created with provided parameters.
   */
  public static Archive create (final String name, final byte[] content, final String mimeType) {
    return create( name, name, content.length, content, mimeType );
  }
}
