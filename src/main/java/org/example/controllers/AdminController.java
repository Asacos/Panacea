package org.example.controllers;

import org.example.dao.DAO;
import org.example.models.Service;
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
public class AdminController {
    private final DAO dao;

    private boolean isValid(String email) {
        return email.matches("^[\\w-.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$");
    }

    @Autowired
    public AdminController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/ap") //All persons for admin
    public String adminPersons(Model model) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("persons", dao.getPersonUsers());
            return "admin/person/persons";
        }
        return "access";
    }

    @GetMapping("/ad") //All doctors for admin
    public String adminDoctors(Model model) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("doctorsout", dao.getDoctorUsersOut());
            model.addAttribute("doctorshome", dao.getDoctorUsersHome());
            return "admin/doctor/doctors";
        }
        return "access";
    }

    @GetMapping("/aa") //All appointments for admin
    public String adminApps(Model model) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("apps", dao.getApps());
            return "admin/apps";
        }
        return "access";
    }

    @GetMapping("/as") //All services for admin
    public String adminServices(Model model) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("services", dao.getServices());
            return "admin/service/services";
        }
        return "access";
    }

    @GetMapping("/doctor/{id}")
    public String showDoctor(@PathVariable("id") int id, Model model) throws IOException {
        model.addAttribute("doctor", dao.showDoctor(id));
        if (dao.getRole().equals("admin")) {
            return "admin/doctor/showdoctor";
        }
        if (dao.getRole().equals("person")) {
            model.addAttribute("times", dao.getTimes(id));
            return "person/showdoctor";
        }
        return "access";
    }

    @GetMapping("/person/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) throws IOException {
        model.addAttribute("person", dao.showPerson(id));
        model.addAttribute("lists", dao.getLists(id));
        if (dao.getRole().equals("admin")) {
            return "admin/person/showperson";
        }
        if (dao.getRole().equals("doctor")) {
            return "doctor/showperson";
        }
        return "access";
    }

    //admin doctor pref
    @GetMapping("doctor/{id}/edit")
    public String editDoctor(Model model, @PathVariable("id") int id) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("doctor", dao.showDoctor(id));
            return "admin/doctor/editdoctor";
        }
        return "access";
    }

    @PatchMapping("doctor/{id}")
    public String updateDoctor(@ModelAttribute("doctor") User user, @PathVariable("id") int id, Model model) throws IOException {
        dao.updateDoctor(id, user);
        model.addAttribute("message", "Edit successful");
        return "success";
    }

    @DeleteMapping("doctor/{id}")
    public String deleteDoctor(@PathVariable("id") int id, Model model) throws IOException {
        dao.deleteDoctor(id);
        model.addAttribute("message", "Delete successful");
        return "success";
    }

    @GetMapping("/dap")
    public String appendDoctor() throws IOException {
        if (dao.getRole().equals("admin")) {
            return "admin/doctor/appenddoctor";
        }
        return "access";
    }

    @PostMapping("/dapres")
    public String appendDoctorResult(@RequestParam("surname") String surname,
                                     @RequestParam("name") String name,
                                     @RequestParam("lastname") String lastname,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("spec") String spec,
                                     @RequestParam("about") String about,
                                     @RequestParam("type") String type,
                                     Model model) throws NoSuchAlgorithmException, IOException {
        if((surname.length() > 2) && (surname.length() < 30) &&
                (name.length() > 2) && (name.length() < 30) &&
                (lastname.length() > 2) && (lastname.length() < 30) &&
                (isValid(email)) && (password.length() > 6) && (password.length() < 30)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            password = Base64.getEncoder().encodeToString(hash);
            User user = new User(surname,name,lastname,email,password, spec, about, type);
            dao.appendDoctorRow(user);
            model.addAttribute("message", "Doctor append successful");
            return "success";
        }
        model.addAttribute("message", "Doctor append failed");
        return "fail";
    }

    //admin person pref
    @GetMapping("person/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("person", dao.showPerson(id));
            return "admin/person/editperson";
        }
        return "access";
    }

    @PatchMapping("person/{id}")
    public String update(@ModelAttribute("person") User user, @PathVariable("id") int id, Model model) throws IOException {
        dao.updatePerson(id, user);
        model.addAttribute("message", "Edit successful");
        return "success";
    }

    @DeleteMapping("person/{id}")
    public String deletePerson(@PathVariable("id") int id, Model model) throws IOException {
        dao.deletePerson(id);
        model.addAttribute("message", "Delete successful");
        return "success";
    }

    @GetMapping("/pap")
    public String appendPerson() throws IOException {
        if (dao.getRole().equals("admin")) {
            return "admin/person/appendperson";
        }
        return "access";
    }

    @PostMapping("/papres")
    public String appendPersonResult(@RequestParam("surname") String surname,
                                     @RequestParam("name") String name,
                                     @RequestParam("lastname") String lastname,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     Model model) throws NoSuchAlgorithmException, IOException {
        if((surname.length() > 2) && (surname.length() < 30) &&
                (name.length() > 2) && (name.length() < 30) &&
                (lastname.length() > 2) && (lastname.length() < 30) &&
                (isValid(email)) && (password.length() > 6) && (password.length() < 30)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            password = Base64.getEncoder().encodeToString(hash);
            User user = new User(surname,name,lastname,email,password);
            dao.appendPersonRow(user);
            model.addAttribute("message", "Person append successful");
            return "success";
        }
        model.addAttribute("message", "Person append failed");
        return "fail";
    }

    //admin service pref (add pages)
    @GetMapping("/service/{id}")
    public String showService(@PathVariable("id") int id, Model model) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("service", dao.showService(id));
            return "admin/service/showservice";
        }
        return "access";
    }

    @GetMapping("service/{id}/edit")
    public String editService(Model model, @PathVariable("id") int id) throws IOException {
        if (dao.getRole().equals("admin")) {
            model.addAttribute("service", dao.showService(id));
            return "admin/service/editservice";
        }
        return "access";
    }

    @PatchMapping("service/{id}")
    public String updateService(@ModelAttribute("service") Service service, @PathVariable("id") int id, Model model) throws IOException {
        dao.updateService(id, service);
        model.addAttribute("message", "Edit successful");
        return "success";
    }

    @DeleteMapping("service/{id}")
    public String deleteService(@PathVariable("id") int id, Model model) throws IOException {
        dao.deleteService(id);
        model.addAttribute("message", "Delete successful");
        return "success";
    }

    @GetMapping("/sap")
    public String appendService() throws IOException {
        if (dao.getRole().equals("admin")) {
            return "admin/service/appendservice";
        }
        return "access";
    }

    @PostMapping("/sapres")
    public String appendServiceResult(@RequestParam("name") String name,
                                      @RequestParam("cost") String cost,
                                      Model model) throws IOException {
        Service service = new Service(name, cost);
        dao.appendService(service);
        model.addAttribute("message", "Service append successful");
        return "success";
    }
}
