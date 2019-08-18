package com.github.aborg0.rxala.invariant

import java.util.Optional

import com.github.aborg0.rxala.{<:!<, AtMostOne, CanOnlyFail, Cardinality, CardinalityFlatMapResult, EmptyCompletion, ExactlyOne, Family, FlatMapIs, LowerBounded, UpperBounded}

/**
  *
  * @tparam R Environment
  * @tparam E Error
  * @tparam T Data
  * @tparam C Cardinality
  * @tparam F Implementation family
  */
trait Observable[R, E, T, C <: Cardinality, F <: Family] extends Any with FlowableSource[R, E, T, C, F] {
  def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality](f: java.util.function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, F]])(implicit flatMapBehaviour: FlatMapIs, cardinalityResult: CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, F]

  def map[Q](f: java.util.function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, F]

  def withFilter(keep: java.util.function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, F]

  /**
    * {{{
    *   import org.scalatest._
    *   import Matchers._
    *   println("Hi")
    *   true shouldBe false
    *   "???: Observable[Any, Any, String, Empty].keep(_ => true): Observable[Any, Any, String, Empty]" should Compile
    * }}}
    *
    * @param p                predicate for keeping the content
    * @param upperBoundedCard Upper bound on cardinality
    * @param notLowerBounded  Evidence for no lower bound on cardinality
    * @return The filtered result
    */
  def keep(p: java.util.function.Predicate[_ >: T])(implicit upperBoundedCard: C <:< UpperBounded, notLowerBounded: C <:!< LowerBounded): Observable[R, E, T, C, F]

  def keep(p: java.util.function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, F]
}

trait ObservableFactory[EBound, F <: Family] {
  def empty[R, E <: EBound, T]: Observable[R, _ <: E, T, _ <: EmptyCompletion, F]

  def fail[R, E <: EBound with Throwable, T](error: E): Observable[R, _ <: E, T, CanOnlyFail, F]

  def single[R, E <: EBound, T](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, F]

  def maybe[R, E <: EBound, T <: Any](o: Optional[T]): Observable[R, _ <: E, T, _ <: AtMostOne, F] = if (o.isPresent) {
    single[R, E, T](o.get)
  } else {
    empty[R, E, T]
  }
}