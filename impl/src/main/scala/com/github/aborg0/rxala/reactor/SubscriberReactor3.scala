package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.invariant.wrapper.SubscriberWrapper

class SubscriberReactor3[R, E <: Throwable, T](override private[rxala] val obs: Reactor3.WrappedSubscriberType[R, E, T])
  extends SubscriberWrapper[R, E, T, Reactor3.type] {

  override def onNext(t: T): Unit = obs.onNext(t)

  override def onComplete(): Unit = obs.onComplete()

  override def onError(error: E): Unit = obs.onError(error)
}
