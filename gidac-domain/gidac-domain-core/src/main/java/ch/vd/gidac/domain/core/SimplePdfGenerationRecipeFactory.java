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

package ch.vd.gidac.domain.core;

import ch.vd.gidac.domain.core.policies.ArchiveValidPolicy;
import ch.vd.gidac.domain.core.policies.RequestIdPolicy;

/**
 * Simple implementation of the recipe factory.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class SimplePdfGenerationRecipeFactory implements PdfGenerationRecipeFactory {

  private final ArchiveValidPolicy archiveValidPolicy = new ArchiveValidPolicy();
  private final RequestIdPolicy requestIdPolicy = new RequestIdPolicy();

  private Archive archive;

  private RequestId requestId;

  @Override
  public boolean canCreate() {
    return archiveValidPolicy.test( archive ) && requestIdPolicy.test( requestId );
  }

  @Override
  public PdfGenerationRecipe create() {
    if ( !canCreate() ) {
      throw new IllegalStateException( "The recipe cannot be generate with the currently defined state" );
    }
    return new PdfGenerationRecipe( requestId, archive );
  }

  @Override
  public PdfGenerationRecipeFactory archive( Archive archive ) {
    this.archive = archive;
    return this;
  }

  @Override
  public PdfGenerationRecipeFactory requestId( RequestId requestId ) {
    this.requestId = requestId;
    return this;
  }
}
