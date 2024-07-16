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
import java.util.Date;

@Controller
@RequestMapping("/")
public class DoctorController {
    private final DAO dao;

    @Autowired
    public DoctorController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/da") //All appointments for doctor
    public String doctorApps(Model model) throws IOException {
        if (dao.getRole().equals("doctor")) {
            model.addAttribute("persons", dao.getDoctorApps(dao.getId()));
            return "doctor/apps";
        }
        return "access";
    }

    @PostMapping("/lap")
    public String appendList(@RequestParam("id") int id, Model model) throws IOException {
        model.addAttribute("id", id);
        Date date = new Date();
        String datestr = String.format("%d.%d.%d", date.getDate(), date.getMonth() + 1, date.getYear() + 1900);
        model.addAttribute("date", datestr);
        return "doctor/lap";
    }

    @PostMapping("/lapres")
    public String appendListResult(@RequestParam("id") int id,
                                   @RequestParam("date") String date,
                                   @RequestParam("type") String type,
                                   @RequestParam("complaint") String complaint,
                                   @RequestParam("diagnosis") String diagnosis,
                                   @RequestParam("assignment") String assignment,
                                   Model model) throws IOException {
        if(type.isEmpty() || complaint.isEmpty() || diagnosis.isEmpty() || assignment.isEmpty()) {
            model.addAttribute("message", "List append failed");
            return "fail";
        }
        dao.appendList(id, date, type, complaint, diagnosis, assignment);
        model.addAttribute("message", "List append succesful");
        return "success";
    }
}
