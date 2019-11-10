package com.github.aborg0.rxala.invariant.wrapper

import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{<:!<, Cardinality, LowerBounded, NoBackPressure, Temperature, UpperBounded}

trait NoBackPressureObservableWrapper[R, E, T, C <: Cardinality, H <: Temperature, F <: WrapperFamily[NoBackPressure]] extends Any with Observable[R, E, T, C, NoBackPressure, H, F] {
  private[rxala] def wrapped: F#WrappedObservableType[R, E, T]
//  private[rxala] def wrappedNoBackPressure[BB <: B =:= NoBackPressure]: F#WrappedNoBackPressureObservableType[R, E, T]

  private[rxala] def extract(s: Subscriber[_ <: R, _ >: E, _ >: T, NoBackPressure, F]): F#WrappedSubscriberType[R, E, T] = s.asInstanceOf[NoBackPressureSubscriberWrapper[R, E, T, F]].subscriber

  override def keep(p: Predicate[_ >: T])(implicit upperBoundedCard: C <:< UpperBounded, notLowerBounded: C <:!< LowerBounded): Observable[R, E, T, C, NoBackPressure, H, F] = withFilter(p).asInstanceOf[Observable[R, E, T, C, NoBackPressure, H, F]]
}

trait ObservableWrapperNoBackPressureFactory[EBound, F <: WrapperFamily[NoBackPressure]] extends com.github.aborg0.rxala.invariant.ObservableFactory[EBound, NoBackPressure, F] {
  private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: F#WrappedObservableType[R, E, T]): Observable[R, E, T, C, NoBackPressure, H, F]
//  private[rxala] def wrapNoBackPressure[R, E, T, C <: Cardinality, H <: Temperature](o: F#WrappedNoBackPressureObservableType[R, E, T]): Observable[R, E, T, C, NoBackPressure, H, F]
}
