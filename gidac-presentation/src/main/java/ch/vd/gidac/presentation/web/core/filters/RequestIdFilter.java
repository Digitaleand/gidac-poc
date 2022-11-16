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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.IdGenerator;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter which defines the request id in request metadata.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class RequestIdFilter extends OncePerRequestFilter {
  private static final Logger log = LogManager.getLogger( RequestIdFilter.class );

  /**
   * Generator for id
   */
  private final IdGenerator idGenerator;

  /**
   * Name of the header where the correlation id stands in the request and in the response.
   */
  private final String requestHeader;

  /**
   * Create a new instance of the filter with the generator abe to generate unique id.
   *
   * @param idGenerator   the generator to use to generate request.
   * @param requestHeader the name of the header to fetch the correlation id into.
   */
  public RequestIdFilter( final IdGenerator idGenerator, final String requestHeader ) {
    this.idGenerator = idGenerator;
    this.requestHeader = requestHeader;
  }

  @Override
  protected void doFilterInternal( final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain filterChain )
      throws ServletException, IOException {
    log.trace( "Checking the presence of the request id header" );
    var requestId = request.getHeader( requestHeader );
    if ( StringUtils.isEmpty( requestId ) ) {
      log.trace( "No request id found, setting it" );
      requestId = idGenerator.generateId().toString();
      log.trace( "The request id will be " + requestId );
    } else {
      log.trace( "Request id found!" );
    }
    request.setAttribute( requestHeader, requestId );
    log.trace( "Assigning the request id to the response" );
    // Bad practice to mutate the original object.
    response.setHeader( requestHeader, requestId );

    log.trace( "Invoking the next step in the filter" );
    filterChain.doFilter( request, response );
  }
}
