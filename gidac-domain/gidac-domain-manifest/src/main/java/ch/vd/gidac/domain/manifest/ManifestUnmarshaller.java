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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper able to unmarshall the manifest
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class ManifestUnmarshaller {

  public ManifestUnmarshaller() {

  }

  public Manifest unmarshall( final InputStream inputStream, boolean autoClose ) {
    try {
      final var jaxbContext = JAXBContext.newInstance( Manifest.class );
      final var unmarshaller = jaxbContext.createUnmarshaller();
      final var manifest = (Manifest) unmarshaller.unmarshal( inputStream );
      if (autoClose) {
        inputStream.close();
      }
      return manifest;
    } catch( JAXBException | IOException exception) {
      throw new UnmarshallException(exception);
    }
  }
}
