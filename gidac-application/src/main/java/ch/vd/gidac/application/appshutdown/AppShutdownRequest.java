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

package ch.vd.gidac.application.appshutdown;

import ch.vd.gidac.domain.core.policies.NotEmptyStringPolicy;

public record AppShutdownRequest( String applicatinoName, String rootPath, boolean useNative ) {

  private static final NotEmptyStringPolicy policy = new NotEmptyStringPolicy();

  /**
   * Create a new instance of the application shutdown request.
   *
   * @param applicationName the name of the application
   * @param rootPath        the absolute path to the application.
   * @param useNative       indicates if the system is using the native API to manage temporary file system items
   *
   * @return a new instance of the application shutdown request
   *
   * @throws IllegalArgumentException thrown if the request cannot be created
   */
  public static AppShutdownRequest create (final String applicationName, final String rootPath, final boolean useNative) {
    if (policy.test( applicationName ) && ( useNative || policy.test( rootPath ) )) {
      return new AppShutdownRequest( applicationName, rootPath, useNative );
    }
    throw new IllegalArgumentException( "Unable to create the AppShutdownRequest" );
  }

  /**
   * Create a new instance of the application shutdown request.
   *
   * @param applicationName the name of the application.
   * @param rootPath        the root path to the application working directory.
   * @param useNative       indicates if the system is using the native API to manage temporary file system items
   *
   * @return a new instance of the application shutdown request
   *
   * @throws IllegalArgumentException thrown if the request cannot be created
   */
  public static AppShutdownRequest create (final String applicationName, final String rootPath, final String useNative) {
    return create( applicationName, rootPath, Boolean.getBoolean( useNative.trim() ) );
  }
}
