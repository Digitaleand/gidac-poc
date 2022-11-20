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

package ch.vd.gidac.presentation.web.startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger log = LogManager.getLogger( Bootstrap.class );

  private final String applicationName;

  private final String applicationRootPath;

  private final boolean useNative;

  public Bootstrap (
      @Value("${application.name}") final String applicationName,
      @Value("${application.processing.fs-tree.tmp-dir") String applicationRootPath,
      @Value("${application.processing.fs-tree.native-tmp") String useNative) {
    this.applicationName = applicationName;
    this.applicationRootPath = applicationRootPath;
    this.useNative = Boolean.getBoolean( useNative );
  }

  @Override
  public void onApplicationEvent (final ContextRefreshedEvent event) {
    log.info( "Handling application startup, preparing the application context" );
    log.debug( "The configuration will use {} as application name, {} as application root working directory and {} use the native  temporary directory",
        applicationName, applicationRootPath, useNative ? "will" : "won't" );
  }
}
