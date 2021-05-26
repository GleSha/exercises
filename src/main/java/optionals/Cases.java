package optionals;

import java.util.Optional;

public class Cases {


    public static class Foo {

        private int value;

        public Foo(int value) {
            System.out.println("Foo created with value = " + value);
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "value=" + value +
                    '}';
        }
    }

    public static void testOrElse() {
        /**
         * In this case we get this output:
         *
         * Foo created with value = 1
         * Foo created with value = 2
         * Foo{value=1}
         */
        Optional<Foo> optionalFoo = Optional.ofNullable(new Foo(1));
        Foo foo = optionalFoo.orElse(new Foo(2));
        System.out.println(foo);

        /**
         * This code adds:
         *
         * someMethod is running
         * Foo created with value = -1
         * Foo{value=1}
         *
         * to output
         */
        foo = optionalFoo.orElse(someMethod());
        System.out.println(foo);

        /**
         * This code adds:
         *
         * someMethod is running
         * Foo created with value = -1
         * Foo{value=-1}
         *
         * to output
         */
        optionalFoo = Optional.empty();
        foo = optionalFoo.orElse(someMethod());
        System.out.println(foo);
        /**
         * parameter of orElse() is evaluated even when having a non-empty Optional.
         */
    }

    public static void testOrElseGet() {
        /**
         * In this case we get output:
         *
         * Foo created with value = 1
         * Foo{value=1}
         *
         */
        Optional<Foo> optionalFoo = Optional.ofNullable(new Foo(1));
        Foo foo = optionalFoo.orElseGet(() -> new Foo(2));
        System.out.println(foo);

        /**
         * this code adds:
         *
         * Foo{value=1}
         *
         * to output
         */
        foo = optionalFoo.orElseGet(Cases::someMethod);
        System.out.println(foo);

        /**
         * this code adds:
         *
         * someMethod is running
         * Foo created with value = -1
         * Foo{value=-1}
         *
         * to output
         */
        optionalFoo = Optional.empty();
        foo = optionalFoo.orElseGet(Cases::someMethod);
        System.out.println(foo);

        /**
         * Supplier method passed as an argument is only executed when an Optional value is not present.
         */
    }

    public static void testOrElseThrow() {

        Optional<Foo> optionalFoo = Optional.ofNullable(new Foo(1));

        try {
            /**
             * This code prints:
             *
             * Foo created with value = 1
             * Foo{value=1}
             *
             */
            Foo foo = optionalFoo.orElseThrow(FooException::new);
            System.out.println(foo);

            /**
             * this code adds:
             *
             * optionals.Cases$FooException
             * 	at java.base/java.util.Optional.orElseThrow(Optional.java:408)
             * 	at optionals.Cases.testOrElseThrow(Cases.java:122)
             * 	at Main.main(Main.java:8)
             *
             * 	to output.
             */
            optionalFoo = Optional.empty();
            foo = optionalFoo.orElseThrow(FooException::new);
            System.out.println(foo);

            /**
             * Supplier method passed as an argument is only executed when an Optional value is not present.
             */

        } catch (FooException e) {
            e.printStackTrace();
        }
    }

    public static Foo someMethod() {
        System.out.println("someMethod is running");
        return new Foo(-1);
    }

    public static class FooException extends Exception {
    }

    public static class Bar {

        private int value;

        public Bar(int value) {
            System.out.println("Bar created with value = " + value);
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Bar{" +
                    "value=" + value +
                    '}';
        }
    }

    public static void testMapsFilterAndGet() {
        /**
         * This code prints:
         *
         * Foo created with value = 1
         * Exception in thread "main" java.util.NoSuchElementException: No value present
         * 	at java.base/java.util.Optional.get(Optional.java:148)
         * 	at optionals.Cases.testMapsFilterAndGet(Cases.java:181)
         * 	at Main.main(Main.java:8)
         *
         */
        Optional<Foo> optionalFoo = Optional.ofNullable(new Foo(1));
        Foo foo = optionalFoo.get();
        optionalFoo = Optional.empty();
        //foo = optionalFoo.get();

        /**
         * This code prints:
         *
         * Foo created with value = 1
         * Foo created with value = 42
         * Foo{value=42}
         *
         *
         */
        optionalFoo = Optional.ofNullable(new Foo(1));
        foo = optionalFoo.filter((f) -> f.getValue() < 0).orElseGet(() -> new Foo(42));
        System.out.println(foo);


        /**
         * this code prints:
         *
         * Foo created with value = 1
         * Foo{value=1}
         *
         */
        optionalFoo = Optional.ofNullable(new Foo(1));
        foo = optionalFoo.filter((f) -> f.getValue() == 1).orElseGet(() -> new Foo(42));
        System.out.println(foo);

        /**
         * this code prints:
         *
         * Bar created with value = 1
         * Bar{value=1}
         *
         */
        //foo have value = 1
        Bar bar = optionalFoo.map(Cases::getBar).get();
        System.out.println(bar);
        /**
         *
         * this code prints the same output
         *
         */
        bar = optionalFoo.flatMap(Cases::getBarOptional).orElseGet(() -> new Bar(42));
        System.out.println(bar);
        /**
         * flatMap does not wrap the lambda return value with an additional Optional,
         * so we must return an Optional for using it.
         */
    }

    public static Bar getBar(Foo foo) {
        return new Bar(foo.getValue());
    }

    public static Optional<Bar> getBarOptional(Foo foo) {
        if (foo.getValue() > 0) {
            return Optional.of(new Bar(foo.getValue()));
        } else {
            return Optional.empty();
        }
    }

}
