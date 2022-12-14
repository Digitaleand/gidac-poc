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

package ch.vd.gidac.application.appinit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppInitRequestTest {

  @ParameterizedTest
  @ValueSource(strings = { "test", "012test24", "TheAppl1cat10n" })
  void createApplicationRequest (final String name) {
    final var request = AppInitRequest.create( name, "/tmp", true );
    assertNotNull( request );
    assertEquals( name, request.appName() );
  }

  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void unableToCreateAppRequest (final String name) {
    assertThrows( IllegalArgumentException.class, () -> {
      AppInitRequest.create( name, "tmp", true );
    } );
  }

  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void unableToCreateAppRequestWithInvalidBaseDir(final String baseDir) {
    assertThrows( IllegalArgumentException.class, () -> {
      AppInitRequest.create( "test", baseDir, false );
    } );
  }

  @Test
  void invalidNullName() {
    assertThrows( IllegalArgumentException.class, () -> {
      AppInitRequest.create( null, "/tmp", true );
    } );
  }
}