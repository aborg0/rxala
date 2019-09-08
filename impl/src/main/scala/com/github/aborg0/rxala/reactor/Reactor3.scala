package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.WithBackPressure
import com.github.aborg0.rxala.invariant.wrapper.WrapperFamily
import org.reactivestreams.{Subscriber, Subscription}
import reactor.core.publisher.Flux

object Reactor3 extends WrapperFamily[WithBackPressure] {
  override type WrappedObservableType[R, E, T] = Flux[T]
  override type WrappedSubscriberType[R, E, T] = Subscriber[T]
//  override type WrappedSubscriptionType[R, E, T] = Subscription
  override type SubscriptionType = Subscription
}
