package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.invariant.wrapper.WrapperFamily

case object RxJava3 extends WrapperFamily {
  override type WrappedObservableType[R, E, T] = io.reactivex.Flowable[T]
  override type WrappedSubscriberType[R, E, T] = io.reactivex.FlowableSubscriber[T]
}
