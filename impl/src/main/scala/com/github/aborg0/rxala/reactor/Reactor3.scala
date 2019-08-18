package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.invariant.wrapper.WrapperFamily
import org.reactivestreams.Subscriber
import reactor.core.publisher.Flux

object Reactor3 extends WrapperFamily {
  override type WrappedObservableType[R, E, T] = Flux[T]
  override type WrappedSubscriberType[R, E, T] = Subscriber[T]
}
