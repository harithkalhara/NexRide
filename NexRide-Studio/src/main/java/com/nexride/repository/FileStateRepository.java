package com.nexride.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Function;

/**
 * Minimal in-memory repository for this standalone sample and its unit test.
 * It does not persist state to disk; replace with the original application repository
 * when integrating into the full NexRide backend.
 */
public class FileStateRepository implements StateRepository, AutoCloseable {
    private final State state = new State();

    public FileStateRepository(ObjectMapper mapper, String path) {
        // Constructor retained to match the uploaded test's expected API.
    }

    @Override
    public synchronized <T> T read(Function<State, T> action) {
        return action.apply(state);
    }

    @Override
    public synchronized <T> T update(Function<State, T> action) {
        return action.apply(state);
    }

    @Override
    public void close() { }
}
