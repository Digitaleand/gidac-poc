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

import org.apache.commons.lang3.RandomStringUtils;
import org.javatuples.Quintet;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArchiveCreationSpecificationTest {

  private final ArchiveCreationSpecification spec = new ArchiveCreationSpecification();

  @Test
  void checkValidArchive () {
    // GIVEN
    final var name = "test";
    final var originalName = "test2";
    final var content = "this is a test".getBytes( StandardCharsets.UTF_8 );
    final var size = content.length;
    final var mime = "text/strings";

    // WHEN
    final var result = spec.test( Quintet.with( name, originalName, (long) size, content, mime ) );

    // THEN
    assertTrue( result );
  }

  private void checkFailure(final String name, final String originalName, final Long size, final byte[] content, final String mime) {
    // WHEN
    final var result = spec.test( Quintet.with( name, originalName, (long) size, content, mime ) );

    // THEN
    assertFalse( result );
  }

  private final Supplier<String> name = () -> RandomStringUtils.random( 10 );
  private final Supplier<String> mime = () -> "text/strings";
  private final Supplier<byte[]> content = () -> RandomStringUtils.random( 20 ).getBytes( StandardCharsets.UTF_8 );

  @Test
  void checkInvalidArchiveContent() {
    final var payload = content.get();
    checkFailure( "", name.get(), (long) payload.length, payload, mime.get()  );
  }

  @Test
  void checkInvalidArchiveContent2() {
    final var payload = content.get();
    checkFailure( name.get(), "", (long) payload.length, payload, mime.get()  );
  }

  @Test
  void checkInvalidArchiveContent3() {
    final var payload = content.get();
    checkFailure( name.get(), "", (long) payload.length - 1, payload, mime.get()  );
  }

  @Test
  void checkInvalidArchiveContent4() {
    final var payload = content.get();
    checkFailure( name.get(), "", 0L, payload, mime.get()  );
  }

  @Test
  void checkInvalidArchiveContent5() {
    final var payload = content.get();
    checkFailure( name.get(), "", 1L, "".getBytes( StandardCharsets.UTF_8 ), mime.get()  );
  }

  @Test
  void checkInvalidArchiveContent6() {
    final var payload = content.get();
    checkFailure( name.get(), "", (long) payload.length - 1, content.get(), ""  );
  }
}