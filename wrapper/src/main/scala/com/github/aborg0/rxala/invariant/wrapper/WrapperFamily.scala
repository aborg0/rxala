package com.github.aborg0.rxala.invariant.wrapper

import com.github.aborg0.rxala.{BackPressure, Family}

import scala.language.higherKinds

trait WrapperFamily[B <: BackPressure] extends Family[B] {
  type WrappedObservableType[R, E, T]
  type WrappedSubscriberType[R, E, T]
}
