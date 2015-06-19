/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

/**
 *
 */
package org.mozilla.javascript.tests;

import java.io.File;

import junit.framework.TestCase;

import org.mozilla.javascript.*;

/**
 * See https://bugzilla.mozilla.org/show_bug.cgi?id=409702
 * @author Norris Boyd
 */
public class DoubleEquals extends TestCase {

    public static abstract class Foo {
        public Foo() {
        }

        public abstract void a();

        public abstract int b();

        public static abstract class Subclass extends Foo {

            @Override
            public final void a() {
            }
        }
    }

  public void testAdapter() {
      String source =
          "var a, b; var c = 1; a == b == c;";

      Context cx = ContextFactory.getGlobal().enterContext();
      try {
          cx.initStandardObjects();
          cx.debugHtml(source, new File("/tmp/DoubeEquals.html"));
      } finally {
          Context.exit();
      }
  }
}
