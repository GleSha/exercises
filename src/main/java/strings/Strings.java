package strings;

public class Strings {

    public static void test() {

        /**
         * This code prints:
         *
         * true
         * true
         * true
         * false
         * 101574
         * 101574
         * 101574 (hashcodes are the same)
         *
         * s1 and s2 refers to the same string object, but constructor for s3 creates new string object
         */
        String s1 = "foo";
        String s2 = "foo";
        String s3 = new String("foo");
        System.out.println(s1.equals(s2));
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s3));
        System.out.println(s1 == s3);
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(System.identityHashCode(s1));
        System.out.println(System.identityHashCode(s2));
        System.out.println(System.identityHashCode(s3));

        /**
         *
         * This code prints:
         *
         * After intern():
         * true
         * true
         * true
         * true
         * 101574
         * 101574
         * 101574
         */

        System.out.println("After intern():");
        /**
         *
         * now s3 refers to the object for s1 and s2 => s1, s2, s3 refers to the same object in memory
         */
        s3 = s3.intern();
        System.out.println(s1.equals(s2));
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s3));
        System.out.println(s1 == s3);
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(s3.hashCode());
        System.out.println(System.identityHashCode(s1));
        System.out.println(System.identityHashCode(s2));
        System.out.println(System.identityHashCode(s3));

    }
}
