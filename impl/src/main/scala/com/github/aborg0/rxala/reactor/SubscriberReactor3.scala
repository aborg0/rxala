package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.invariant.wrapper.BackPressureSubscriberWrapper

class SubscriberReactor3[R, E <: Throwable, T](override private[rxala] val obs: Reactor3.WrappedSubscriberType[R, E, T])
  extends BackPressureSubscriberWrapper[R, E, T, Reactor3.type] {

  override def onNext(t: T): Unit = obs.onNext(t)

  override def onComplete(): Unit = obs.onComplete()

  override def onError(error: E): Unit = obs.onError(error)

  override def onSubscribe(subscription: Reactor3.SubscriptionType): Unit = obs.onSubscribe(subscription)
}
