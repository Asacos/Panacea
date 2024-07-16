package org.example.controllers;

import org.example.dao.DAO;
import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Controller
@RequestMapping("/")
public class LoginPageController {

    private final DAO dao;

    private boolean isValid(String email) {
        return email.matches("^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$");
    }

    @Autowired
    public LoginPageController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/index") //starter page
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login") //login page
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register") //register page
    public String regPage() {
        return "register";
    }

    @PostMapping("/regres") //register result
    public String regRes(@RequestParam("surname") String surname,
                         @RequestParam("name") String name,
                         @RequestParam("lastname") String lastname,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         Model model) throws NoSuchAlgorithmException, IOException {
        if((surname.length() > 2) && (surname.length() < 30) &&
           (name.length() > 2) && (name.length() < 30) &&
           (lastname.length() > 2) && (lastname.length() < 30) &&
           (isValid(email)) && (password.length() > 6) && (password.length() < 30)) {
            if (dao.isPersonExist(email) || dao.isDoctorExist(email) || dao.isAdminExist(email)) {
                model.addAttribute("message", "This user already exists");
                return "failind";
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            password = Base64.getEncoder().encodeToString(hash);
            User user = new User(surname,name,lastname,email,password);
            dao.appendPersonRow(user);
            model.addAttribute("message", "Register successful");
            return "sucind";
        }
        model.addAttribute("message", "Register failed");
        return "failind";
    }

    @PostMapping("/logres") //login result
    public String logRes (@RequestParam("email") String email,
                          @RequestParam("password") String password, Model model) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(password.getBytes());
        password = Base64.getEncoder().encodeToString(hash);
        if(dao.isPersonExist(email) && dao.checkPersonPassword(email, password)) {
            dao.setRole("person");
            dao.setId(dao.getPersonID(email));
            model.addAttribute("message", "Login successful");
            return "success";
        } else if (dao.isDoctorExist(email) && dao.checkDoctorPassword(email, password)) {
            dao.setRole("doctor");
            dao.setId(dao.getDoctorID(email));
            model.addAttribute("message", "Login successful");
            return "success";
        } else if (dao.isAdminExist(email) && dao.checkAdminPassword(email, password)) {
            dao.setRole("admin");
            dao.setId(dao.getAdminID(email));
            model.addAttribute("message", "Login successful");
            return "success";
        }
        model.addAttribute("message", "Login failed");
        return "failind";
    }

    @GetMapping("/main") //main page
    public String mainPage() {
        if (!dao.getRole().equals("none")) {
           switch (dao.getRole()) {
               case "person":
                   if (dao.getStatus() == 1) {
                       return "main/personf";
                   }
                   return "main/personu";
               case "doctor":
                   return "main/doctor";
               case "admin":
                   return "main/admin";
           }
        }
        return "login";
    }

    @GetMapping("/lo") //logout
    public String logout(Model model) throws IOException {
        dao.setRole("none");
        dao.setId(0);
        model.addAttribute("message", "Logout successful");
        return "sucind";
    }
}