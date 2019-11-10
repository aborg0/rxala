package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.invariant.wrapper.BackPressureSubscriberWrapper
import org.reactivestreams.Subscription

class SubscriberRxJava3[R, E <: Throwable, T](private[rxala] override val subscriber: RxJava3BackPressure.WrappedSubscriberType[R, E, T])
  extends AnyVal with BackPressureSubscriberWrapper[R, E, T, RxJava3BackPressure.type] {
  override def onNext(t: T): Unit = subscriber.onNext(t)

  override def onComplete(): Unit = subscriber.onComplete()

  override def onError(error: E): Unit = subscriber.onError(error)

  override def onSubscribe(subscription: Subscription): Unit = subscriber.onSubscribe(subscription)
}
