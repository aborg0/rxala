package com.github.aborg0.rxala.invariant.wrapper

import com.github.aborg0.rxala.Family

import scala.language.higherKinds

trait WrapperFamily extends Family {
  type WrappedObservableType[R, E, T]
  type WrappedSubscriberType[R, E, T]
}
