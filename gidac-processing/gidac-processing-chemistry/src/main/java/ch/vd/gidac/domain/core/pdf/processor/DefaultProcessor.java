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

package ch.vd.gidac.domain.core.pdf.processor;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class DefaultProcessor implements Processor {

  private static final Logger log = LogManager.getLogger( DefaultProcessor.class );

  private String getCommandString( final ProcessingRecipe processingRecipe ) {
    log.info("Generating command");

    final var command = new StringBuilder();
    command.append( processingRecipe.executable() )
        .append( " " )
        .append( "--input" )
        .append( "=" )
        .append( processingRecipe.ditaMap() )
        .append( " " )
        .append( "--format" )
        .append( "=" )
        .append( processingRecipe.format() )
        .append( " " )
        .append( "--output" )
        .append( "=" )
        .append( processingRecipe.outputDir() )
        .append( " " )
        .append( "--temp" )
        .append( "=" )
        .append( processingRecipe.tmpDir() );

    if ( StringUtils.isNotEmpty( processingRecipe.style() ) ) {
      command.append( " " )
          .append( "--args.css" )
          .append( "=" )
          .append( processingRecipe.style() );
    }

    if ( processingRecipe.verbose() ) {
      command.append( " " )
          .append( "-v" );
    }

    final var cmd = command.toString();

    log.trace("The command will be {}", cmd);

    return cmd;
  }

  @Override
  public void execute( final ProcessingRecipe processingRecipe ) {
    final var env = new HashMap<String, String>();
    env.put( "JAVA_HOME", "/home/mehdi/.sdkman/candidates/java/current" );

    try {
      final var command = getCommandString( processingRecipe );
      final var commandLine = CommandLine.parse( command );
      final var executor = new DefaultExecutor();
      executor.setExitValue( 0 );
      final var exitCode = executor.execute( commandLine, env );
      if ( exitCode != 0 ) {
        throw new RuntimeException( "The generation process fails with exit code " + exitCode );
      }
    } catch ( final Exception e ) {
      throw new RuntimeException( e );
    }
  }
}
