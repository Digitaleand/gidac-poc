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

import java.util.List;
import java.util.stream.Stream;

/**
 * Decorator for the manifest. It allows to abstract the core implementation of the manifest.
 *
 * <p>This is an adapter on the {@link Manifest} and it will not evolve when the manifest is regenerated</p>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class ManifestDecorator {

  public static final String MANIFEST_FILE_NAME = "manifest.xml";

  private final Manifest manifest;

  public ManifestDecorator (final Manifest manifest) {
    this.manifest = manifest;
  }

  public String getVersion () {
    return manifest.version;
  }

  public List<Item> getItems () {
    return manifest.getItems().getItem();
  }

  public Stream<Item> getItemsStream () {
    return getItems().stream();
  }

  public List<String> getFiles (final Item item) {
    return item.getFiles().file;
  }

  public Stream<String> getFilesStream (final Item item) {
    return item.getFiles().file.stream();
  }
}
