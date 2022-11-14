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

package ch.vd.gidac.application.generatepdf;

import ch.vd.gidac.domain.core.PdfGenerationRecipe;
import ch.vd.gidac.domain.core.RequestId;
import ch.vd.gidac.domain.core.SimplePdfGenerationRecipeFactory;

public class DefaultGeneratePdfRequestHandler implements GeneratePdfRequestHandler {

  @Override
  public GeneratePdfResponse handleRequest (GeneratePdfRequest request) {
    PdfGenerationRecipe recipe = null;
    try {
      final var factory = new SimplePdfGenerationRecipeFactory();
      factory.requestId( RequestId.fromString( request.requestId() ) )
          .archive( request.archive() );
      if (!factory.canCreate()) {
        return new GeneratePdfResponse( request, null,
            new IllegalStateException( "The recipe cannot be created" ) );
      }
      recipe = factory.create()
          .setUp()
          .extract();
      if (!recipe.canProcess()) {
        return new GeneratePdfResponse( request, null,
            new IllegalStateException( "The recipe cannot be baked" ) );
      }
      final var binary = recipe.prepare()
          .bake()
          .pack()
          .getBinary();
      return new GeneratePdfResponse( request, binary, null );
    } catch (final Exception e) {
      return new GeneratePdfResponse( request, null, e );
    } finally {
      if (null != recipe) {
        try {
          recipe.cleanUp();
        } catch (final Exception ignore) {
          // ignoring error here since this is due to underlying implementation without any value for the client.
        }
      }
    }
  }
}
