package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.invariant.wrapper.BackPressureSubscriberWrapper
import org.reactivestreams.Subscription

class SubscriberRxJava3[R, E <: Throwable, T](private[rxala] override val obs: RxJava3BackPressure.WrappedSubscriberType[R, E, T])
  extends AnyVal with BackPressureSubscriberWrapper[R, E, T, RxJava3BackPressure.type] {
  override def onNext(t: T): Unit = obs.onNext(t)

  override def onComplete(): Unit = obs.onComplete()

  override def onError(error: E): Unit = obs.onError(error)

  override def onSubscribe(subscription: Subscription): Unit = obs.onSubscribe(subscription)
}
