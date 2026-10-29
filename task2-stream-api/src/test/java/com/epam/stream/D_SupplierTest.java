package com.epam.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class D_SupplierTest {
    @Test
    public void d_supplier1() {
        Supplier<StringBuilder> sup = () -> new StringBuilder("abc");
        assertEquals("abc", sup.get().toString());
    }
}
