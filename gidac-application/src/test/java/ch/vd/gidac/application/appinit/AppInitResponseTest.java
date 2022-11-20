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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppInitResponseTest {

  @Test
  void appResponseCreationSuccess () {
    // GIVEN
    final var name = "test";
    final var dir = Path.of( FileUtils.getTempDirectoryPath() );
    final var request = AppInitRequest.create( name, "/tmp", true );

    // WHEN
    final var response = AppInitResponse.create( request, dir );

    // THEN
    assertNotNull( response );
    assertNotNull( response.request() );
    assertEquals( name, response.request().appName() );
    assertEquals( dir, response.appWorkingDirectory() );
  }


  @Test
  void appResponseCreationFailureWithInvalidRequest () {
    // GIVEN
    final var dir = Path.of( FileUtils.getTempDirectoryPath() );

    // WHEN
    assertThrows( IllegalArgumentException.class, () -> AppInitResponse.create( null, dir ) );
  }

  @Test
  void appResponseCreationFailureWithInvalidDir () {
    // GIVEN
    final var request = AppInitRequest.create( "test", "/tmp", true );

    // WHEN
    assertThrows( IllegalArgumentException.class, () -> AppInitResponse.create( request, null ) );
  }
}