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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class ManifestDecoratorTest {
  private static Manifest manifest;
  private static ManifestDecorator decorator;

  @BeforeAll
  static void setUp () throws JAXBException, IOException {
    final var jaxbContext = JAXBContext.newInstance( Manifest.class );
    final var unmarshaller = jaxbContext.createUnmarshaller();
    try (final var is =
             ManifestDecoratorTest.class.getClassLoader().getResourceAsStream( "samples/manifest.xml" )) {
      assertNotNull( is, "The input stream must be defined" );
      manifest = (Manifest) unmarshaller.unmarshal( is );
      decorator = new ManifestDecorator( manifest );
    } catch (final Exception e) {
      fail( e.getMessage() );
      throw e;
    }
  }

  @Test
  void decorateVersion () {
    assertEquals( "1.0", decorator.getVersion() );
  }

  @Test
  void getItemList () {
    assertNotNull( decorator.getItems() );
    assertEquals( 2, decorator.getItems().size() );
  }

  @Test
  void getItemStream () {
    assertNotNull( decorator.getItemsStream() );
    assertEquals( 2, decorator.getItemsStream().toList().size() );
  }

  @Test
  void getFileList () {
    final var item = manifest.items.item.get( 0 );
    assertNotNull( decorator.getFiles( item ) );
    assertEquals( 3, decorator.getFiles( item ).size() );
  }

  @Test
  void getFileStream () {
    final var item = manifest.items.item.get( 1 );
    assertNotNull( decorator.getFilesStream( item ) );
    assertEquals( 3, decorator.getFilesStream( item ).toList().size() );
  }
}