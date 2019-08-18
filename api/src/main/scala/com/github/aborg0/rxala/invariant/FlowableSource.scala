package com.github.aborg0.rxala.invariant

import com.github.aborg0.rxala.{Cardinality, Family}

trait FlowableSource[R, E, T, C <: Cardinality, F <: Family] extends Any {
  def subscribe(s: Subscriber[_ <: R, _ >: E, _ >: T, F]): Unit
}
