package org.launchcode.library.controllers;

import org.launchcode.library.data.StudentRepository;
import org.launchcode.library.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.launchcode.library.controllers.ListController.columnChoices;

@Controller
@RequestMapping("students/search")
public class SearchController {
    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("")
    public String search(Model model) {
        model.addAttribute("columns", columnChoices);
//        model.addAttribute("title", "Search Student");
//        model.addAttribute("Students", studentRepository.findAll());
        return "students/search";
    }

    @PostMapping("results")
    public String processSearchStudent(Model model, @RequestParam String searchType, @RequestParam Integer searchTerm) {
        if (searchTerm != null) {
            model.addAttribute("title", "Search Student");
            model.addAttribute("Students", studentRepository.findById(searchTerm));
        }
        return "students/update";
    }
}
