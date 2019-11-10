package com.github.aborg0.rxala.akka

import akka.actor.ClassicActorSystemProvider
import akka.stream.javadsl.{Sink, Source}
import akka.stream.{Graph, SinkShape}
import com.github.aborg0.rxala.invariant.wrapper.BackPressureSubscriberWrapper

class SubscriberAkka[R, E <: Throwable, T](private[rxala] override val subscriber: Akka.WrappedSubscriberType[R, E, T])(private val provider: ClassicActorSystemProvider)
  extends /*AnyVal with*/ BackPressureSubscriberWrapper[R, E, T, Akka.type] {
  override def onNext(t: T): Unit = Sink.fromGraph(subscriber).runWith(Source.single(t), provider)

  override def onComplete(): Unit = Sink.fromGraph(subscriber).runWith(Source.empty(), provider)

  override def onError(error: E): Unit = Sink.fromGraph(subscriber).runWith(Source.failed(error), provider)

  override def onSubscribe(subscription: Akka.SubscriptionType): Unit = () /*subscription*/
}
