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

import ch.vd.gidac.domain.core.specifications.BinaryCreationSpecification;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a binary content to send back to the client.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public record Binary( String mimeType, String name, byte[] payload ) {

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Binary binary = (Binary) o;
    return mimeType.equals( binary.mimeType ) && name.equals( binary.name ) && Arrays.equals( payload, binary.payload );
  }

  @Override
  public int hashCode () {
    int result = Objects.hash( mimeType, name );
    result = 31 * result + Arrays.hashCode( payload );
    return result;
  }

  @Override
  public String toString () {
    return "Binary{" +
        "mimeType='" + mimeType + '\'' +
        ", name='" + name + '\'' +
        ", payload=" + Arrays.toString( payload ) +
        '}';
  }

  public static Binary create (final String mimeType, final String name, final byte[] payload) {
    final var specification = new BinaryCreationSpecification();
    if (specification.isSatisfiedBy( Triple.of( mimeType, name, payload ) )) {
      return new Binary( mimeType, name, payload );
    }
    throw new IllegalArgumentException( "Provided argument are not valid regarding binary creation rules" );
  }
}
