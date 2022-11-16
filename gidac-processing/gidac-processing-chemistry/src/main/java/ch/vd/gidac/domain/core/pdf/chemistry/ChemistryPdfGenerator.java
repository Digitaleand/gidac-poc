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
import ch.vd.gidac.domain.core.pdf.PdfGenerator;
import ch.vd.gidac.domain.core.pdf.processor.Processor;

/**
 * Default implementation of the pdf generator.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class ChemistryPdfGenerator implements PdfGenerator {

  private final Processor processor;

  /**
   * Create an instance of the generator with a given processor.
   *
   * @param processor the processor to use to send information to the Process.
   */
  public ChemistryPdfGenerator( final Processor processor ) {
    this.processor = processor;
  }

  @Override
  public void generatePdf( WorkingDirectory directory, DitaMap ditaMap ) {

  }
}
