package org.example.dao;

import org.example.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DAO {

    private int id = 0;
    private String role = "none";

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    private List<String> setAllTimes() {
        List<String> times = new ArrayList<>();
        times.add("8:00");
        times.add("8:30");
        times.add("9:00");
        times.add("9:30");
        times.add("10:00");
        times.add("10:30");
        times.add("11:00");
        times.add("11:30");
        times.add("12:00");
        times.add("12:30");
        times.add("13:00");
        times.add("13:30");
        return times;
    }

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isPersonExist(String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM person WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        return !users.isEmpty();
    }

    public boolean isDoctorExist(String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM doctor WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        return !users.isEmpty();
    }

    public boolean isAdminExist(String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM admin WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        return !users.isEmpty();
    }

    public boolean checkPersonPassword (String email, String password) {
        List <User> users = jdbcTemplate.query("SELECT * FROM person WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getPass().equals(password);
        }
        return false;
    }

    public boolean checkDoctorPassword (String email, String password) {
        List <User> users = jdbcTemplate.query("SELECT * FROM doctor WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getPass().equals(password);
        }
        return false;
    }

    public boolean checkAdminPassword (String email, String password) {
        List <User> users = jdbcTemplate.query("SELECT * FROM admin WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getPass().equals(password);
        }
        return false;
    }

    public int getPersonID (String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM person WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getId();
        }
        return -1;
    }

    public int getDoctorID (String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM doctor WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getId();
        }
        return -1;
    }

    public int getAdminID (String email) {
        List <User> users = jdbcTemplate.query("SELECT * FROM admin WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(User.class));
        if (!users.isEmpty()) {
            User user = users.get(0);
            return user.getId();
        }
        return -1;
    }

    public void appendPersonRow(User user) {
        jdbcTemplate.update("INSERT INTO person (surname, name, lastname, email, pass, status) VALUES (?, ?, ?, ?, ?, 0)",
                user.getSurname(), user.getName(), user.getLastname(), user.getEmail(), user.getPass());
    }

    public void appendDoctorRow(User user) {
        jdbcTemplate.update("INSERT INTO doctor (surname, name, lastname, email, pass, spec, about, type) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", user.getSurname(), user.getName(), user.getLastname(),
                user.getEmail(), user.getPass(), user.getSpec(), user.getAbout(), user.getType());
    }

    public List<User> getPersonUsers () {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> getDoctorUsersOut () {
        return jdbcTemplate.query("SELECT * FROM doctor WHERE type = ?", new Object[]{"out"},
                new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> getDoctorUsersHome () {
        return jdbcTemplate.query("SELECT * FROM doctor WHERE type = ?", new Object[]{"home"},
                new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> getDoctorApps (int doctorId) {
        List<App> apps = jdbcTemplate.query("SELECT * FROM apped WHERE doctor_id = ?", new Object[]{doctorId},
                new BeanPropertyRowMapper<>(App.class));
        List<User> users = new ArrayList<>();
        for (App app : apps) {
            List<User> users1 = jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{app.getPerson_id()},
                    new BeanPropertyRowMapper<>(User.class));
            User user = users1.get(0);
            user.setTime(app.getTime());
            users.add(user);
        }
        return users;
    }

    public List<User> getPersonApps (int personId) {
        List<App> apps = jdbcTemplate.query("SELECT * FROM apped WHERE person_id = ?", new Object[]{personId},
                new BeanPropertyRowMapper<>(App.class));
        List<User> users = new ArrayList<>();
        for (App app : apps) {
            List<User> users1 = jdbcTemplate.query("SELECT * FROM doctor WHERE id = ?", new Object[]{app.getDoctor_id()},
                    new BeanPropertyRowMapper<>(User.class));
            User user = users1.get(0);
            user.setTime(app.getTime());
            users.add(user);
        }
        return users;
    }

    public List<Service> getServices () {
        return jdbcTemplate.query("SELECT * FROM service", new BeanPropertyRowMapper<>(Service.class));
    }

    public List<Appex> getApps() {
        List<App> apps = jdbcTemplate.query("SELECT * FROM apped", new BeanPropertyRowMapper<>(App.class));
        List<Appex> appexes = new ArrayList<>();
        for (App app : apps) {
            Appex appex;
            User person = jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{app.getPerson_id()},
                    new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
            User doctor = jdbcTemplate.query("SELECT * FROM doctor WHERE id = ?", new Object[]{app.getDoctor_id()},
                    new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
            if (person != null && doctor != null) {
                appex = new Appex(person.getData(), doctor.getDocData(), app.getTime());
                appexes.add(appex);
            }
        }
        return appexes;
    }

    public User showDoctor(int id) {
        return jdbcTemplate.query("SELECT * FROM doctor WHERE id = ?", new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    public User showPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    public void updateDoctor(int id, User user) {
        jdbcTemplate.update("UPDATE doctor SET surname = ?, name = ?, lastname = ?, email = ?, pass = ?, spec = ? WHERE id = ?",
                user.getSurname(), user.getName(), user.getLastname(), user.getEmail(), user.getPass(), user.getSpec(), id);
    }

    public void updatePerson(int id, User user) {
        jdbcTemplate.update("UPDATE person SET surname = ?, name = ?, lastname = ?, email = ?, pass = ? WHERE id = ?",
                user.getSurname(), user.getName(), user.getLastname(), user.getEmail(), user.getPass(), id);
    }

    public void deleteDoctor(int id) {
        List<App> apps =jdbcTemplate.query("SELECT * FROM apped WHERE doctor_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(App.class));
        if (!apps.isEmpty()) {
            jdbcTemplate.update("DELETE FROM apped WHERE doctor_id = ?", id);
        }
        jdbcTemplate.update("DELETE FROM doctor WHERE id = ?", id);
    }

    public void deletePerson(int id) {
        List<App> apps =jdbcTemplate.query("SELECT * FROM apped WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(App.class));
        List<Lists> lists =jdbcTemplate.query("SELECT * FROM list WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Lists.class));
        if (!apps.isEmpty()) {
            jdbcTemplate.update("DELETE FROM apped WHERE person_id = ?", id);
        }
        if (!lists.isEmpty()) {
            jdbcTemplate.update("DELETE FROM list WHERE person_id = ?", id);
        }
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }

    public Service showService(int id) {
        return jdbcTemplate.query("SELECT * FROM service WHERE id = ?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Service.class)).stream().findAny().orElse(null);
    }

    public void updateService(int id, Service service) {
        jdbcTemplate.update("UPDATE service SET name = ?, cost = ? WHERE id = ?",
                service.getName(), service.getCost(), id);
    }

    public void deleteService(int id) {
        jdbcTemplate.update("DELETE FROM service WHERE id = ?", id);
    }

    public void appendService(Service service) {
        jdbcTemplate.update("INSERT INTO service (name, cost) VALUES (?, ?)", service.getName(), service.getCost());
    }

    public int getStatus() {
        User user =jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{this.getId()},
                new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
        if (user != null) {
            return user.getStatus();
        }
        return 0;
    }

    public void insertCart(String gender, String bd, String adress, String omsnumber, String snilsnumber,
                           String pseries, String pnumber, int id) {
        Date date = new Date();
        User user = jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
        String str = String.format("Cart registration date: day %d month %d year %d\nSurname %s name %s last name %s\n" +
                        "Gender %s birth day %s\nAdress %s\nOMS number %s\nSnils number %s\nPasport: series %s number %s",
                date.getDate(), date.getMonth() + 1, date.getYear() + 1900, user.getSurname(), user.getName(),
                user.getLastname(), gender, bd, adress, omsnumber, snilsnumber, pseries, pnumber);
        jdbcTemplate.update("UPDATE person SET about = ?, status = 1 WHERE id = ?", str, id);
    }

    public void appoint(int pid, int did, String time) {
        jdbcTemplate.update("INSERT INTO apped (person_id, doctor_id, time) VALUES (?, ?, ?)", pid, did, time);
    }

    public List<Lists> getLists(int id) {
        return jdbcTemplate.query("SELECT * FROM list WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Lists.class));
    }

    public void appendList(int id, String date, String type, String complaint, String diagnosis, String assignment) {
        jdbcTemplate.update("INSERT INTO list (person_id, date, type, complaint, diagnosis, assignment) " +
                "VALUES (?, ?, ?, ?, ?, ?)", id, date, type, complaint, diagnosis, assignment);
    }

    public List<String> getTimes(int id) {
        List<App> apps = jdbcTemplate.query("SELECT * FROM apped WHERE doctor_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(App.class));
        List<String> times = setAllTimes();
        for (App app : apps) {
            times.remove(app.getTime());
        }
        return times;
    }
}