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

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class IsProcessableArchiveSpecificationTest {

    @Test
    void checkProcessable() throws URISyntaxException {
        // GIVEN
        final var url = getClass().getClassLoader().getResource("processing");
        assertNotNull(url);
        final var path = Paths.get(url.toURI());

        // WHEN
        final var specification = new IsProcessableArchiveSpecification();
        final var result = specification.isSatisfiedBy(path);

        // THEN
        assertTrue(result);
    }

    @Test
    void checkNonProcessable() throws URISyntaxException {
        // GIVEN
        final var url = getClass().getClassLoader().getResource("ditas");
        assertNotNull(url);
        final var path = Paths.get(url.toURI());

        // WHEN
        final var specification = new IsProcessableArchiveSpecification();
        final var result = specification.isSatisfiedBy(path);

        // THEN
        assertFalse(result);
    }
}