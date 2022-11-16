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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManifestExistsPolicyTest {

  @Test
  void fileExists() throws IOException {
    // GIVEN
    final var p = Files.createTempDirectory( "3efbf8b0-41a7-4f7c-b575-01939db037a8" );
    final var manifest = Files.createFile( Path.of( p.toString(), "manifest.xml" ) );

    // WHEN
    final var policy = new ManifestExistsPolicy();
    final var result = policy.test( p );

    // THEN
    assertTrue( result );
    Files.delete( manifest );
    Files.delete( p );
  }

  @Test
  void fileNotExists() throws IOException {
    // GIVEN
    final var p = Files.createTempDirectory( "3efbf8b0-41a7-4f7c-b575-01939db037a8" );

    // WHEN
    final var policy = new ManifestExistsPolicy();
    final var result = policy.test( p );

    // THEN
    assertFalse( result );
    Files.delete( p );
  }
}