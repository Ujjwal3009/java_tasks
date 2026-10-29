package com.epam.files;

import com.epam.files.serialization.Employee;
import com.epam.files.serialization.Manager;
import com.epam.files.serialization.Person;
import com.epam.files.serialization.SerializationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SerializationTest {

    @Test
    void testPersonSerializationTransientNumeric() throws Exception {
        var person = new Person("John", 30);
        byte[] bytes = SerializationService.serialize(person);
        Person deserialized = SerializationService.deserialize(bytes);

        Assertions.assertEquals("John", deserialized.getName());
        // age is transient, should reset to default 0
        Assertions.assertEquals(0, deserialized.getAge());
    }

    @Test
    void testEmployeeSerializationTransientNumeric() throws Exception {
        var emp = new Employee("Alice", 28, "Engineering", 101L, 75000.0);
        byte[] bytes = SerializationService.serialize(emp);
        Employee deserialized = SerializationService.deserialize(bytes);

        Assertions.assertEquals("Alice", deserialized.getName());
        Assertions.assertEquals("Engineering", deserialized.getDepartment());
        Assertions.assertEquals(0, deserialized.getAge());
        Assertions.assertEquals(0L, deserialized.getId());
        Assertions.assertEquals(0.0, deserialized.getSalary());
    }

    @Test
    void testManagerSerializationTransientNumeric() throws Exception {
        var mgr = new Manager("Bob", 45, "Management", 501L, 120000.0, "Project Titan", 25000.0);
        byte[] bytes = SerializationService.serialize(mgr);
        Manager deserialized = SerializationService.deserialize(bytes);

        Assertions.assertEquals("Bob", deserialized.getName());
        Assertions.assertEquals("Management", deserialized.getDepartment());
        Assertions.assertEquals("Project Titan", deserialized.getProject());
        Assertions.assertEquals(0, deserialized.getAge());
        Assertions.assertEquals(0L, deserialized.getId());
        Assertions.assertEquals(0.0, deserialized.getSalary());
        Assertions.assertEquals(0.0, deserialized.getBonus());
    }
}
