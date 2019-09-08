package com.github.aborg0.rxala.invariant

import com.github.aborg0.rxala.{BackPressure, Family}

trait Subscriber[R, E, T, B <: BackPressure, F <: Family[B]] extends Any {
  def onNext(t: T): Unit

  def onComplete(): Unit

  def onError(error: E): Unit

  def onSubscribe(subscription: F#SubscriptionType): Unit
}
