package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.invariant.wrapper.BackPressureSubscriberWrapper

class SubscriberReactor3[R, E <: Throwable, T](override private[rxala] val subscriber: Reactor3.WrappedSubscriberType[R, E, T])
  extends BackPressureSubscriberWrapper[R, E, T, Reactor3.type] {

  override def onNext(t: T): Unit = subscriber.onNext(t)

  override def onComplete(): Unit = subscriber.onComplete()

  override def onError(error: E): Unit = subscriber.onError(error)

  override def onSubscribe(subscription: Reactor3.SubscriptionType): Unit = subscriber.onSubscribe(subscription)
}
