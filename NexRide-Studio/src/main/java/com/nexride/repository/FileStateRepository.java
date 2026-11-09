package com.nexride.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexride.model.AppState;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/** Single-instance, atomic JSON text-file storage for the coursework application. */
@Repository
public class FileStateRepository implements StateRepository {
  private final ObjectMapper mapper;
  private final Path file;
  private final FileChannel lockChannel;
  private final FileLock lock;
  private AppState state;

  public FileStateRepository(ObjectMapper mapper, @Value("${nexride.data-dir:./data}") String dir)
      throws IOException {
    this.mapper = mapper;
    Path folder = Path.of(dir).toAbsolutePath();
    Files.createDirectories(folder);
    this.file = folder.resolve("nexride-data.txt");
    lockChannel =
        FileChannel.open(
            folder.resolve(".nexride.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    lock = lockChannel.tryLock();
    if (lock == null)
      throw new IOException("This data folder is already open in another NexRide process.");
    state = Files.exists(file) ? mapper.readValue(file.toFile(), AppState.class) : new AppState();
  }

  private AppState copy() {
    return mapper.convertValue(state, AppState.class);
  }

  public synchronized <T> T read(Function<AppState, T> action) {
    return action.apply(copy());
  }

  public synchronized <T> T update(Function<AppState, T> action) {
    AppState next = copy();
    T result = action.apply(next);
    Path temp = file.resolveSibling("nexride-data.tmp");
    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), next);
      try (FileChannel out = FileChannel.open(temp, StandardOpenOption.WRITE)) {
        out.force(true);
      }
      Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      state = next;
      return result;
    } catch (IOException e) {
      try {
        Files.deleteIfExists(temp);
      } catch (IOException ignored) {
      }
      throw new IllegalStateException(
          "Could not save NexRide data. Existing data has been preserved.", e);
    }
  }

  @PreDestroy
  public void close() throws IOException {
    lock.release();
    lockChannel.close();
  }
}
