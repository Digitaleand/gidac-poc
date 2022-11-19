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

package ch.vd.gidac.domain.core.specifications;

import ch.vd.gidac.domain.core.Archive;
import ch.vd.gidac.domain.core.policies.BinaryPayloadPolicy;
import ch.vd.gidac.domain.core.policies.NotEmptyStringPolicy;
import org.javatuples.Quintet;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Specification for archive creation.
 *
 * <p>This specification ensure that the archive can be created according to provided parameters.</p>
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public class ArchiveCreationSpecification implements Predicate<Quintet<String, String, Long, byte[], String>> {

  /**
   * Create specifications to pass to the specification from arguments valid on the archive creation content.
   *
   * <p>This is an alias of {@link Archive#getSpecificationArguments(String, String, Long, byte[], String)}</p>
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
    return Archive.getSpecificationArguments( name, originalName, size, content, mime );
  }

  private final NotEmptyStringPolicy policy = new NotEmptyStringPolicy();

  private final BinaryPayloadPolicy binaryPolicy = new BinaryPayloadPolicy();

  private final BiPredicate<Long, byte[]> sizePolicy = (size, content) -> size == content.length;

  /**
   * Test if the argyments match expectations to create the archive.
   *
   * <p>The form of the quintent is the following one.<br/><bloquote><pre>
   *   quintet: name, originalName, size, content, mimeType
   * </pre></bloquote></p>
   *
   * @param item the input argument
   *
   * @return {@code true} if the archive can be created, {@code false} otherwise.
   */
  @Override
  public boolean test (Quintet<String, String, Long, byte[], String> item) {
    return policy.test( item.getValue0() ) &&
        policy.test( item.getValue1() ) &&
        binaryPolicy.test( item.getValue3() ) &&
        sizePolicy.test( item.getValue2(), item.getValue3() ) &&
        policy.test( item.getValue4() );
  }
}
