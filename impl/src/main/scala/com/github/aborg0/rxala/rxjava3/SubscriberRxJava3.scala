package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.invariant.wrapper.SubscriberWrapper

class SubscriberRxJava3[R, E <: Throwable, T](private[rxala] override val obs: RxJava3.WrappedSubscriberType[R, E, T])
  extends AnyVal with SubscriberWrapper[R, E, T, RxJava3.type] {
  override def onNext(t: T): Unit = obs.onNext(t)

  override def onComplete(): Unit = obs.onComplete()

  override def onError(error: E): Unit = obs.onError(error)
}
