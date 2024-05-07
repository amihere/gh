package com.kpsl.java_google_api;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main
 */
public class Main {

  static Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    String apiKey = System.getProperty("maps.key");
    String[] origins = {System.getProperty("o")};
    String[] destinations = {System.getProperty("d")};
    long _10_MINS = 10 * 60 * 1000;

    Helper h = new Helper(apiKey, origins, destinations);
    long time = System.currentTimeMillis() + _10_MINS;
    var instant = changeToReadableTime(time).atZone(ZoneOffset.UTC);
    System.out.println("Hello routing with departure time of: " +
                       instant.getHour() + ":" + instant.getMinute());

    fetchTrafficResults(time, h);
  }

  private static Instant changeToReadableTime(long time) {
    return java.time.Instant.ofEpochMilli(time);
  }

  public static void fetchTrafficResults(long millis, Helper helper)
      throws ApiException, InterruptedException, IOException {

    String[] origins = helper.origins();
    String[] destinations = helper.destinations();
    String apiKey = helper.apiKey();

    GeoApiContext ctx = new GeoApiContext.Builder().apiKey(apiKey).build();
    DistanceMatrix response = DistanceMatrixApi.newRequest(ctx)
                                  .departureTime(Instant.ofEpochMilli(millis))
                                  .origins(origins)
                                  .destinations(destinations)
                                  .await();
    for (DistanceMatrixRow matRow : response.rows) {
      for (DistanceMatrixElement elem : matRow.elements) {
        System.out.println(
            "\n\nA route suggests: \n" + elem.durationInTraffic.inSeconds / 60 +
            " minutes in traffic,\n" + elem.duration.inSeconds / 60 +
            " minutes total, \nand covering " +
            elem.distance.inMeters / 1000 + '.' +
            elem.distance.inMeters % 1000 + "km\n\n");
      }
    }

    ctx.shutdown();
  }
}

record Helper(String apiKey, String[] origins, String[] destinations) {}
