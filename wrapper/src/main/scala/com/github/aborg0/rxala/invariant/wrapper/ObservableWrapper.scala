package com.github.aborg0.rxala.invariant.wrapper

import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{<:!<, Cardinality, LowerBounded, UpperBounded}

trait ObservableWrapper[R, E, T, C <: Cardinality, F <: WrapperFamily] extends Any with Observable[R, E, T, C, F] {
  private[rxala] def wrapped: F#WrappedObservableType[R, E, T]

  private[rxala] def extract(s: Subscriber[_ <: R, _ >: E, _ >: T, F]): F#WrappedSubscriberType[R, E, T] = s.asInstanceOf[SubscriberWrapper[R, E, T, F]].obs

  override def keep(p: Predicate[_ >: T])(implicit upperBoundedCard: C <:< UpperBounded, notLowerBounded: C <:!< LowerBounded): Observable[R, E, T, C, F] = withFilter(p).asInstanceOf[Observable[R, E, T, C, F]]
}

trait ObservableWrapperFactory[EBound, F <: WrapperFamily] extends com.github.aborg0.rxala.invariant.ObservableFactory[EBound, F] {
  private[rxala] def wrap[R, E, T, C <: Cardinality](o: F#WrappedObservableType[R, E, T]): Observable[R, E, T, C, F]
}
