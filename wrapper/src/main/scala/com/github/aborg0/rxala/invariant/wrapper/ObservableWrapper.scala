package com.github.aborg0.rxala.invariant.wrapper

import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{<:!<, Cardinality, LowerBounded, Temperature, UpperBounded, WithBackPressure}

trait BackPressureObservableWrapper[R, E, T, C <: Cardinality, H <: Temperature, F <: WrapperFamily[WithBackPressure]] extends Any with Observable[R, E, T, C, WithBackPressure, H, F] {
  private[rxala] def wrapped: F#WrappedObservableType[R, E, T]
//  private[rxala] def wrappedNoBackPressure[BB <: B =:= NoBackPressure]: F#WrappedNoBackPressureObservableType[R, E, T]

  private[rxala] def extract(s: Subscriber[_ <: R, _ >: E, _ >: T, WithBackPressure, F]): F#WrappedSubscriberType[R, E, T] = s.asInstanceOf[BackPressureSubscriberWrapper[R, E, T, F]].subscriber

  override def keep(p: Predicate[_ >: T])(implicit upperBoundedCard: C <:< UpperBounded, notLowerBounded: C <:!< LowerBounded): Observable[R, E, T, C, WithBackPressure, H, F] = withFilter(p).asInstanceOf[Observable[R, E, T, C, WithBackPressure, H, F]]
}

trait ObservableWrapperFactory[EBound, F <: WrapperFamily[WithBackPressure]] extends com.github.aborg0.rxala.invariant.ObservableFactory[EBound, WithBackPressure, F] {
  private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: F#WrappedObservableType[R, E, T]): Observable[R, E, T, C, WithBackPressure, H, F]
//  private[rxala] def wrapNoBackPressure[R, E, T, C <: Cardinality, H <: Temperature](o: F#WrappedNoBackPressureObservableType[R, E, T]): Observable[R, E, T, C, NoBackPressure, H, F]
}
