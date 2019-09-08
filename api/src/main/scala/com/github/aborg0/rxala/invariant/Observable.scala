package com.github.aborg0.rxala.invariant

import java.util.Optional

import com.github.aborg0.rxala.{<:!<, AtMostOne, BackPressure, CanOnlyFail, Cardinality, CardinalityFlatMapResult, EmptyCompletion, ExactlyOne, Family, FlatMapIs, LowerBounded, NoBackPressure, Temperature, UpperBounded, WithBackPressure}

/**
  *
  * @tparam R Environment
  * @tparam E Error
  * @tparam T Data
  * @tparam C Cardinality
  * @tparam B Back-pressure supported?
  * @tparam H Hot or cold?
  * @tparam F Implementation family
  */
trait Observable[R, E, T, C <: Cardinality, B <: BackPressure, H <: Temperature, F <: Family[B]] extends Any with ObservableSource[R, E, T, B,  F] {
  def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality, HI <: Temperature](f: java.util.function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, B, _<:HI, F]])(implicit flatMapBehaviour: FlatMapIs, cardinalityResult: CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, B, HI, F]

  def map[Q](f: java.util.function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, B, H, F]

  def withFilter(keep: java.util.function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, B, H, F]

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
  def keep(p: java.util.function.Predicate[_ >: T])(implicit upperBoundedCard: C <:< UpperBounded, notLowerBounded: C <:!< LowerBounded): Observable[R, E, T, C, B, H, F]

  def keep(p: java.util.function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, B, H, F]
}

trait ObservableFactory[EBound, B <: BackPressure, F <: Family[B]] {
  def empty[R, E <: EBound, T, H <: Temperature]: Observable[R, _ <: E, T, _ <: EmptyCompletion, B, H, F]

  def fail[R, E <: EBound with Throwable, T, H <: Temperature](error: E): Observable[R, _ <: E, T, CanOnlyFail, B, H, F]

  def single[R, E <: EBound, T, H <: Temperature](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, B, H, F]

  def maybe[R, E <: EBound, T <: Any, H <: Temperature](o: Optional[T]): Observable[R, _ <: E, T, _ <: AtMostOne, B, H, F] = if (o.isPresent) {
    single[R, E, T, H](o.get)
  } else {
    empty[R, E, T, H]
  }

}