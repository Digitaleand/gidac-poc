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

package ch.vd.gidac.domain.core.compress;

import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultZipManagerTest {

    private static final String ARCHIVE = "archives/flowers.zip";

    private Path root;
    private Path workingDirectory;

    @BeforeEach
    void prepareWorkingDirectory() {
        root = Paths.get("/tmp/gidac", UUID.randomUUID().toString());
        workingDirectory = root.resolve("output");
    }

    @AfterEach
    void cleanupWorkingDirectory() throws IOException {
        PathUtils.deleteDirectory(root);
    }

    @Test
    void unzipSuccess() throws IOException, URISyntaxException {
        // GIVEN
        final var url = getClass().getClassLoader().getResource(ARCHIVE);
        assertNotNull(url);
        final var uri = url.toURI();
        final var file = new File(uri);
        assertNotNull(file);


        final var dir = Files.createDirectories(root);
        final var archive = Paths.get(uri);
        assertNotNull(archive);

        // WHEN
        final var zipManager = new DefaultZipManager();
        zipManager.unzip(archive.toFile(), dir);

        // THEN
        final var files = dir.toFile().listFiles();
        assertNotEquals(0, files.length);
    }

    @Test
    void unzipByteArray() throws IOException {
        final var zipManager = new DefaultZipManager();
        // GIVEN
        try(final var inputStream = getClass().getClassLoader().getResourceAsStream(ARCHIVE)) {
            assert inputStream != null;
            final var content = inputStream.readAllBytes();
            final var dir = Files.createDirectories(root);
            zipManager.unzip(content, dir);

            // THEN
            final var files = dir.toFile().listFiles();
            assertNotEquals(0, files.length);
        }
    }
}