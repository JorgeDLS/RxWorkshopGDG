package com.scmspain.workshop.rxintroduction;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public class RxTime {

  public static final Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);

  //public static final Observable<Date> now = Observable.just(new Date());

  public static Observable<Date> getDateEverySecond() {
    return interval.flatMap(aLong -> now);
  }

  //public static final Observable<Date> now = Observable.create(new Observable.OnSubscribe<Date>() {
  //  @Override public void call(Subscriber<? super Date> subscriber) {
  //    subscriber.onNext(new Date());
  //    subscriber.onCompleted();
  //  }
  //});
/*
  static Observable<Integer> myWrongInterval =
      Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
          try {
            for (int i = 0; i < 5; i++) {
              if (subscriber.isUnsubscribed()) return;
              Thread.sleep(1000);
              subscriber.onNext(i);
            }
            subscriber.onCompleted();
          } catch (Exception e) {
            subscriber.onError(e);
          }
        }
      });
*/


  public static final Observable<Date> now = Observable.defer(() -> Observable.just(new Date()));

}
