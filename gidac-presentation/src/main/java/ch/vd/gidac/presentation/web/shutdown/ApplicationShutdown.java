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

package ch.vd.gidac.presentation.web.shutdown;

import ch.vd.gidac.application.appshutdown.AppShutdownRequest;
import ch.vd.gidac.application.appshutdown.AppShutdownRequestHandler;
import ch.vd.gidac.presentation.web.core.hooks.BaseApplicationLifecycleHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Shutdown the application.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
@Component
public class ApplicationShutdown extends BaseApplicationLifecycleHook implements ApplicationListener<ContextClosedEvent> {

  private final AppShutdownRequestHandler appShutdownRequestHandler;

  private static final Logger log = LogManager.getLogger( ApplicationShutdown.class );

  public ApplicationShutdown (
      @Value("${application.name}") final String applicationName,
      @Value("${application.run.processing.fs-tree.tmp-dir}") String applicationRootPath,
      @Value("${application.run.processing.fs-tree.native-tmp}") String useNative,
      final AppShutdownRequestHandler appShutdownRequestHandler) {
    super( applicationName, applicationRootPath, useNative );
    this.appShutdownRequestHandler = appShutdownRequestHandler;
  }

  @Override
  public void onApplicationEvent (ContextClosedEvent event) {
    log.info( "Handling the application shutdown" );
    final var request = AppShutdownRequest.create( applicationName, applicationRootPath, useNative );
    final var response = appShutdownRequestHandler.handleRequest( request );
    if(response.succeed()) {
      log.info("Application successfully cleaned up");
    } else {
      log.info("Unable to cleanup the application working directory");
    }
  }
}
