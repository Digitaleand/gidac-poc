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
 * <p>Take care, the recipe is only valid with all parameters.</p>
 *
 * @implNote The only format supported for now is {@code pdf}.
 * @param executable the executable to invoke,
 * @param ditaMap    the ditamap to generate data for.
 * @param outputDir  the output directory to create the pdf in.
 * @param format     the format of the output (pdf only for now).
 * @param style      the style to apply during the processing.
 */
public record ProcessingRecipe(String executable, // the absolute path to the executable to run
                               String tmpDir,     // the temporary directory for the processing
                               String ditaMap,    // the ditamap to process
                               String outputDir,  // the output directory
                               String format,     // the format of the output
                               String style,      // the style to apply the transformation on
                               boolean verbose) { // the verbosity of the process

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

    /**
     * Builder for the processing recipe.
     */
    Builder() {
    }

    /**
     * Set the executable to use during the processing.
     *
     * @param executable  the executable.
     * @return the current instance of the process recipe builder.
     */
    public Builder executable( final String executable ) {
      this.executable = executable;
      return this;
    }

    /**
     * Set the temporary directory of the processing.
     *
     * @param tmpDir  the temporary directory
     * @return the current instance of the processing recipe builder.
     */
    public Builder tmpDir( final String tmpDir ) {
      this.tmpDir = tmpDir;
      return this;
    }

    /**
     * Set the dita map to process during the execution.
     *
     * @param ditaMap the dita map file to execute
     * @return the current instance of the processing recipe builder.
     */
    public Builder ditaMap( final String ditaMap ) {
      this.ditaMap = ditaMap;
      return this;
    }

    /**
     * Set the output directory to use to put results of the process on.
     *
     * @param outputDir the output directory to use
     * @return the current instance of the processing recipe builder.
     */
    public Builder outputDir( final String outputDir ) {
      this.outputDir = outputDir;
      return this;
    }

    /**
     * Set the format of the output of the process.
     *
     * @implNote  For now, the only supported format is pdf.
     * @param format  the format.
     * @return the current instance of the processing recipe builder.
     * @throws IllegalArgumentException thrown if the format is not supported.
     */
    public Builder format( final String format ) {
      if (!"pdf".equals( format )) {
        throw new UnsupportedFormatException("The only supported format is pdf");
      }
      this.format = format;
      return this;
    }

    /**
     * Set the file or the directory of the style for the process.
     *
     * @param style the directory or the file where the style to apply is stored.
     * @return the current instance of the processing recipe builder.
     */
    public Builder style( final String style ) {
      this.style = style;
      return this;
    }

    /**
     * Set the verbosity of the process.
     *
     * @param verbose the verbosity {@code true} means the process will be verbose {@code false} indicates the
     *                process will be mute (no output at all).
     * @return the current instance of the processing recipe builder.
     */
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
      throw new InvalidProcessingRecipe( "all arguments must be defined" );
    }
  }
}
