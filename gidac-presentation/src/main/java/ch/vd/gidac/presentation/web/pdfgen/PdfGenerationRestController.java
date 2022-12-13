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

package ch.vd.gidac.presentation.web.pdfgen;

import ch.vd.gidac.application.generatepdf.GeneratePdfRequest;
import ch.vd.gidac.application.generatepdf.GeneratePdfRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Rest controller for pdf generation.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@RestController()
@RequestMapping("/binaries")
public class PdfGenerationRestController {

  private static final Logger log = LogManager.getLogger(PdfGenerationRestController.class);

  /**
   * Archive factory to use to read information from the request.
   */
  private final ArchiveFactory archiveFactory;

  private final GeneratePdfRequestHandler requestHandler;

  /**
   * Default constructor of the controller
   */
  public PdfGenerationRestController( final GeneratePdfRequestHandler requestHandler ) {
    this.requestHandler = requestHandler;
    archiveFactory = new ArchiveFactory();
  }

  /**
   * Handle a file upload (archive in zip format for now) and return the binary generated.
   *
   * <p>Depending on the content of the zip, the system will return a raw binary (if only one pdf should be
   * generated) or a zip file if more than one binary should be generated. The content type is adapted based on the
   * content of the response.</p>
   *
   * @param file the zip file to handle to generate the pdf content for.
   *
   * @return the response entity with the content of the data generated as payload.
   */
  @PostMapping(
      consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE },
      produces = { MediaType.APPLICATION_PDF_VALUE, "application/zip" }
  )
  public ResponseEntity<byte[]> generatePdf(
      @RequestAttribute("x-request-id") final String requestId,
      @RequestPart("file") final MultipartFile file
  ) {
    try {
      final var archive = archiveFactory.toArchive( file );
      final var request = new GeneratePdfRequest( requestId, archive );
      final var response = requestHandler.handleRequest( request );
      final var headers = new HttpHeaders();
      if(null == response.binary()) {
        log.trace( "The binary is null. It indicates the process is in error" );
        log.info( response.exception().getMessage() );
        return ResponseEntity.badRequest().build();
      }
      headers.setContentType( MediaType.parseMediaType( response.binary().mimeType() ) );
      headers.setContentDispositionFormData( response.binary().name(), response.binary().name() );
      return new ResponseEntity<>( response.binary().payload(), headers, HttpStatus.OK );
    } catch ( final IOException e ) {
      return ResponseEntity.badRequest().build();
    }
  }
}
