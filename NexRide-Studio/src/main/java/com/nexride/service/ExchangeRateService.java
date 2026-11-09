package com.nexride.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexride.model.*;
import com.nexride.repository.StateRepository;
import java.net.URI;
import java.net.http.*;
import java.time.*;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
  private final StateRepository repo;
  private final ObjectMapper json;

  public ExchangeRateService(StateRepository r, ObjectMapper j) {
    repo = r;
    json = j;
  }

  public SiteSettings refresh(User admin) {
    UserService.requireAdmin(admin);
    try {
      HttpResponse<String> res =
          HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(8))
              .build()
              .send(
                  HttpRequest.newBuilder(URI.create("https://open.er-api.com/v6/latest/USD"))
                      .timeout(Duration.ofSeconds(12))
                      .GET()
                      .build(),
                  HttpResponse.BodyHandlers.ofString());
      JsonNode root = json.readTree(res.body());
      double rate = root.path("rates").path("LKR").asDouble(0);
      if (res.statusCode() != 200 || rate <= 0)
        throw ApiException.bad("Rate provider did not return a valid LKR rate.");
      return repo.update(
          s -> {
            s.getSettings().setLkrPerUsd(java.math.BigDecimal.valueOf(rate));
            s.getSettings().setRateSource("ExchangeRate-API");
            s.getSettings()
                .setRateUpdatedAt(
                    Instant.ofEpochSecond(root.path("time_last_update_unix").asLong()).toString());
            return s.getSettings();
          });
    } catch (ApiException e) {
      throw e;
    } catch (Exception e) {
      throw ApiException.bad(
          "Could not reach the exchange-rate provider. Enter the rate manually.");
    }
  }
}
