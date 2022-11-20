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

package ch.vd.gidac.application.appinit;

import ch.vd.gidac.domain.core.policies.ExistingPathPolicy;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Response for the application initialization process.
 *
 * @param request             the request sent by the client to the use case
 * @param appWorkingDirectory the application working directory created during the initialization process.
 */
public record AppInitResponse( AppInitRequest request, Path appWorkingDirectory ) {

  /**
   * Create a new instance of the response.
   *
   * <p>Since the response is always generated from the application right after processing with the domain, the
   * {@code appWorkingDirectory} should always be valid. However, here, we have a check just to ensure domain consistency
   * accross boundaries.</p>
   *
   * @param request             the request to use to create the response
   * @param appWorkingDirectory the instance of the application working directory.
   *
   * @return an instance of the application response.
   */
  public static AppInitResponse create (final AppInitRequest request, Path appWorkingDirectory) {
    // the control of the path consistency is not necessary here. We keep it for consistency reason but this should
    /// be review during the development of the production ready application.
    final var policy = new ExistingPathPolicy();
    if (Objects.nonNull( request ) && policy.test( appWorkingDirectory )) {
      return new AppInitResponse( request, appWorkingDirectory );
    }
    throw new IllegalArgumentException( "Request or path invalid, the appInitResponse cannot be created" );
  }
}
