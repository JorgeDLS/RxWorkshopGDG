package com.scmspain.workshop.rxintroduction;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;

public class RxTimeTest {

  @Test
  public void printInterval() throws Exception {
    System.out.println("printInterval()");

    System.out.println("printInterval() - Observable created");

    Observer<Long> longObserver = new Observer<Long>() {
      @Override
      public void onCompleted() {
        System.out.println("printInterval() --- onCompleted");
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onNext(Long aLong) {
        System.out.println("printInterval() --- onNext: " + aLong);
      }
    };
    System.out.println("printInterval() - Observer created");

    System.out.println("printInterval() - Before Subscribe");
    //RxTime.interval.subscribe(longObserver);
    TestSubscriber<Long> testSubscriber = new TestSubscriber<>(longObserver);
    RxTime.interval.take(20).subscribe(testSubscriber);
    testSubscriber.awaitTerminalEvent();

    System.out.println("printInterval() - After Subscribe");
  }

  @Test
  public void testDateEverySecond() throws Exception {
    Date oldDate = new Date();

    Observable<Date> dateObservable = RxTime.getDateEverySecond();
    Iterable<Date> dates = dateObservable.take(3).toBlocking().toIterable();

    for (Date date : dates) {
      System.out.println("newDate: " + date);
      assertTrue(
          "New date should later than old\n new: " + date + "\n oldDate:" + oldDate,
          date.after(oldDate)
      );
      oldDate = date;
    }
  }


@Test
  public void printTimerOfTimers() throws Exception {
    System.out.println("testTimerOfTimers");
    TestSubscriber<String> testSubscriber = new TestSubscriber<>(new Observer<String>() {
      @Override
      public void onCompleted() {
        System.out.println("Completed");
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onNext(String string) {
        System.out.println(string);
      }
    });
  Observable.interval(0, 500, TimeUnit.MILLISECONDS)
        .map(Long::intValue)
        .take(10)
        .concatMap(aInt -> Observable.interval(0, 500, TimeUnit.MILLISECONDS)
            .map(aLong -> new Date().getTime() + " " + aInt + "->" + aLong)
            .doOnSubscribe(() -> System.out.println("Emit " + aInt))
            .take(aInt))
        .subscribe(testSubscriber);

    testSubscriber.awaitTerminalEvent();
    System.out.println();
  }

}