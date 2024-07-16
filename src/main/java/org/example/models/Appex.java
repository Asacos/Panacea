package org.example.models;

public class Appex {
    private String person;
    private String doctor;
    private String time;

    public Appex(String person, String doctor, String time) {
        this.person = person;
        this.doctor = doctor;
        this.time = time;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
