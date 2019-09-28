package com.github.aborg0.rxala.reactor;

import com.github.aborg0.rxala.Cold;
import com.github.aborg0.rxala.ExactlyOne;
import com.github.aborg0.rxala.WithBackPressure;
import com.github.aborg0.rxala.invariant.Observable;
import com.github.aborg0.rxala.invariant.Subscriber;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.Test;

public class SimpleJavaTests {
    @Test
    public void testCheck() {
        TestSubscriber<Integer> testObserver = new TestSubscriber<>();
        Subscriber<Object, Throwable, Integer, WithBackPressure, Reactor3$> subscriber = new SubscriberReactor3<>(testObserver);
        Observable<Object, ? extends Throwable, Integer, ? extends ExactlyOne, WithBackPressure, Cold, Reactor3$> observer1 =
        ObservableReactor3Factory.single/*<Object, Throwable, Integer, Cold>*/(4)/*.map(i -> i + 3)*/;
        Observable<Object, ? extends Throwable, Integer, ? extends ExactlyOne, WithBackPressure, Cold, Reactor3$> observer = observer1.map(i -> i + 3);
        observer.subscribe(subscriber);
        testObserver.assertValue(7);
        testObserver.assertComplete();
    }
}
