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

package ch.vd.gidac.domain.core.policies;

import ch.vd.gidac.domain.core.Archive;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiveValidPolicyTest {

  private final ArchiveValidPolicy policy = new ArchiveValidPolicy();

  @Test
  void archiveNull () {
    assertFalse( policy.test( null ) );
  }

  @Test
  void archiveWithoutPayload () {
    final var archive = new Archive( "foo", "foo", 0, new byte[] {}, "application/zip" );
    assertFalse( policy.test( archive ) );
  }

  @Test
  void archiveWithNullPayload () {
    final var archive = new Archive( "foo", "foo", 0, null, "application/zip" );
    assertFalse( policy.test( archive ) );
  }

  @Test
  void archiveWithoutMimeType () {
    final var archive = new Archive( "foo", "foo", 0, "test".getBytes( StandardCharsets.UTF_8 ),
        "" );
    assertFalse( policy.test( archive ) );
  }

  @Test
  void archiveWithoutName() {
    final var archive = new Archive( "", "foo", 0, "test".getBytes( StandardCharsets.UTF_8 ),
        "application/zip" );
    assertFalse( policy.test( archive ) );
  }

  @Test
  void archiveWithoutOriginalName() {
    final var archive = new Archive( "foo", "", 0, "test".getBytes( StandardCharsets.UTF_8 ),
        "application/zip" );
    assertFalse( policy.test( archive ) );
  }

  @Test
  void archiveValid() {
    final var archive = new Archive( "foo", "bar", 0, "test".getBytes( StandardCharsets.UTF_8 ),
        "application/zip" );
    assertTrue( policy.test( archive ) );
  }
}