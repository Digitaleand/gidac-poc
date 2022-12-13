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

package ch.vd.gidac.domain.core.pdf.processor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Just for demonstration purpose")
class DefaultProcessoorIntegrationTest {

  @Test
  void checkCommandLineProcessing () {
    // GIVEN
    final var commandLine = "/home/georgette/Applications/oxygen-publishing-engine-3.x/bin/dita " +
        "--input=/tmp/863762cc-d71d-49d9-8ee6-586ef12cab6812161336840445757620/input/flowers/flowers.ditamap " +
        "--format=pdf-css-html5 " +
        "--output=/tmp/863762cc-d71d-49d9-8ee6-586ef12cab6812161336840445757620/output " +
       // "--style=/tmp/863762cc-d71d-49d9-8ee6-586ef12cab6812161336840445757620/input/custom/main.css " +
        "--temp=/tmp/863762cc-d71d-49d9-8ee6-586ef12cab6812161336840445757620/tmp";

    // WHEN
    Assertions.assertDoesNotThrow( () -> {
      final var executor = new DefaultExecutor();
      executor.setExitValue( 0 );
      executor.execute( CommandLine.parse( commandLine ) );
    } );
  }
}
