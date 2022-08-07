package org.hf.application.javabase.apply.jdk8.clone;

public class Student implements Cloneable{

    private String name;

    private int age;

    private Person person;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Student{" +
                this.hashCode()+
                "name='" + name + '\'' +
                ", age=" + age +
                ", person=" + person +
                '}';
    }

    @Override
    protected Object clone(){
        try {
          Student student = (Student) super.clone();
          student.person= (Person) person.clone();
          return student;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
