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

import ch.vd.gidac.domain.core.compress.DefaultZipManager;
import ch.vd.gidac.domain.core.compress.ZipManager;
import ch.vd.gidac.domain.core.pdf.PdfGenerator;
import ch.vd.gidac.domain.core.specifications.IsProcessableArchiveSpecification;
import ch.vd.gidac.domain.core.specifications.Specification;
import ch.vd.gidac.domain.manifest.Item;
import ch.vd.gidac.domain.manifest.ManifestDecorator;
import ch.vd.gidac.domain.manifest.ManifestUnmarshaller;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate and root entity which defines the processing of an archive to generate a pdf.
 *
 * @author Mehdi Lefebvre
 * @version 0.0.1
 * @since 0.0.1
 */
public class PdfGenerationRecipe {

  /**
   * The unique id of the request for which we are generating the binary
   */
  private final RequestId requestId;

  /**
   * The archive to process to generate binary
   */
  private final Archive archive;

  /**
   * The result of the process.
   */
  private Binary binary;

  /**
   * Reader for the manifest
   */
  private ManifestDecorator reader;

  /**
   * List of dita maps processed during the transformation.
   */
  private final List<DitaMap> ditaMaps = new ArrayList<>();

  /**
   * Reference to the working directory for the processing.
   */
  private WorkingDirectory workingDirectory;

  /**
   * Manager to use during the process to interact with zip items
   */
  private final ZipManager zipManager;

  /**
   * This is the specification to use to check if the archive is processable.
   */
  private final Specification<Path> processableSpecification;

  /**
   * Create a new recipe to generate a pdf.
   *
   * <p>A recipe can only be generated by a factory.</p>
   *
   * @param requestId the unique id of the process for which we are generating the binary.
   * @param archive   the archive to use to read info to generate the binary.
   */
  PdfGenerationRecipe (final RequestId requestId, final Archive archive) {
    this.requestId = requestId;
    this.archive = archive;
    zipManager = new DefaultZipManager();
    processableSpecification = new IsProcessableArchiveSpecification();
  }

  /**
   * Get the request id which represents the unique id of the generation process.
   *
   * @return the request id
   */
  public RequestId getRequestId () {
    return requestId;
  }

  /**
   * Get the binary generated during the process.
   *
   * <p>It is not possible to invoke this method before processing the recipe. In such case a
   * {@link RuntimeException} will be thrown.</p>
   *
   * @return the binary generated during the process
   *
   * @throws RuntimeException if the binary has not been generated at the moment of the call.
   */
  public Binary getBinary () {
    if (null == binary) {
      throw new NoBinaryGeneratedException( "The recipe has not been baked" );
    }
    return binary;
  }

  /**
   * Get the archive on which the system is processing.
   *
   * @return the archive
   */
  public Archive getArchive () {
    return archive;
  }

  /**
   * This method prepares the host to receive the process (temporary files, locks, ...). It also set up all resources
   * invoked the process.
   *
   * @return the current instance of the recipe
   *
   * @throws RuntimeException if anything goes wrong during the process.
   */
  public PdfGenerationRecipe setUp () {
    try {
      workingDirectory = WorkingDirectory.create( requestId );
      workingDirectory.lock();
      return this;
    } catch (final IOException ioException) {
      throw new RuntimeException( ioException );
    }
  }

  /**
   * Effectively extract the archive into the working directory without doing anything else.
   *
   * @return the current instance of the recipe
   *
   * @throws RuntimeException if something goes wrong during the process.
   */
  public PdfGenerationRecipe extract () throws IOException {
    workingDirectory.markDirty();
    zipManager.unzip( archive.bytes(), workingDirectory.inputDirectory() );
    return this;
  }

