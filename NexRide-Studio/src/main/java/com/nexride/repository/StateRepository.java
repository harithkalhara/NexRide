package com.nexride.repository;

import com.nexride.model.AppState;
import java.util.function.Function;

public interface StateRepository {
  <T> T read(Function<AppState, T> action);

  <T> T update(Function<AppState, T> action);
}
