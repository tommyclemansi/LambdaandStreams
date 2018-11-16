package org.paumard.lambdamasterclass.part1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Test01_Consumer {

    /**
     * Write a consumer that clears the list it consumes.
     */
    @Test
    public void consumer_1() {

        Consumer<List<String>> consumer = List::clear;

        List<String> list =
                new ArrayList<>(Arrays.asList("a", "b", "c"));

        consumer.accept(list);

        assertThat(list).isEmpty();
    }

    /**
     * Write a consumer that first consumes the list with the
     * consumer c1, and then consumers it with the second consumer
     * c2.
     */
    @Test
    public void consumer_2() {
        Consumer<List<String>> c1 = list -> {
            list.add("first");
            System.out.println("c1 invoked");
        };
        Consumer<List<String>> c2 = list -> {
            list.add("second");
            System.out.println("c2 invoked");
        };

        Consumer<List<String>> consumer = c1.andThen(c2); // TODO
/*    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }*/
/*
 it means:
                    pass list         pass list again
                     | list.add(first)  | list.add(second)
 return (T t) -> this.accept(t); after.accept(t);

 */


        List<String> list =
                new ArrayList<>(Arrays.asList("a", "b", "c"));

        consumer.accept(list);
        assertThat(list).containsExactly("a", "b", "c", "first", "second");
    }


}
