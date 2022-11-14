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

package ch.vd.gidac.presentation.web.core.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Define all semantic naming convention used in our stack and create method to request some part of
 * the application.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@Aspect
@Component
public abstract class SemanticAspectDefinition {

  /**
   * Define the execution of a domain method.
   */
  @Pointcut("execution(* ch.vd.gidac.domain.*.*(..))")
  public void domainMethodExecution () {}

  /**
   * Define the execution of a controller method or anything placed in the web
   * package of the presentation or in any of its children.
   */
  @Pointcut("execution(* ch.vd.gidac.presentation.web.*.*(..)) || execution(* ch.vd.gidac.presentation.web.pdfgen.*.*(..))")
  public void controllerMethodExecution () {}

  /**
   * Defines the execution of any method of any class in the application layer.
   */
  @Pointcut("execution(* ch.vd.gidac.application.*.*(..))")
  public void applicationLayerMethodExecution () {}

  /**
   * Defines the execution of any method of any request handler.
   */
  @Pointcut("execution(* ch.vd.gidac.application.*.*.handleRequest(..))")
  public void requestHandlerLayerMethodExecution () {}

  /**
   * Fetch the logger for the given joinpoint.
   *
   * @param joinPoint the joinpoint to fetch logger for
   *
   * @return the instance of the logger associate to the class under process.
   */
  protected Logger getLogger (final JoinPoint joinPoint) {
    return LogManager.getLogger( joinPoint.getSignature().getDeclaringType() );
  }


  /**
   * Measure the time elapsed for a method execution.
   *
   * @param joinPoint the join point activated.
   *
   * @return the result of the process.
   */
  @Around("controllerMethodExecution() || requestHandlerLayerMethodExecution()")
  public Object aroundExecution (final ProceedingJoinPoint joinPoint) {
    final var log = getLogger( joinPoint );
    final var start = Instant.now();
    try {
      final var response = joinPoint.proceed();
      final var end = Instant.now();
      final var duration = Duration.between( start, end ).toMillis();
      log.info( "The execution of the {}#{} takes {}ms",
          joinPoint.getSignature().getDeclaringType().getSimpleName(),
          joinPoint.getSignature().getName(),
          duration );
      return response;
    } catch (Throwable e) {
      log.warn( "An error occurred during use case execution with message {}", e.getMessage() );
      throw new RuntimeException( e );
    }
  }
}
