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

import java.util.UUID;

/**
 * This is the representation of the request id sent by the client to perform the pdf generation process.
 *
 * <p>This id is used to generate the working directory of the process.</p>
 *
 * @version 0.0.1
 */
public record RequestId( UUID value ) {

  /**
   * Factory used to generate the requestId from a string.
   *
   * @param value the string representation of the id
   *
   * @return on instance of the requestId
   */
  public static RequestId fromString (final String value) {
    return new RequestId( UUID.fromString( value ) );
  }

  /**
   * Generate a new random requestId.
   *
   * @return a fresh instance of a randomly generated requestId.
   */
  public static RequestId generate () {
    return new RequestId( UUID.randomUUID() );
  }

  @Override
  public String toString () {
    return value.toString();
  }
}
