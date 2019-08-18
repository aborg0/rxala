package com.github.aborg0.rxala.reactor

import java.util.function
import java.util.function.Predicate

import com.github.aborg0.rxala
import com.github.aborg0.rxala.invariant.wrapper.{ObservableWrapper, ObservableWrapperFactory}
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{CanOnlyFail, Cardinality, EmptyCompletion, ExactlyOne, FlatMapIs}
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

class ObservableReactor3[R, E, T, C <: Cardinality](private[rxala] val wrapped: Reactor3.WrappedObservableType[R, E, T]) extends AnyVal with ObservableWrapper[R, E, T, C, Reactor3.type] {

  import ObservableReactor3Factory.wrap

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality](f: function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, Reactor3.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: rxala.CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, Reactor3.type] = {
    val mapper: function.Function[_ >: T, _ <: Publisher[Q]] = (t: T) =>
      f(t).asInstanceOf[ObservableWrapper[RI, EI, Q, CI, Reactor3.type]].wrapped
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap =>
        // Flux#switchMap is not generic enough, so casting
        ObservableReactor3Factory.wrap(wrapped.switchMap[Q](mapper.asInstanceOf[java.util.function.Function[_ >: T, Publisher[_ <: Q]]]))
      case FlatMapIs.MergeMap => ObservableReactor3Factory.wrap(wrapped.flatMap(mapper))
      case FlatMapIs.ConcatMap => ObservableReactor3Factory.wrap(wrapped.concatMap(mapper))
    }
  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, Reactor3.type] = wrap(wrapped.map(f))

  override def withFilter(keep: Predicate[_ >: T]): Observable[R, E, T, Cardinality, Reactor3.type] = wrap(wrapped.filter(keep))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T, Cardinality, Reactor3.type] = withFilter(p)

  override def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, Reactor3.type]): Unit = wrapped.subscribe(extract(s))
}

object ObservableReactor3Factory extends ObservableWrapperFactory[Any, Reactor3.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality](o: Reactor3.WrappedObservableType[R, E, T]): Observable[R, E, T, C, Reactor3.type] = new ObservableReactor3[R, E, T, C](o)

  override def empty[R, E, T]: Observable[R, _ <: E, T, _ <: EmptyCompletion, Reactor3.type] = wrap(Flux.empty())

  override def fail[R, E <: Throwable, T](error: E): Observable[R, _ <: E, T, CanOnlyFail, Reactor3.type] = wrap(Flux.error(error))

  override def single[R, E, T](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, Reactor3.type] = wrap(Flux.just[T](t))
}