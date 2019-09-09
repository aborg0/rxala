package com.github.aborg0.rxala.rxjava3

import java.util.function
import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.wrapper.{NoBackPressureObservableWrapper, NoBackPressureSubscriberWrapper, ObservableWrapperFactory, ObservableWrapperNoBackPressureFactory}
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{CanOnlyFail, Cardinality, CardinalityFlatMapResult, EmptyCompletion, ExactlyOne, FlatMapIs, NoBackPressure, Temperature}

class ObservableRxJava3NoBackPressure[R, E, T, C <: Cardinality, H <: Temperature](private[rxala] override val wrapped: RxJava3NoBackPressure.WrappedObservableType[R, E, T])
  extends AnyVal with NoBackPressureObservableWrapper[R, E, T, C, H, RxJava3NoBackPressure.type] {

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality, HI <: Temperature](f: java.util.function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, NoBackPressure, _<: HI, RxJava3NoBackPressure.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, NoBackPressure, HI, RxJava3NoBackPressure.type] = {
    val mapper: io.reactivex.rxjava3.functions.Function[T, io.reactivex.rxjava3.core.ObservableSource[Q] ] = func((t: T) => {
      f(t).asInstanceOf[NoBackPressureObservableWrapper[RI, EI, Q, cardinalityResult.C, HI, RxJava3NoBackPressure.type]].wrapped
    })
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap => ObservableRxJava3NoBackPressureFactory.wrap(wrapped.switchMap(mapper))
      case FlatMapIs.MergeMap => ObservableRxJava3NoBackPressureFactory.wrap(wrapped.flatMap(mapper))
      case FlatMapIs.ConcatMap => ObservableRxJava3NoBackPressureFactory.wrap(wrapped.concatMap(mapper))
    }
  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, NoBackPressure, H, RxJava3NoBackPressure.type] = ObservableRxJava3NoBackPressureFactory.wrap[R, E, Q, C, H](wrapped.map(func(f)))

  override def withFilter(keep: function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, NoBackPressure, H, RxJava3NoBackPressure.type] = ObservableRxJava3NoBackPressureFactory.wrap(wrapped.filter(pred(keep)))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T,Cardinality,  NoBackPressure, H, RxJava3NoBackPressure.type] = withFilter(p)

  override def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, NoBackPressure, RxJava3NoBackPressure.type]): Unit = wrapped.subscribe(s.asInstanceOf[NoBackPressureSubscriberWrapper[R, E, T, RxJava3NoBackPressure.type]].obs)

}

object ObservableRxJava3NoBackPressureFactory extends ObservableWrapperNoBackPressureFactory[Any, RxJava3NoBackPressure.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: RxJava3NoBackPressure.WrappedObservableType[R, E, T]) =
    new ObservableRxJava3NoBackPressure[R, E, T, C, H](o)

  override def empty[R, E, T, H <: Temperature]: Observable[R, _ <: E, T, _ <: EmptyCompletion, NoBackPressure, H, RxJava3NoBackPressure.type] =
    wrap[R, E, T, EmptyCompletion, H](io.reactivex.rxjava3.core.Observable.empty())

  override def fail[R, E <: Throwable with Any, T, H <: Temperature](error: E): Observable[R, _ <: E, T, CanOnlyFail, NoBackPressure, H, RxJava3NoBackPressure.type] =
    wrap[R, E, T, CanOnlyFail, H](io.reactivex.rxjava3.core.Observable.error(error))

  override def single[R, E, T, H <: Temperature](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, NoBackPressure, H, RxJava3NoBackPressure.type] =
    wrap[R, E, T, ExactlyOne, H](io.reactivex.rxjava3.core.Observable.just(t))
}
