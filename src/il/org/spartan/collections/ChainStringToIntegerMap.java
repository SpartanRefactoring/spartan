package il.org.spartan.collections;

import java.util.*;
import java.util.Map.*;

import org.jetbrains.annotations.*;

public class ChainStringToIntegerMap {
  @NotNull public Map<String, Integer> inner = new HashMap<>();

  public boolean containsKey(final String key) {
    return inner.containsKey(key);
  }

  public boolean containsValue(final int value) {
    return inner.containsValue(Integer.valueOf(value));
  }

  @NotNull public Set<Entry<String, Integer>> entrySet() {
    return inner.entrySet();
  }

  @SuppressWarnings("null") public int get(final Object key) {
    return inner.get(key).intValue();
  }

  public boolean isEmpty() {
    return inner.isEmpty();
  }

  @NotNull public Set<String> keySet() {
    return inner.keySet();
  }

  @NotNull public ChainStringToIntegerMap put(final String key, final int value) {
    assert !inner.containsKey(key);
    inner.put(key, Integer.valueOf(value));
    return this;
  }

  @NotNull public ChainStringToIntegerMap putAll(@NotNull final Map<? extends String, ? extends Integer> ¢) {
    inner.putAll(¢);
    return this;
  }

  @NotNull public ChainStringToIntegerMap putOn(final int value, @NotNull final String... keys) {
    for (final String key : keys)
      put(key, value);
    return this;
  }

  @NotNull public ChainStringToIntegerMap remove(final String key) {
    inner.remove(key);
    return this;
  }

  public int size() {
    return inner.size();
  }

  @NotNull public Collection<Integer> values() {
    return inner.values();
  }
}