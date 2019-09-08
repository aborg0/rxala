package com.github.aborg0.rxala

package object rxjava3 {
  def func[T, Q](f: java.util.function.Function[_ >: T, _ <: Q]): io.reactivex.rxjava3.functions.Function[T, Q] = (t: T) => f(t)

  def pred[T, Q](f: java.util.function.Predicate[_ >: T]): io.reactivex.rxjava3.functions.Predicate[T] = (t: T) => f.test(t)
}
