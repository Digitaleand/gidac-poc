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

package ch.vd.gidac.presentation.web.core.filters;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Define a way to enrich the logging context to add meta information inside logs.
 *
 * <p>Note: this approach won't work with an asynchronous way of implementing the behavior of the
 * api.</p>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class LoggingContextEnricherFilter extends OncePerRequestFilter {
  private static final Logger log = LogManager.getLogger( LoggingFailureAnalysisReporter.class );

  /**
   * Name of the header where the correlation id stands in the request and in the resposne.
   */
  private final String requestHeader;

  /**
   * Create a new instance of the filter and set header to read to fetch the correlation id.
   *
   * @param requestHeader the name of the header to fetch the correlation id into.
   */
  public LoggingContextEnricherFilter (final String requestHeader) {
    this.requestHeader = requestHeader;
  }

  @Override
  protected void doFilterInternal (final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain filterChain)
      throws ServletException, IOException {
    log.trace( "Enriching the loggign context with request meta information" );

    final var requestId = (String) request.getAttribute( requestHeader );
    try ( final CloseableThreadContext.Instance cti = CloseableThreadContext.put( "requestId",
        requestId ) ) {
      cti.put( "x-ml-test", "demo" );
      log.trace( "Request id defined in the context, process the next step in the chain" );
      filterChain.doFilter( request, response );
      log.trace( "Processing complete, Cleaning up the content of the request." );
    }
  }
}
