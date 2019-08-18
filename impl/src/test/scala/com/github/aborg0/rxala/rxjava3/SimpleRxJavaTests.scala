package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.ExactlyOne
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import io.reactivex.subscribers.TestSubscriber
import org.scalatest.WordSpec

class SimpleRxJavaTests extends WordSpec {

  "TestObserver" should {
    "check map" in {
      val testObserver = new TestSubscriber[Integer]()
      val subscriber: Subscriber[Any, Throwable, Integer, RxJava3.type] = new SubscriberRxJava3[Any, Throwable, Integer](testObserver)
      val observer: Observable[Any, _ <: Throwable, Integer, _ <: ExactlyOne, RxJava3.type] = ObservableRxJava3Factory.single[Any, Throwable, Int](4).map(_ + 3)
      observer.subscribe(subscriber)
      testObserver.assertValue(7)
      testObserver.assertComplete()
    }
  }
}
