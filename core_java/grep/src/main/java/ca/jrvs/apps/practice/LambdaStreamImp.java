package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamImp implements LambdaStreamExc {

    /**
     * Create a String stream from array
     */
    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }

    /**
     * Convert all strings to uppercase
     * please use createStrStream
     */
    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings)
                .map(String::toUpperCase);
    }

    /**
     * filter strings that contains the pattern
     * return stream which NO element contains the pattern
     */
    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream
                .filter(s -> !s.contains(pattern));
    }

    /**
     * Create a intStream from a arr[]
     */
    @Override
    public IntStream createIntStream(int[] arr) {
        return Arrays.stream(arr);
    }

    /**
     * Convert a stream to list
     */
    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * Convert a intStream to list
     */
    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    /**
     * Create a IntStream range from start to end inclusive
     */
    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    /**
     * Convert a intStream to a doubleStream
     * and compute square root of each element
     */
    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(Math::sqrt);
    }

    /**
     * filter all even number and return odd numbers
     */
    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(n -> n % 2 != 0);
    }

    /**
     * Return a lambda function that print a message with a prefix and suffix
     */
    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return msg -> System.out.println(prefix + msg + suffix);
    }

    /**
     * Print each message with a given printer
     */
    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Arrays.stream(messages).forEach(printer);
    }

    /**
     * Print all odd number from a intStream
     */
    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream)
                .mapToObj(String::valueOf)
                .forEach(printer);
    }

    /**
     * Square each number from the input using flatMap
     */
    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints
                .flatMap(List::stream)
                .map(n -> n * n);
    }

    public static void main(String[] args) {

        LambdaStreamExc lse = new LambdaStreamImp();

        System.out.println("=== createStrStream ===");
        lse.createStrStream("java", "lambda", "stream")
                .forEach(System.out::println);

        System.out.println("\n=== toUpperCase ===");
        lse.toUpperCase("java", "lambda", "stream")
                .forEach(System.out::println);

        System.out.println("\n=== filter (remove strings containing 'a') ===");
        Stream<String> filtered =
                lse.filter(lse.createStrStream("java", "hello", "world"), "a");
        filtered.forEach(System.out::println);

        System.out.println("\n=== createIntStream from array ===");
        int[] arr = {1, 2, 3, 4, 5};
        lse.createIntStream(arr).forEach(System.out::println);

        System.out.println("\n=== toList(Stream<E>) ===");
        List<String> strList =
                lse.toList(lse.createStrStream("a", "b", "c"));
        System.out.println(strList);

        System.out.println("\n=== toList(IntStream) ===");
        List<Integer> intList =
                lse.toList(lse.createIntStream(arr));
        System.out.println(intList);

        System.out.println("\n=== createIntStream(start, end) ===");
        lse.createIntStream(1, 5).forEach(System.out::println);

        System.out.println("\n=== squareRootIntStream ===");
        lse.squareRootIntStream(lse.createIntStream(1, 5))
                .forEach(System.out::println);

        System.out.println("\n=== getOdd ===");
        lse.getOdd(lse.createIntStream(0, 10))
                .forEach(System.out::println);

        System.out.println("\n=== getLambdaPrinter ===");
        lse.getLambdaPrinter("start>", "<end")
                .accept("Message body");

        System.out.println("\n=== printMessages ===");
        String[] messages = {"a", "b", "c"};
        lse.printMessages(messages,
                lse.getLambdaPrinter("msg:", "!"));

        System.out.println("\n=== printOdd ===");
        lse.printOdd(
                lse.createIntStream(0, 10),
                lse.getLambdaPrinter("odd number:", "!")
        );

        System.out.println("\n=== flatNestedInt ===");

        Stream<List<Integer>> nestedInts = Stream.of(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5)
        );

        lse.flatNestedInt(nestedInts)
                .forEach(System.out::println);

    }


}

