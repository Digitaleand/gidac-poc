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

package ch.vd.gidac.domain.core.pdf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dita.dost.ProcessorFactory;
import org.dita.dost.exception.DITAOTException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DitaOtApiToolkit {

  private static final Logger log = LogManager.getFormatterLogger(DitaOtApiToolkit.class);

  @Test
  void check() throws DITAOTException, IOException {
    final var ditaRoot = Paths.get( "/home/mehdi/Applications/dita-ot-4.0" );
    final var root = Paths.get( "/tmp/gidac" );
    final var inputFile =  root.resolve( "input/gidac-flowers/flowers/flowers.ditamap" );
    final var outputDir = root.resolve( "output" );

    final var factory = ProcessorFactory.newInstance( ditaRoot.toFile() );
    final var processor = factory.newProcessor( "pdf");
    processor.setInput( inputFile.toFile() )
        .setOutputDir( outputDir.toFile() )
        .run();
    final var list = Files.list( outputDir );
    assertFalse(list.toList().isEmpty());
  }
}
