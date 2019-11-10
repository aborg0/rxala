package com.github.aborg0.rxala.rxjava3

import java.util.function
import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.wrapper.{BackPressureObservableWrapper, BackPressureSubscriberWrapper, ObservableWrapperFactory}
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{CanOnlyFail, Cardinality, CardinalityFlatMapResult, Cold, EmptyCompletion, ExactlyOne, FlatMapIs, Temperature, WithBackPressure}
import io.reactivex.rxjava3.core.Flowable

class ObservableRxJava3BackPressure[R, E, T, C <: Cardinality, H <: Temperature](private[rxala] override val wrapped: RxJava3BackPressure.WrappedObservableType[R, E, T])
  extends AnyVal with BackPressureObservableWrapper[R, E, T, C, H, RxJava3BackPressure.type] {

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality, HI <: Temperature](f: java.util.function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, WithBackPressure, _<: HI, RxJava3BackPressure.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, WithBackPressure, HI, RxJava3BackPressure.type] = {
    val mapper: io.reactivex.rxjava3.functions.Function[T, org.reactivestreams.Publisher[Q]] = func((t: T) => {
      f(t).asInstanceOf[BackPressureObservableWrapper[RI, EI, Q, cardinalityResult.C, HI, RxJava3BackPressure.type]].wrapped
    })
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap => ObservableRxJava3Factory.wrap(wrapped.switchMap(mapper))
      case FlatMapIs.MergeMap => ObservableRxJava3Factory.wrap(wrapped.flatMap(mapper))
      case FlatMapIs.ConcatMap => ObservableRxJava3Factory.wrap(wrapped.concatMap(mapper))
    }
  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, WithBackPressure, H, RxJava3BackPressure.type] = ObservableRxJava3Factory.wrap[R, E, Q, C, H](wrapped.map(func(f)))

  override def withFilter(keep: function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, WithBackPressure, H, RxJava3BackPressure.type] = ObservableRxJava3Factory.wrap(wrapped.filter(pred(keep)))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T,Cardinality,  WithBackPressure, H, RxJava3BackPressure.type] = withFilter(p)

  override def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, WithBackPressure, RxJava3BackPressure.type]): Unit = wrapped.subscribe(s.asInstanceOf[BackPressureSubscriberWrapper[R, E, T, RxJava3BackPressure.type]].subscriber)

}

object ObservableRxJava3Factory extends ObservableWrapperFactory[Any, RxJava3BackPressure.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: RxJava3BackPressure.WrappedObservableType[R, E, T]) =
    new ObservableRxJava3BackPressure[R, E, T, C, H](o)

  override def empty[R, E, T, H <: Temperature]: Observable[R, _ <: E, T, _ <: EmptyCompletion, WithBackPressure, H, RxJava3BackPressure.type] =
    wrap[R, E, T, EmptyCompletion, H](Flowable.empty())

  override def fail[R, E <: Throwable with Any, T, H <: Temperature](error: E): Observable[R, _ <: E, T, CanOnlyFail, WithBackPressure, H, RxJava3BackPressure.type] =
    wrap[R, E, T, CanOnlyFail, H](Flowable.error(error))

  override def single[R, E, T, H <: Temperature](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, WithBackPressure, H, RxJava3BackPressure.type] =
    wrap[R, E, T, ExactlyOne, H](Flowable.just(t))
}
