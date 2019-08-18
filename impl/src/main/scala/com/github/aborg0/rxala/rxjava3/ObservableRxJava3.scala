package com.github.aborg0.rxala.rxjava3

import java.util.function
import java.util.function.Predicate

import com.github.aborg0.rxala.invariant.wrapper.{ObservableWrapper, ObservableWrapperFactory, SubscriberWrapper}
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{CanOnlyFail, Cardinality, CardinalityFlatMapResult, EmptyCompletion, ExactlyOne, FlatMapIs}
import io.reactivex.Flowable

class ObservableRxJava3[R, E, T, C <: Cardinality](private[rxala] override val wrapped: RxJava3.WrappedObservableType[R, E, T])
  extends AnyVal with ObservableWrapper[R, E, T, C, RxJava3.type] {

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality](f: java.util.function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, RxJava3.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, RxJava3.type] = {
    val mapper: io.reactivex.functions.Function[T, org.reactivestreams.Publisher[Q] /*io.reactivex.ObservableSource[Q]*/ ] = func((t: T) => {
      f(t).asInstanceOf[ObservableWrapper[RI, EI, Q, cardinalityResult.C, RxJava3.type]].wrapped
    })
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap => ObservableRxJava3Factory.wrap(wrapped.switchMap(mapper))
      case FlatMapIs.MergeMap => ObservableRxJava3Factory.wrap(wrapped.flatMap(mapper))
      case FlatMapIs.ConcatMap => ObservableRxJava3Factory.wrap(wrapped.concatMap(mapper))
    }
  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, RxJava3.type] = ObservableRxJava3Factory.wrap[R, E, Q, C](wrapped.map(func(f)))

  override def withFilter(keep: function.Predicate[_ >: T]): Observable[R, E, T, Cardinality, RxJava3.type] = ObservableRxJava3Factory.wrap(wrapped.filter(pred(keep)))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T, Cardinality, RxJava3.type] = withFilter(p)

  override def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, RxJava3.type]): Unit = wrapped.subscribe(s.asInstanceOf[SubscriberWrapper[R, E, T, RxJava3.type]].obs)
}

object ObservableRxJava3Factory extends ObservableWrapperFactory[Any, RxJava3.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality](o: RxJava3.WrappedObservableType[R, E, T]) =
    new ObservableRxJava3[R, E, T, C](o)

  override def empty[R, E, T]: Observable[R, _ <: E, T, _ <: EmptyCompletion, RxJava3.type] =
    wrap[R, E, T, EmptyCompletion](Flowable.empty())

  override def fail[R, E <: Throwable with Any, T](error: E): Observable[R, _ <: E, T, CanOnlyFail, RxJava3.type] =
    wrap[R, E, T, CanOnlyFail](Flowable.error(error))

  override def single[R, E, T](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, RxJava3.type] =
    wrap[R, E, T, ExactlyOne](Flowable.just(t))
}
