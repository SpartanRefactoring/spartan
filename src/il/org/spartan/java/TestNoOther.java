/**
 *
 */
package il.org.spartan.java;

import static il.org.spartan.AssertToAzzert.*;
import static il.org.spartan.azzert.*;

import org.junit.*;

import java.io.*;
import java.util.*;

import static il.org.spartan.AssertToAzzert.*;import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.*;

import il.org.spartan.*;
import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 16/05/2011 */
@RunWith(Theories.class) @SuppressWarnings("static-method")
//
public class TestNoOther {
  @DataPoints public static File[] javaFiles() throws IOException {
    final Set<File> $ = new TreeSet<>();
    new JavaFilesVisitor(".", new PlainFileOnlyAction() {
      @Override public void visitFile(final File f) {
        $.add(f);
      }
    }).go();
    return Iterables.toArray($, File.class);
  }

  public static String read(final File f) throws IOException {
    final char[] $ = new char[(int) f.length()];
    final FileReader x = new FileReader(f);
    final int n = x.read($);
    x.close();
    return new String(Arrays.copyOf($, n));
  }

  public static void write(final File f, final String text) throws IOException {
    final Writer w = new FileWriter(f);
    w.write(text);
    w.close();
  }

  private final File fin = new File("test/data/UnicodeFile");

  @Test public void brace_brace_newline() throws IOException {
    azzert.that(TokenAsIs.stringToString("{}\n"), is("{}\n"));
  }

  @Theory public void fullTokenization(final File f) throws IOException {
    System.err.println("Testing " + f);
    azzert.that(TokenAsIs.fileToString(f), is(read(f)));
  }

  @Test public void some_method() throws IOException {
    final String s = Separate.nl(
        //
        "private static int circularSum(final int[] a, final int[] b, final int offset) {", //
        "  int $ = 0;", //
        " for (int i = 0; i < a.length; i++)", //
        "    $ += a[i] * b[(i + offset) % b.length];", //
        "  return $;", //
        "}", //
        " ", //
        " ", //
        "  ");
    azzert.that(TokenAsIs.stringToString(s), is(s));
  }

  @Test public void unicode() throws IOException {
    final String s = "יוסי";
    azzert.that((TokenAsIs.stringToString(s) + ""), is(s));
  }

  @Test public void unicodeFileAgainstFileOutput() throws IOException {
    final String s = TokenAsIs.fileToString(fin);
    final File fout = new File(fin.getPath() + ".out");
    write(fout, s);
    azzert.that(read(fout), is(s));
    azzert.that(read(fout), is(read(fin)));
  }

  @Test public void unicodeFileAgainstString() throws IOException {
    azzert.that(TokenAsIs.fileToString(fin), is(read(fin)));
  }

  @Test public void unicodeFileLenth() throws IOException {
    assert fin.length() > TokenAsIs.fileToString(fin).length();
  }
}
