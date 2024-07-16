package org.example.models;

public class User {
    private int id;
    private String surname;
    private String name;
    private String lastname;
    private String email;
    private String pass;
    private String about;

    private String time;
    private String spec;
    private int status;
    private String type;

    public User() {

    }

    public User(String surname, String name, String lastname, String email, String pass) {
        this.surname = surname;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
    }

    public User(String surname, String name, String lastname, String email, String pass, String spec, String about, String type) {
        this.surname = surname;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
        this.spec = spec;
        this.about = about;
        this.type = type;
    }

    public User(String email, String password) {
        this.email = email;
        this.pass = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getData(){
        return surname + " " + name + " " + lastname;
    }

    public String getDocData(){
        return surname + " " + name + " " + lastname + " (" + spec + ")";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