  /**
   * Initialize the manifest, all styles, generators and so on.
   *
   * @return the current instance of the recipe
   *
   * @throws RuntimeException if anything goes wrong during the process.
   */
  public PdfGenerationRecipe prepare () {
    final var unmarshaller = new ManifestUnmarshaller();
    try (final var inputStream =
             new FileInputStream( workingDirectory.getManifestFile() )) {

      final var manifest = unmarshaller.unmarshall( inputStream, false );
      reader = new ManifestDecorator( manifest );

      final var ditaStream = reader.getItems()
          .stream()
          .map( Item::getDitamap )
          .map( Paths::get )
          .map( DitaMap::fromPath );

      ditaMaps.addAll( ditaStream.toList() );
    } catch (IOException exception) {
      throw new PdfRecipePreparationException( exception );
    }
    return this;
  }

  /**
   * Effective processing of the recipe.
   *
   * <p>If there is more than one file to bake, all files will be processed one by one.</p>
   * <p>For now, the process is sequential, it must be parallelized when we go on production.</p>
   *
   * @return the current instance of the recipe
   *
   */
  public PdfGenerationRecipe bake (final PdfGenerator pdfGenerator) {
    ditaMaps.forEach( ditaMap -> pdfGenerator.generatePdf( workingDirectory, ditaMap ) );
    return this;
  }

  public PdfGenerationRecipe tearDown () throws IOException {
    workingDirectory.markClean();
    workingDirectory.unlock();
    ;
    return this;
  }

  private void createBinaryFromPath (final Path pdf) throws IOException {
    final var name = FilenameUtils.getName( pdf.toString() );
    final var mime = "application/pdf"; // this should be adapted from the format
    try (final var fis = new FileInputStream( pdf.toFile() )) {
      final var content = fis.readAllBytes();
      binary = Binary.create( mime, name, content );
    }
  }

  private void createBinaryFromOutput () throws IOException {
    final var zip = zipManager.zip( workingDirectory.outputDirectory() );
    final var name = zip.getName();
    final var mime = "application/pdf";
    try (final var fis = new FileInputStream( zip )) {
      final var content = fis.readAllBytes();
      binary = Binary.create( mime, name, content );
    }
  }

  /**
   * Takes in charge the strategy to create the binary according to the content of the recipe.
   *
   * @return the current instance of the recipe
   *
   * @throws RuntimeException thrown if something goes wrong during the process.
   */
  public PdfGenerationRecipe pack () throws IOException {
    final var outputFiles = workingDirectory.listOutputFiles();
    if (outputFiles.size() == 1) {
      createBinaryFromPath( outputFiles.get( 0 ) );
    } else {
      createBinaryFromOutput();
    }
    return this;
  }

  /**
   * Check  if the recipe can be process.
   *
   * <p>This call MUST be made after extracting the archive since it checks if the content of the archive is
   * correct.</p>
   *
   * @return {@code true} if the recipe can be baked or {@code false} otherwise.
   */
  public boolean canProcess () {
    return processableSpecification.isSatisfiedBy( workingDirectory.inputDirectory() );
  }

  /**
   * Clean up all resources used during the process in order to avoid to let the waste growing on the host system.
   *
   * @return the current instance of the recipe
   *
   * @throws RuntimeException may occur if something goes wrong during the process.
   */
  public PdfGenerationRecipe cleanUp () throws IOException {
    workingDirectory.cleanup();
    return this;
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final var recipe = (PdfGenerationRecipe) o;

    return new EqualsBuilder()
        .append( requestId, recipe.requestId )
        .append( archive, recipe.archive )
        .append( binary, recipe.binary )
        .isEquals();
  }

  @Override
  public int hashCode () {
    return new HashCodeBuilder( 17, 37 )
        .append( requestId )
        .append( archive )
        .append( binary )
        .toHashCode();
  }

  @Override
  public String toString () {
    return "PdfGenerationRecipe{" +
        "requestId=" + requestId.value().toString() +
        ", archive=" + archive.originalName() +
        ", binary=" + binary.name() +
        '}';
  }
}
