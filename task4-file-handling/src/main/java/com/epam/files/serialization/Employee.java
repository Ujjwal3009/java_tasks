package com.epam.files.serialization;

public class Employee extends Person {
    private static final long serialVersionUID = 1L;

    private String department;
    private transient long id;
    private transient double salary;

    public Employee() {
    }

    public Employee(String name, int age, String department, long id, double salary) {
        super(name, age);
        this.department = department;
        this.id = id;
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
