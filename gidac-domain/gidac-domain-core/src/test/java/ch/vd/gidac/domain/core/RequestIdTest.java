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

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestIdTest {

  @Test
  void fromString() {
    // GIVEN
    final var id = "30a87b39-10b9-49b9-bebb-677a57bf1fcd";

    // WHEN
    final var requestId = RequestId.fromString( id );

    // THEN
    assertNotNull( requestId );
    assertEquals( id, requestId.value().toString() );
  }

  @Test
  void generateString() {
    // GIVEN
    final var id = "30a87b39-10b9-49b9-bebb-677a57bf1fcd";

    // WHEN
    final var requestId = RequestId.fromString( id );

    // THEN
    assertEquals( id, requestId.toString() );
  }

  @Test
  void random() {
    final var requestId = RequestId.generate();
    assertNotNull( requestId );
    assertDoesNotThrow( () -> {
      UUID.fromString( requestId.toString() );
    } );
  }
}