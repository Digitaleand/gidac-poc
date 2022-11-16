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

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Default implementation of the zip manager.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultZipManager implements ZipManager {
    @Override
    public void zip(Path path) {

    }

    @Override
    public void zip(File file) {

    }

    private void extract(final Path path, final ZipArchiveInputStream za) throws IOException {
        ZipEntry zipEntry;
        while ((zipEntry = za.getNextZipEntry()) != null) {
            final var file = path.resolve(zipEntry.getName());
            if (zipEntry.isDirectory()) {
                Files.createDirectories(file);
            } else {
                try (final var fos = new FileOutputStream(file.toFile())) {
                    IOUtils.copy(za, fos);
                }
            }
        }
    }

    @Override
    public void unzip(byte[] content, Path path) throws IOException {
        try(final var za = new ZipArchiveInputStream(new ByteArrayInputStream(content))) {
            extract(path, za);
        }
    }

    @Override
    public void unzip(final File zipFile, final Path path) throws IOException {
        try (final var za = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            extract(path, za);
        }
    }
}
