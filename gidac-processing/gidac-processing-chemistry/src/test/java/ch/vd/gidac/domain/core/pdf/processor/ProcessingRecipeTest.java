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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessingRecipeTest {

  @Test
  void createValidRecipe() {
    // GIVEN
    final var executable = "executable";
    final var tmpDir = "tmpDir";
    final var ditaMap = "ditaMap";
    final var outputDir = "outputDir";
    final var format = "format";
    final var style = "style";

    // WHEN
    final var recipe = ProcessingRecipe.builder()
        .executable( executable )
        .tmpDir( tmpDir )
        .ditaMap( ditaMap )
        .outputDir( outputDir )
        .format( format )
        .style( style )
        .build();

    // THEN
    assertEquals( executable, recipe.executable() );
    assertEquals( tmpDir, recipe.tmpDir() );
    assertEquals( ditaMap, recipe.ditaMap() );
    assertEquals( outputDir, recipe.outputDir() );
    assertEquals( format, recipe.format() );
    assertEquals( style, recipe.style() );
  }

  @Test
  void invalidRecipe() {

    // GIVEN
    final var executable = "executable";
    final var tmpDir = "tmpDir";
    final var ditaMap = "ditaMap";
    final var outputDir = "outputDir";
    final var format = "format";
    final var style = "";

    // WHEN / THEN
    assertThrows( IllegalStateException.class, () -> {
      ProcessingRecipe.builder()
          .executable( executable )
          .tmpDir( tmpDir )
          .ditaMap( ditaMap )
          .outputDir( outputDir )
          .format( format )
          .style( style )
          .build();
    } );
  }

}