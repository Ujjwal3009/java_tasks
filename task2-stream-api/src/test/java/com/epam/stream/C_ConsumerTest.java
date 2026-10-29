package com.epam.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class C_ConsumerTest {
    @Test
    void c_consumer1() {
        StringBuilder sb = new StringBuilder("xyz");
        Consumer<StringBuilder> appenderConsumer = b -> b.append("abc");
        appenderConsumer.accept(sb);
        assertEquals("xyzabc", sb.toString());
    }
}
