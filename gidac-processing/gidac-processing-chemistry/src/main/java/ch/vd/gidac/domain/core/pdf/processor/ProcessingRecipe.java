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

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

/**
 * Recipe for processing to send to the runner.
 *
 * <p>Take care, the recipe is only valid with all paramters.</p>
 *
 * @param executable the executable to invoke,
 * @param ditaMap    the ditamap to generate data for.
 * @param outputDir  the output directory to create the pdf in.
 * @param format     the format of the output (pdf only for now).
 * @param style      the style to apply during the processing.
 */
public record ProcessingRecipe(String executable, String tmpDir, String ditaMap, String outputDir, String format,
                               String style, boolean verbose) {

  /**
   * Get an instance of the builder.
   *
   * @return a fresh instance of a builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to use to create the recipe.
   */
  public static class Builder {
    private String executable;

    private String tmpDir;

    private String ditaMap;

    private String outputDir;

    private String format;

    private String style;

    private boolean verbose = false;

    Builder() {
    }

    public Builder executable( final String executable ) {
      this.executable = executable;
      return this;
    }

    public Builder tmpDir( final String tmpDir ) {
      this.tmpDir = tmpDir;
      return this;
    }

    public Builder ditaMap( final String ditaMap ) {
      this.ditaMap = ditaMap;
      return this;
    }

    public Builder outputDir( final String outputDir ) {
      this.outputDir = outputDir;
      return this;
    }

    public Builder format( final String format ) {
      this.format = format;
      return this;
    }

    public Builder style( final String style ) {
      this.style = style;
      return this;
    }

    public Builder verbose( final boolean verbose ) {
      this.verbose = verbose;
      return this;
    }

    private final Predicate<String> isValidString = StringUtils::isNotEmpty;

    /**
     * Create a new instance of a processing recipe based on the provided information.
     * <p>
     * If the recipe is not fullfilled, the creation will fail with a runtime exception.
     *
     * @return the processing recipe instance
     *
     * @throws RuntimeException if the recipe is not valid
     */
    public ProcessingRecipe build() {
      final var valid = isValidString.test( executable ) &&
          isValidString.test( tmpDir ) &&
          isValidString.test( ditaMap ) &&
          isValidString.test( outputDir ) &&
          isValidString.test( format ) /*&&
          isValidString.test( style )*/;
      if ( valid ) {
        return new ProcessingRecipe( executable, tmpDir, ditaMap, outputDir, format, style, verbose );
      }
      throw new IllegalStateException( "all arguments must be defined" );
    }
  }
}
