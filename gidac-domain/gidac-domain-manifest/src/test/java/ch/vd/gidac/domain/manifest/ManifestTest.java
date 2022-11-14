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

package ch.vd.gidac.domain.manifest;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ManifestTest {
  @Test
  void createManifest () throws JAXBException {
    final var jaxbContext = JAXBContext.newInstance( Manifest.class );
    final var unmarshaller = jaxbContext.createUnmarshaller();
    try (final var is = getClass().getClassLoader().getResourceAsStream( "samples/manifest.xml" )) {
      assertNotNull( is, "The input stream must be defined" );
      final var manifest = (Manifest) unmarshaller.unmarshal( is );
      assertNotNull( manifest, "The manifest must be defined" );
      assertEquals( "1.0", manifest.getVersion() );
      final var item = manifest.getItems().getItem().get( 0 );
      assertEquals( "1", item.getName() );
      assertEquals( "file.dita", item.getDitamap() );
      assertEquals( "file1.dita", item.getFiles().getFile().get( 0 ) );
    } catch (final Exception e) {
      fail( e.getMessage() );
    }

  }
}
