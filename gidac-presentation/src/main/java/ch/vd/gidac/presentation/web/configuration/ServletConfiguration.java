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

import ch.vd.gidac.presentation.web.core.filters.LoggingContextEnricherFilter;
import ch.vd.gidac.presentation.web.core.filters.RequestIdFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;

/**
 * Configuration of the servlet's behavior.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class ServletConfiguration {
  private static final Logger log = LogManager.getLogger( ServletConfiguration.class );
  private final IdGenerator idGenerator;

  @Value("${application.context.request.requestIdHeader}")
  String requestIdHeaderName;

  public ServletConfiguration( final IdGenerator idGenerator ) {
    this.idGenerator = idGenerator;
  }

  /**
   * Create the filter to use to add the {@code x-request-id} header in the payload of the request.
   *
   * @return an instance of the filter.
   */
  @Bean
  FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
    log.trace( "Setting up the requestId filter" );
    final FilterRegistrationBean<RequestIdFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter( new RequestIdFilter( idGenerator, requestIdHeaderName ) );
    registrationBean.addUrlPatterns( "/*" );
    registrationBean.setOrder( 1 );
    log.trace( "Request id filter set up completed" );
    return registrationBean;
  }

  /**
   * Create the filter to let the system filling the logging context
   *
   * @return the bean definition
   */
  @Bean
  FilterRegistrationBean<LoggingContextEnricherFilter> loggingContextFilter() {
    log.trace( "Setting up the loggingContext filter" );
    final FilterRegistrationBean<LoggingContextEnricherFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter( new LoggingContextEnricherFilter( requestIdHeaderName ) );
    registrationBean.addUrlPatterns( "/*" );
    registrationBean.setOrder( 1 );
    log.trace( "loggingContext filter setup completed" );
    return registrationBean;
  }
}
