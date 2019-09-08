package com.github.aborg0.rxala.invariant.wrapper

import com.github.aborg0.rxala.invariant.Subscriber
import com.github.aborg0.rxala.{NoBackPressure, WithBackPressure}

import scala.language.higherKinds

trait BackPressureSubscriberWrapper[R, E, T, F <: WrapperFamily[WithBackPressure]] extends Any with Subscriber[R, E, T, WithBackPressure, F] {
  private[rxala] def obs: F#WrappedSubscriberType[R, E, T]
}
trait NoBackPressureSubscriberWrapper[R, E, T, F <: WrapperFamily[NoBackPressure]] extends Any with Subscriber[R, E, T, NoBackPressure, F] {
  private[rxala] def obs: F#WrappedSubscriberType[R, E, T]
}
