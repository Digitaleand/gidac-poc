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

package ch.vd.gidac.domain.core.specifications;

import ch.vd.gidac.domain.core.policies.ExistingPathPolicy;
import ch.vd.gidac.domain.core.policies.NotEmptyStringPolicy;
import org.javatuples.Pair;

import java.nio.file.Path;

/**
 * Specification to create the application directory.
 *
 * <p>The configuration of the application indicates the path to the location where the application working directory
 * will be created. The name of the application is also the name of the working directory.</p>
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public class ApplicationDirectoryCreationSpecification implements Specification<Pair<String, Path>> {

  private final NotEmptyStringPolicy notEmptyStringPolicy = new NotEmptyStringPolicy();
  private final ExistingPathPolicy existingPathPolicy = new ExistingPathPolicy();

  @Override
  public boolean isSatisfiedBy (Pair<String, Path> payload) {
    return notEmptyStringPolicy.test( payload.getValue0() ) &&
        existingPathPolicy.test( payload.getValue1() );
  }
}
