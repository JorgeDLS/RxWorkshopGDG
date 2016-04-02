package com.scmspain.workshop.flights;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class FlightsBusiness {
  private final ArrayList<FlightsApi> flightProviders;
  protected final Comparator<Flight> flightComparator = new Comparator<Flight>() {
    @Override public int compare(Flight lhs, Flight rhs) {
      return lhs.price - rhs.price;
    }
  };

  public FlightsBusiness(String[] flightProviderEndpoints) {
    flightProviders = new ArrayList<>(flightProviderEndpoints.length);
    for (String flightProviderEndpoint : flightProviderEndpoints) {
      flightProviders.add(FlightsApiBuilder.getFlightsApi(flightProviderEndpoint));
    }
  }

  public ArrayList<Flight> flights() {
    ArrayList<Flight> flights = new ArrayList<>();
    for (FlightsApi flightProvider : flightProviders) {
      flights.add(flightProvider.getFlight());
    }
    return flights;
  }

  public List<Flight> flightsByPrice() {
    ArrayList<Flight> flights = flights();
    Collections.sort(flights, flightComparator);
    return flights;
  }

  public Observable<Flight> flightsObservable() {
    return Observable.from(flightProviders).flatMap(new Func1<FlightsApi, Observable<Flight>>() {
      @Override public Observable<Flight> call(FlightsApi flightProvider) {
        return flightProvider.getRxFlight();
      }
    });
  }

  public Observable<List<Flight>> flightsByPriceObservable() {
    return flightsObservable().toSortedList(new Func2<Flight, Flight, Integer>() {
      @Override public Integer call(Flight flight, Flight flight2) {
        return flightComparator.compare(flight, flight2);
      }
    });
  }

  public Observable<Collection<Flight>> incrementalFlightsByPriceObservable() {
    return flightsObservable().scan(new ArrayList<Flight>(),
        new Func2<Collection<Flight>, Flight, Collection<Flight>>() {
          @Override public Collection<Flight> call(Collection<Flight> flights, Flight flight2) {
            ArrayList<Flight> newFlights = new ArrayList<>(flights);
            newFlights.add(flight2);
            Collections.sort(newFlights, flightComparator);
            return newFlights;
          }
        }).filter(new Func1<Collection<Flight>, Boolean>() {
      @Override public Boolean call(Collection<Flight> flights) {
        return !flights.isEmpty();
      }
    });
  }
}
