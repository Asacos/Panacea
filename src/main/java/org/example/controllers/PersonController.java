package org.example.controllers;

import org.example.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class PersonController {
    private final DAO dao;

    @Autowired
    public PersonController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/pd") //All doctors for person
    public String personDoctors(Model model) throws IOException {
        if (dao.getRole().equals("person")) {
            model.addAttribute("doctorsout", dao.getDoctorUsersOut());
            model.addAttribute("doctorshome", dao.getDoctorUsersHome());
            if (dao.getStatus() == 1) {
                return "person/doctorsf";
            }
            return "person/doctorsu";
        }
        return "access";
    }

    @GetMapping("/pa") //All appointments for person
    public String personApps(Model model) throws IOException {
        if (dao.getRole().equals("person")) {
            model.addAttribute("doctors", dao.getPersonApps(dao.getId()));
            return "person/apps";
        }
        return "access";
    }

    @GetMapping("/ps") //All services for person
    public String personServices(Model model) throws IOException {
        if (dao.getRole().equals("person")) {
            model.addAttribute("services", dao.getServices());
            return "person/services";
        }
        return "access";
    }

    @PostMapping("/pad")
    public String appoint(@RequestParam("id") int id, @RequestParam("time") String time, Model model) throws IOException {
        dao.appoint(dao.getId(), id, time);
        model.addAttribute("message", "Appointment successful");
        return "success";
    }

    @GetMapping("/up")
    public String up() throws IOException {
        if (dao.getRole().equals("person")) {
            return "person/upgrade";
        }
        return "access";
    }

    @PostMapping("/upres") //register result
    public String regRes(@RequestParam("gender") String gender,
                         @RequestParam("bd") String bd,
                         @RequestParam("adress") String adress,
                         @RequestParam("omsnumber") String omsnumber,
                         @RequestParam("snilsnumber") String snilsnumber,
                         @RequestParam("pseries") String pseries,
                         @RequestParam("pnumber") String pnumber,
                         Model model) throws IOException {
        if (gender.isEmpty() || bd.isEmpty() || adress.isEmpty() || omsnumber.isEmpty() || snilsnumber.isEmpty() ||
                pseries.isEmpty() || pnumber.isEmpty()) {
            model.addAttribute("message", "Upgrade failed");
            return "fail";
        }
        dao.insertCart(gender, bd, adress, omsnumber, snilsnumber, pseries, pnumber, dao.getId());
        model.addAttribute("message", "Upgrade successful");
        return "success";
    }
}
