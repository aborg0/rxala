package com.github.aborg0.rxala.reactor

import java.util.function
import java.util.function.Predicate

import com.github.aborg0.rxala
import com.github.aborg0.rxala.invariant.wrapper.{BackPressureObservableWrapper, ObservableWrapperFactory}
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{BackPressure, CanOnlyFail, Cardinality, Cold, EmptyCompletion, ExactlyOne, FlatMapIs, Temperature, WithBackPressure}
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

class ObservableReactor3[R, E, T, C <: Cardinality, H <: Temperature](private[rxala] val wrapped: Reactor3.WrappedObservableType[R, E, T]) extends AnyVal with BackPressureObservableWrapper[R, E, T, C, H, Reactor3.type] {

  import ObservableReactor3Factory.wrap

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality, HI <: Temperature](f: function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, WithBackPressure,_<: HI, Reactor3.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: rxala.CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, WithBackPressure, HI, Reactor3.type] = {
    val mapper: function.Function[_ >: T, _ <: Publisher[Q]] = (t: T) =>
      f(t).asInstanceOf[BackPressureObservableWrapper[RI, EI, Q, CI, HI, Reactor3.type]].wrapped
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap =>
        // Flux#switchMap is not generic enough, so casting
        ObservableReactor3Factory.wrap(wrapped.switchMap[Q](mapper.asInstanceOf[java.util.function.Function[_ >: T, Publisher[_ <: Q]]]))
      case FlatMapIs.MergeMap => ObservableReactor3Factory.wrap(wrapped.flatMap(mapper))
      case FlatMapIs.ConcatMap => ObservableReactor3Factory.wrap(wrapped.concatMap(mapper))
    }
  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, WithBackPressure, H, Reactor3.type] = wrap(wrapped.map(f))

  override def withFilter(keep: Predicate[_ >: T]): Observable[R, E, T, Cardinality, WithBackPressure, H, Reactor3.type] = wrap(wrapped.filter(keep))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T, Cardinality, WithBackPressure, H, Reactor3.type] = withFilter(p)

  override def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, WithBackPressure, Reactor3.type]): Unit = wrapped.subscribe(extract(s))
}

object ObservableReactor3Factory extends ObservableWrapperFactory[Any, Reactor3.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: Reactor3.WrappedObservableType[R, E, T]): Observable[R, E, T, C, WithBackPressure, H, Reactor3.type] = new ObservableReactor3[R, E, T, C, H](o)

  override def empty[R, E, T, H <: Temperature]: Observable[R, _ <: E, T, _ <: EmptyCompletion, WithBackPressure, H, Reactor3.type] = wrap(Flux.empty())

  override def fail[R, E <: Throwable, T, H <: Temperature](error: E): Observable[R, _ <: E, T, CanOnlyFail, WithBackPressure, H, Reactor3.type] = wrap(Flux.error(error))

  override def single[R, E, T, H <: Temperature](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, WithBackPressure, H, Reactor3.type] = wrap(Flux.just[T](t))
}