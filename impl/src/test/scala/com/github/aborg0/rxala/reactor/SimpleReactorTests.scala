package com.github.aborg0.rxala.reactor

import com.github.aborg0.rxala.ExactlyOne
import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import io.reactivex.subscribers.TestSubscriber
import org.scalatest.WordSpec

class SimpleReactorTests extends WordSpec {

  "TestObserver" should {
    "check map" in {
      val testObserver: TestSubscriber[Integer] = new TestSubscriber[Integer]()
      val subscriber: Subscriber[Any, Throwable, Integer, Reactor3.type] = new SubscriberReactor3[Any, Throwable, Integer](testObserver)
      val observer: Observable[Any, _ <: Throwable, Integer, _ <: ExactlyOne, Reactor3.type] = ObservableReactor3Factory.single[Any, Throwable, Int](4).map(_ + 3)
      observer.subscribe(subscriber)
      testObserver.assertValue(7)
      testObserver.assertComplete()
    }
  }

}
