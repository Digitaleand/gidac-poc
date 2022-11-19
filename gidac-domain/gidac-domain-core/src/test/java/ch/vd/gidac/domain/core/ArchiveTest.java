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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchiveTest {

  @Test
  void createValidArchive() {
    // GIVEN
    final var name = "test";
    final var originalName = "test01";
    final var content = "this is a test".getBytes( StandardCharsets.UTF_8);
    final var size = content.length;
    final var mime = "text/strings";

    // WHEN
    final var archive = Archive.create( name, originalName, size, content, mime );

    // THEN
    assertNotNull(archive);
    assertEquals(name, archive.name());
    assertEquals( originalName, archive.originalName() );
    assertEquals( size, archive.size() );
    assertEquals( content, archive.bytes() );
    assertEquals( mime, archive.contentType() );
  }

  @Test
  void createValidArchiveFromShort() {
    // GIVEN
    final var name = "test";
    final var content = "this is a test".getBytes(StandardCharsets.UTF_8);
    final var mime = "text/strings";

    // WHEN
    final var archive = Archive.create( name, content, mime );

    // THEN
    assertEquals( name, archive.name() );
    assertEquals( name, archive.originalName() );
    assertEquals( content.length, archive.size() );
    assertEquals( content, archive.bytes() );
    assertEquals( mime, archive.contentType() );
  }

  @Test
  void invalidArchiveCreationNoName() {
    // GIVEN
    final var name = "";
    final var originalName = "test";
    final var content = "this is a test".getBytes( StandardCharsets.UTF_8);
    final var size = content.length;
    final var mime = "text/strings";

    assertThrows( InvalidArchiveCreationException.class, () -> {
      Archive.create( name, originalName, size, content, mime );
    } );
  }
}