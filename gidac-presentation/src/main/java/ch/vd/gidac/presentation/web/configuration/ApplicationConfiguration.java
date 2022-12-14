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

package ch.vd.gidac.presentation.web.configuration;

import ch.vd.gidac.application.appinit.AppInitRequestHandler;
import ch.vd.gidac.application.appinit.DefaultAppInitRequestHandler;
import ch.vd.gidac.application.appshutdown.AppShutdownRequestHandler;
import ch.vd.gidac.application.appshutdown.DefaultAppShutdownRequestHandler;
import ch.vd.gidac.application.generatepdf.DefaultGeneratePdfRequestHandler;
import ch.vd.gidac.application.generatepdf.GeneratePdfRequestHandler;
import ch.vd.gidac.domain.core.pdf.PdfGenerator;
import ch.vd.gidac.domain.core.pdf.chemistry.ChemistryPdfGenerator;
import ch.vd.gidac.domain.core.pdf.processor.DefaultProcessor;
import ch.vd.gidac.domain.core.pdf.processor.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the application layer and all dependent item.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class ApplicationConfiguration {

  @Value( "${application.run.processing.dita.toolkit-path}" )
  private String ditaToolkotPath;

  @Bean
  Processor processor() {
    return new DefaultProcessor();
  }

  @Bean
  PdfGenerator pdfGenerator() {
    return new ChemistryPdfGenerator(processor(), ditaToolkotPath);
  }

  /**
   * Defines the pdf generation handler to use in the application.
   *
   * @return the instance of the request handler.
   */
  @Bean
  GeneratePdfRequestHandler generatePdfRequestHandler() {
    return new DefaultGeneratePdfRequestHandler( pdfGenerator() );
  }

  @Bean
  AppInitRequestHandler appInitRequestHandler() {
    return new DefaultAppInitRequestHandler();
  }

  @Bean
  AppShutdownRequestHandler appShutdownRequestHandler() {
    return new DefaultAppShutdownRequestHandler();
  }
}
