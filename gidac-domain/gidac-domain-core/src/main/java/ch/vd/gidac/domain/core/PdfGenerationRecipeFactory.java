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

/**
 * Define the contract for a factory able to create {@link  ch.vd.gidac.domain.core.PdfGenerationRecipe}.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public interface PdfGenerationRecipeFactory {

  /**
   * Check if the content of the recipe can be created.
   *
   * @return {@code true} indicates that the recipe may be created, {@code false} indicates the recipe is not creatable.
   */
  boolean canCreate();

  /**
   * Create a new instance of the {@link PdfGenerationRecipe}.
   *
   * @return the new instance of the recipe.
   *
   * @throws RuntimeException raise if the recipe cannot be created.
   */
  PdfGenerationRecipe create();

  /**
   * Define the archive to use to create the recipe.
   *
   * @param archive the archive to use to craete the recipe.
   *
   * @return the current instance of the factory
   */
  PdfGenerationRecipeFactory archive( Archive archive );

  /**
   * Define the request id for which the process runs.
   *
   * @param requestId the request id of the process
   *
   * @return the current instance of the factory.
   */
  PdfGenerationRecipeFactory requestId( RequestId requestId );
}
