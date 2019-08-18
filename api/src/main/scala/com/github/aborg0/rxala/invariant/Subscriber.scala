package com.github.aborg0.rxala.invariant

import com.github.aborg0.rxala.Family

trait Subscriber[R, E, T, F <: Family] extends Any {
  def onNext(t: T): Unit

  def onComplete(): Unit

  def onError(error: E): Unit
}
