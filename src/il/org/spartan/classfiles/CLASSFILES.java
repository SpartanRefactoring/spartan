package il.org.spartan.classfiles;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A utility class, serving as a façade to {@link CLASSPATH}, {@link JRE},
 * {@link EXTENSIONPATH}, {@link ZipFile} and {@link File} providing a unified
 * repository of all locations where Java binaries may be found.
 * @author Yossi Gil */
@Utility public enum CLASSFILES {
  ;
  static Set<ZipFile> zipsInUse = new HashSet<>();

  /** Where are all Java class files found
   * @return the list of directories and ZIP archives in the current search
   *         path. */
  public static Iterable<File> asFiles() {
    final ArrayList<File> $ = new ArrayList<>();
    $.addAll(JRE.asList());
    add($, EXTENSIONPATH.asArray(), CLASSPATH.asArray());
    return $;
  }

  /** Given the full name of a class, return a textual representation of the
   * location where the appropriate <tt>.class</tt> can be found.
   * @param className the full class name, where the inner- and anonymous- class
   *        separator is the <tt>$</tt> character, i.e., in the format returned
   *        by method {@link java.lang.Class#getName()}
   * @return a textual representation of the location in which the corresponding
   *         <tt>.class</tt> can be found, or <code><b>null</b></code> if this
   *         class has no corresponding <tt>.class</tt> file (e.g., in the case
   *         it is a primitive or an array type), or in the case that the
   *         corresponding <tt>.class</tt> file could not be found. */
  public static String location(final String className) {
    nonnull(className);
    for (final File where : asFiles()) {
      final String $ = location(where, className);
      if ($ != null)
        return $;
    }
    return null;
  }

  /** Exercise this class by printing the result of its principal function.
   * @param __ unused */
  public static void main(final String[] __) {
    System.out.println(Separate.by(asFiles(), "\n"));
  }

  /** Given a {@link Class} object, return an open input stream to the
   * <tt>.class</tt> file where this class was implemented. (The input stream is
   * found by searching the class files repositories, and hence is not
   * guaranteed to be precisely that of the given object);
   * @param c an arbitrary {@link Class} object
   * @return an {@link InputStream} to the result of best effort search for the
   *         <tt>.class</tt> where this class was implemented. Or,
   *         <code><b>null</b></code> if this class has no corresponding
   *         <tt>.class</tt> file (e.g., in the case it is a primitive or an
   *         array type), or in the case that the corresponding <tt>.class</tt>
   *         file could not be found. */
  public static InputStream open(final Class<?> c) {
    nonnull(c);
    return open(c.getName());
  }

  /** Given the full name of a class, return an open input stream to the class
   * file where this class was implemented. (The input stream is found by
   * searching the class files repositories, and hence is not guaranteed to be
   * precisely that of the given object.)
   * @param fullClassName the full class name, where the inner- and anonymous-
   *        class separator is the <tt>$</tt> character, i.e., in the format
   *        returned by method {@link java.lang.Class#getName()}
   * @return an {@link InputStream} to the result of the best effort search for
   *         the <tt>.class</tt> where this class was implemented.
   *         <code><b>null</b></code> if this class has no corresponding
   *         <tt>.class</tt> file (e.g., in the case it is a primitive or an
   *         array type), or in the case that the corresponding <tt>.class</tt>
   *         file could not be found. */
  public static InputStream open(final String fullClassName) {
    nonnull(fullClassName);
    for (final File f : asFiles()) {
      final InputStream $ = open(f, fullClassName);
      if ($ != null)
        try {
          $.available();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      if ($ != null)
        return $;
    }
    return null;
  }

  public static void reset() {
    for (final ZipFile z : zipsInUse)
      try {
        z.close();
      } catch (final IOException __) {
        // Absorb (we do not care about errors)
        __.printStackTrace();
      }
    zipsInUse.clear();
  }

  private static void add(final ArrayList<File> ds, final String[]... directoryNamesArray) {
    for (final String[] directories : directoryNamesArray)
      add(ds, directories);
  }

  private static void add(final ArrayList<File> ds, final String[] directoryNames) {
    for (final String directory : directoryNames)
      ds.add(new File(directory));
  }

  private static String canonicalFileName(final String className) {
    return className.replace('.', File.separatorChar) + ".class";
  }

  private static String class2ZipFileName(final String className) {
    return className.replace('.', '/') + ".class";
  }

  private static String location(final File where, final String className) {
    return where.isDirectory() ? searchDirectory(where, className) == null ? null : where.getName()
        : searchZip(where, class2ZipFileName(className)) != null ? where.getName() : null;
  }

  private static InputStream open(final File where, final String className) {
    return where.isDirectory() ? searchDirectory(where, className) : searchZip(where, class2ZipFileName(className));
  }

  private static InputStream searchDirectory(final File where, final String className) {
    final File $ = new File(where, canonicalFileName(className));
    try {
      return !$.exists() ? null : new FileInputStream($);
    } catch (final FileNotFoundException __) {
      return null;
    }
  }

  private static InputStream searchZip(final File where, final String fileName) {
    try {
      final ZipFile z = new ZipFile(where.getAbsoluteFile());
      final ZipEntry e = z.getEntry(fileName);
      if (e == null) {
        z.close();
        return null;
      }
      zipsInUse.add(z);
      return z.getInputStream(e);
      /* for (final ZipEntry e : IterableAdapter.make(z.entries())) if
       * (e.getName().equals(fileName)) { zipsInUse.add(z); return
       * z.getInputStream(e); } z.close(); */
    } catch (final IOException __) {
      // Absorb (we do not care about errors)
    }
    return null;
  }
}
