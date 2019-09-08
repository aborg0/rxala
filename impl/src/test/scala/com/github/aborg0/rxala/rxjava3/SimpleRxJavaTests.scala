package com.github.aborg0.rxala.rxjava3

import com.github.aborg0.rxala.invariant.{Observable, Subscriber}
import com.github.aborg0.rxala.{Cold, ExactlyOne, WithBackPressure}
import io.reactivex.subscribers.TestSubscriber
import org.scalatest.WordSpec

class SimpleRxJavaTests extends WordSpec {

  "TestObserver" should {
    "check map" in {
      val testObserver = new TestSubscriber[Integer]()
//      Flowable.fromIterable(util.Arrays.asList(1, 2)).
      val subscriber: Subscriber[Any, Throwable, Integer, WithBackPressure, RxJava3BackPressure.type] = new SubscriberRxJava3[Any, Throwable, Integer](testObserver)
      val observer: Observable[Any, _ <: Throwable, Integer, _ <: ExactlyOne, WithBackPressure, Cold, RxJava3BackPressure.type] = ObservableRxJava3Factory.single[Any, Throwable, Int, Cold](4).map(_ + 3)
      observer.subscribe(subscriber)
      testObserver.assertValue(7)
      testObserver.assertComplete()
    }
  }
}
