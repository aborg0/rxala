package com.github.aborg0.rxala.akka

import java.util.function
import java.util.function.Predicate

import akka.NotUsed
import akka.stream.{Graph, Materializer, SourceShape}
import akka.stream.javadsl.Source
import com.github.aborg0.rxala
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.invariant.wrapper.{BackPressureObservableWrapper, ObservableWrapperFactory}
import com.github.aborg0.rxala.{CanOnlyFail, Cardinality, EmptyCompletion, ExactlyOne, FlatMapIs, Temperature, WithBackPressure}

class ObservableAkka[R, E, T, C <: Cardinality, H <: Temperature](private[rxala] val wrapped: Akka.WrappedObservableType[R, E, T]) extends AnyVal with BackPressureObservableWrapper[R, E, T, C, H, Akka.type] {
//  import com.github.aborg0.rxala.akka._
  import ObservableAkkaFactory.wrap

  override def flatMap[RI <: R, EI >: E, Q, CI <: Cardinality, HI <: Temperature](
                                                                                   f: function.Function[_ >: T, Observable[_ >: RI, _ <: EI, _ <: Q, CI, WithBackPressure, _ <: HI, Akka.type]])(
    implicit flatMapBehaviour: FlatMapIs,
    cardinalityResult: rxala.CardinalityFlatMapResult[C, CI]): Observable[RI, EI, Q, cardinalityResult.C, WithBackPressure, HI, Akka.type] = {
    val mapper: akka.japi.function.Function[_ >: T, _ <: Graph[SourceShape[Q], RI]] = (t: T) =>
      f(t).asInstanceOf[BackPressureObservableWrapper[RI, EI, Q, CI, HI, Akka.type]].wrapped
    flatMapBehaviour match {
      case FlatMapIs.SwitchMap =>
        // flatMapLatest is not implemented yet: https://github.com/akka/akka-stream-contrib/issues/114
//        ObservableAkkaFactory.wrap(wrapped.mergeLatest[Q](mapper, false))
        ???
      case FlatMapIs.MergeMap => ObservableAkkaFactory.wrap(wrapped.asInstanceOf[Source[T, RI]].flatMapMerge[Q, RI](2, mapper))
      case FlatMapIs.ConcatMap => ObservableAkkaFactory.wrap(wrapped.asInstanceOf[Source[T, RI]].flatMapConcat[Q,RI](mapper))
    }

  }

  override def map[Q](f: function.Function[_ >: T, _ <: Q]): Observable[R, E, Q, C, WithBackPressure, H, Akka.type] = wrap(wrapped.map(func(f)))

  override def withFilter(keep: Predicate[_ >: T]): Observable[R, E, T, Cardinality, WithBackPressure, H, Akka.type] = wrap(wrapped.filter(pred(keep)))

  override def keep(p: Predicate[_ >: T]): Observable[R, E, T, Cardinality, WithBackPressure, H, Akka.type] = withFilter(p)

  override def subscribe(observer: Subscriber[_ <: R, _ >: E, _ >: T, WithBackPressure, Akka.type]): Unit = ??? //Source.asSubscriber().toMat(extract(observer), (a, b) => a).run(??? : Materializer)
}

object ObservableAkkaFactory extends ObservableWrapperFactory[Any, Akka.type] {
  override private[rxala] def wrap[R, E, T, C <: Cardinality, H <: Temperature](o: Source[T, R]) = new ObservableAkka[R, E, T, C, H](o)

  override def empty[R, E <: Any, T, H <: Temperature]: Observable[R, _ <: E, T, _ <: EmptyCompletion, WithBackPressure, H, Akka.type] = wrap[R, E, T, EmptyCompletion,H](Source.empty[T]().asInstanceOf[Source[T, R]])

  override def fail[R, E <: Any with Throwable, T, H <: Temperature](error: E): Observable[R, _ <: E, T, CanOnlyFail, WithBackPressure, H, Akka.type] = wrap(Source.failed[T](error).asInstanceOf[Source[T, R]])

  override def single[R, E <: Any, T, H <: Temperature](t: T): Observable[R, _ <: E, T, _ <: ExactlyOne, WithBackPressure, H, Akka.type] = wrap(Source.single(t).asInstanceOf[Source[T, R]])
}