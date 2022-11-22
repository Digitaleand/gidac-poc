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

/**
 * Response for the application shutdown use case.
 *
 * @param request the request handled.
 * @param succeed the success status of the process {@code true} means success, {@code false} means failure.
 */
public record AppShutdownResponse( AppShutdownRequest request, boolean succeed ) {

  /**
   * Create a new instance of the response.
   *
   * @param request the request handled by the use case
   * @param succeed the success status of the process {@code true} for success, {@code false} far failure
   *
   * @return a new instance of response
   */
  public static AppShutdownResponse create (final AppShutdownRequest request, final boolean succeed) {
    return new AppShutdownResponse( request, succeed );
  }

  /**
   * Create a new instance of the response maked as failed.
   *
   * @param request the request handled by the use case.
   *
   * @return a new instance of the response.
   */
  public static AppShutdownResponse fail (final AppShutdownRequest request) {
    return create( request, false );
  }

  /**
   * Create a new instance of teh response marked as succeeded.
   *
   * @param request the request handled by the use case.
   *
   * @return a new instance of the response.
   */
  public static AppShutdownResponse succeed (final AppShutdownRequest request) {
    return create( request, true );
  }
}
