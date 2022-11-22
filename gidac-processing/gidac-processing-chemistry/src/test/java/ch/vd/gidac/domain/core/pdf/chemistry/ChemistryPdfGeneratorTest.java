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

package ch.vd.gidac.domain.core.pdf.chemistry;

import ch.vd.gidac.domain.core.DitaMap;
import ch.vd.gidac.domain.core.WorkingDirectory;
import ch.vd.gidac.domain.core.pdf.processor.ProcessingRecipe;
import ch.vd.gidac.domain.core.pdf.processor.Processor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.io.IOException;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ChemistryPdfGeneratorTest {

  class ProcessingRecipeMatcher implements ArgumentMatcher<ProcessingRecipe> {

    private final ProcessingRecipe recipe;

    ProcessingRecipeMatcher (final ProcessingRecipe recipe) {
      this.recipe = recipe;
    }

    @Override
    public boolean matches (final ProcessingRecipe processingRecipe) {
      return recipe.equals( processingRecipe );
    }
  }

  @Test
  void generatePdf() throws IOException, InterruptedException {
    // GIVEN
    final var root = Paths.get( "/tmp/gidac/test01" );
    final var input = root.resolve( "input" );
    final var output = root.resolve( "output" );
    final var wd = WorkingDirectory.create( root, input, output );
    final var ditaMap = DitaMap.fromPath( input.resolve( "flowers/flowers.ditamap" ) );
    final var processor = mock( Processor.class );

    // WHEN
    final var generator = new ChemistryPdfGenerator(processor, "/tmp");
    generator.generatePdf( wd, ditaMap );

    // THEN
    final var recipe = ProcessingRecipe.builder()
        .ditaMap( ditaMap.value().toString() )
        .executable( "/tmp" )
        .format( "pdf" )
        .outputDir( output.toString() )
        .verbose( false )
        .tmpDir( root.resolve( "tmp" ).toString() )
        .build();
    verify( processor, times(1) ).execute( argThat( new ProcessingRecipeMatcher( recipe ) ) );
  }
}