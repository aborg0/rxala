package com.github.aborg0.rxala.invariant

import com.github.aborg0.rxala.{BackPressure, Family}

trait ObservableSource[R, E, T, B <: BackPressure, F <: Family[B]] extends Any {
  def subscribe(observer: Subscriber[_ <: R, _ >: E, _ >: T, B, F]): Unit
}
