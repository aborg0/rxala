package com.github.aborg0.rxala.akka

import akka.stream.javadsl.{Sink, Source}
import akka.stream.{Graph, SinkShape}
import com.github.aborg0.rxala.WithBackPressure
import com.github.aborg0.rxala.invariant.wrapper.WrapperFamily

object Akka extends WrapperFamily[WithBackPressure] {
  override type WrappedObservableType[R, E, T] = Source[T, R]
  override type WrappedSubscriberType[R, E, T] = Sink[T, R]
  override type SubscriptionType = Graph[SinkShape[Any], Nothing]
}
