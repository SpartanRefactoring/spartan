package il.org.spartan;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;
import org.junit.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @since @{year}-@{month}-@{day} */
public class maybe<@Nullable T> {
  @NotNull public static <@Nullable T> maybe<T> no() {
    return new maybe<>();
  }

  @NotNull public static <@Nullable T> maybe<T> yes(final T ¢) {
    return new maybe<>(¢);
  }

  @org.jetbrains.annotations.Nullable private @Nullable T inner;

  /** Instantiates this class.
   * @param inner JD */
  public maybe(final @Nullable T inner) {
    this.inner = inner;
  }

  private maybe() {
    inner = null;
  }

  @NotNull public maybe<@Nullable T> clear() {
    inner = null;
    return this;
  }

  @org.jetbrains.annotations.Nullable @Nullable public T get() {
    return inner;
  }

  public boolean missing() {
    return inner == null;
  }

  public boolean present() {
    return inner != null;
  }

  /** @param inner TODO document this parameter */
  @NotNull public maybe<@Nullable T> set(final T inner) {
    this.inner = inner;
    return this;
  }

  @SuppressWarnings({ "javadoc", "static-method" }) public static class TEST {
    @Test public void usecase0() {
      azzert.isNull(maybe.no().get());
    }

    @Test public void usecase1() {
      azzert.isNull(maybe.yes(null).get());
    }

    @Test public void usecase2() {
      azzert.notNull(maybe.yes(new Object()).get());
    }
  }
}
