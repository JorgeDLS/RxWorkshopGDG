package com.scmspain.workshop.rxintroduction;

import rx.Observable;
import rx.functions.Func1;

public class RxBasics {
  public static Observable<Integer> getIntegerObservable() {
    return Observable.just(4);
    //return Observable.just(2,3,4,5);
    //return Observable.range(1,6);
    //return Observable.from(new Integer[] { 2, 3, 4 });
  }

  public static Func1<Integer, Integer> multiplyByTen() {
    return integer -> integer * 10;
  }

  public static Observable<String> getStringObservable() {
    return Observable.just("uno", "dos", "tres", "cuatro");
  }

  public static Func1<String, Integer> stringToLength() {
    return s -> s.length();
  }
}
