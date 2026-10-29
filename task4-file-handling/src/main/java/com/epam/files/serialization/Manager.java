package com.epam.files.serialization;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;

    private String project;
    private transient double bonus;

    public Manager() {
    }

    public Manager(String name, int age, String department, long id, double salary, String project, double bonus) {
        super(name, age, department, id, salary);
        this.project = project;
        this.bonus = bonus;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
