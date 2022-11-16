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

package ch.vd.gidac.presentation.web.core.aspects.logging;

import ch.vd.gidac.presentation.web.core.aspects.SemanticAspectDefinition;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Define all aspects to use for controller execution.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@Aspect
@Component
public class RestControllerLoggingAspect extends SemanticAspectDefinition {

  /**
   * Log the beginning of the execution of a controller's method.
   *
   * @param joinPoint the join point activated.
   */
  @Before("controllerMethodExecution()")
  public void beforeExecuting( final JoinPoint joinPoint ) {
    final var log = getLogger( joinPoint );
    log.info( "Executing {} on controller {}", joinPoint.getSignature().getDeclaringType().getSimpleName(),
        joinPoint.getSignature().getDeclaringTypeName() );
  }

  /**
   * Log the end of the exectuion of a controller's method.
   *
   * @param joinPoint the join point activated.
   */
  @After("controllerMethodExecution()")
  public void afterExecuting( final JoinPoint joinPoint ) {
    final var log = getLogger( joinPoint );
    log.info( "{}#{} Execution ended.", joinPoint.getSignature().getDeclaringType().getSimpleName(),
        joinPoint.getSignature().getName() );
  }

  /**
   * Report an error for a method's controller execution.
   *
   * @param joinPoint the join point activated.
   * @param error     the error thrown by the process.
   */
  @AfterThrowing(pointcut = "controllerMethodExecution()", throwing = "error")
  public void afterThrowing( final JoinPoint joinPoint, Throwable error ) {
    final var log = getLogger( joinPoint );
    log.warn( "An error occurred during the process with message: {}", error.getMessage() );
  }
}
