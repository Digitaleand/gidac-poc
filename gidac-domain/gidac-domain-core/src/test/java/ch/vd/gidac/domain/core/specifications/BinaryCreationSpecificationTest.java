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

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinaryCreationSpecificationTest {

  @Test
  void validBinary () {
    // GIVEN
    final var mime = "application/json";
    final var name = "test";
    final var payload = "test".getBytes( StandardCharsets.UTF_8 );

    // WHEN
    final var spec = new BinaryCreationSpecification();
    final var result = spec.isSatisfiedBy( Triple.of( mime, name, payload ) );

    // THEN
    assertTrue(result);
  }

  private Triple<String, String, byte[]> getTriple(final String mime, final String name, final String payload) {
    return Triple.of( mime, name, payload.getBytes( StandardCharsets.UTF_8 ) );
  }

  void exec(final String mime, final String name, final String payload) {
    final var spec = new BinaryCreationSpecification();
    final var result = spec.isSatisfiedBy( getTriple( mime, name, payload ) );
    assertFalse( result );
  }

  @Test
  void invalidBinaryMime() {
    exec("", "test", "test");
  }

  @Test
  void invalidBinaryName() {
    exec( "application/json", "", "test" );
  }

  @Test
  void invalidPayload() {
    exec( "application/json", "test", "" );
  }
}