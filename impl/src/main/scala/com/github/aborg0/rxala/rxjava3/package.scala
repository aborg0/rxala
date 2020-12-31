package com.github.aborg0.rxala

import io.reactivex.rxjava3.core.Observable

package object rxjava3 {
  def func[T, Q](f: java.util.function.Function[_ >: T, _ <: Q]): io.reactivex.rxjava3.functions.Function[T, Q] = (t: T) => f(t)

  def pred[T, Q](f: java.util.function.Predicate[_ >: T]): io.reactivex.rxjava3.functions.Predicate[T] = (t: T) => f.test(t)

  implicit class ObservableOps[T](val obs: Observable[T]) extends AnyVal {
    def flatMapAsync[S, U](seed: S)(combine: (S, U) => Observable[S])(implicit ev: T <:< Observable[U]): Observable[S] = {
      obs.flatMapSingle(t => ev(t).reduce(seed: S, (acc: S, v: U) => combine(acc, v).blockingFirst()))
    }
  }

}
