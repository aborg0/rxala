package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.{NoBackPressure, WithBackPressure}
import com.github.aborg0.rxala.invariant.wrapper.WrapperFamily
import io.reactivex.rxjava3.disposables.Disposable
import org.reactivestreams.Subscription

case object RxJava3BackPressure extends WrapperFamily[WithBackPressure] {
  override type WrappedObservableType[R, E, T] = io.reactivex.rxjava3.core.Flowable[T]
  override type WrappedSubscriberType[R, E, T] = io.reactivex.rxjava3.core.FlowableSubscriber[T]
//  override type WrappedSubscriptionType[T] = Subscription[T]
  override type SubscriptionType = Subscription
}
case object RxJava3NoBackPressure extends WrapperFamily[NoBackPressure] {
  override type WrappedObservableType[R, E, T] = io.reactivex.rxjava3.core.Observable[T]
  override type WrappedSubscriberType[R, E, T] = io.reactivex.rxjava3.core.Observer[T]
//  override type WrappedSubscriptionType[T] = Disposable
  override type SubscriptionType = Disposable
}
