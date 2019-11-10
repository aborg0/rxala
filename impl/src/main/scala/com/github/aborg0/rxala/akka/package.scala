package com.github.aborg0.rxala

package object akka {
  def func[T, Q](f: java.util.function.Function[_ >: T, _ <: Q]): _root_.akka.japi.function.Function[T, Q] = (t: T) => f(t)

  def pred[T, Q](f: java.util.function.Predicate[_ >: T]): _root_.akka.japi.function.Predicate[T] = (t: T) => f.test(t)

}
