package org.example;

class Foo {
    void bar() {
        /*
         * `Thread.stop()` always throws a `new UnsupportedOperationException()` in Java 21+.
         * For detailed migration instructions see the migration guide available at
         * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/doc-files/threadPrimitiveDeprecation.html
         */
        throw new UnsupportedOperationException();
    }
}