package com.github.aborg0.rxala.invariant.wrapper

import com.github.aborg0.rxala.invariant.Subscriber

import scala.language.higherKinds

trait SubscriberWrapper[R, E, T, F <: WrapperFamily] extends Any with Subscriber[R, E, T, F] {
  private[rxala] def obs: F#WrappedSubscriberType[R, E, T]
}
