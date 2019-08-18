package com.github.aborg0.rxala.invariant

trait ObservableSource[R, E, T] {
  def subscribe(observer: Observer[R, E, _ >: T]): Unit
}
