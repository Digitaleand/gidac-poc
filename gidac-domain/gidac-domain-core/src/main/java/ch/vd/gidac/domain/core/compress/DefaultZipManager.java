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
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.ZipEntry;

/**
 * Default implementation of the zip manager.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultZipManager implements ZipManager {
  @Override
  public File zip( final Path path ) throws IOException {
    final var filename = path.resolve( UUID.randomUUID() + ".zip" );
    try ( final var os = Files.newOutputStream( filename );
          final var as = new ZipArchiveOutputStream( os ) ) {
      try ( final var s = Files.list( path ) ) {
        s.map( Path::toFile ).forEach( f -> {
          try {
            final var entry = as.createArchiveEntry( f, f.getName() );
            as.putArchiveEntry( entry );

            if ( f.isFile() ) {
              try ( final var fis = Files.newInputStream( f.toPath() ) ) {
                IOUtils.copy( fis, as );
              }
            }
            as.closeArchiveEntry();
          } catch ( IOException e ) {
            throw new RuntimeException( e );
          }
        } );
      }
      as.finish();
      return filename.toFile();
    }
  }

  private void extract( final Path path, final ZipArchiveInputStream za ) throws IOException {
    ZipEntry zipEntry;
    while ( ( zipEntry = za.getNextZipEntry() ) != null ) {
      final var file = path.resolve( zipEntry.getName() );
      if ( zipEntry.isDirectory() ) {
        Files.createDirectories( file );
      } else {
        try ( final var fos = new FileOutputStream( file.toFile() ) ) {
          IOUtils.copy( za, fos );
        }
      }
    }
  }

  @Override
  public void unzip( byte[] content, Path path ) throws IOException {
    try ( final var za = new ZipArchiveInputStream( new ByteArrayInputStream( content ) ) ) {
      extract( path, za );
    }
  }

  @Override
  public void unzip( final File zipFile, final Path path ) throws IOException {
    try ( final var za = new ZipArchiveInputStream( new BufferedInputStream( new FileInputStream( zipFile ) ) ) ) {
      extract( path, za );
    }
  }
}
