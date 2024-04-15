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

import java.util.ArrayList;
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

    @PostMapping("update")
    public String displayStudents(@RequestParam(required = false) String searchType, String searchTerm, Model model) {
        model.addAttribute("title", "Student Management");
        Iterable<Student> students;
        Integer studentId = null;
        if (searchType == null) {
            students = studentRepository.findAll();
            model.addAttribute("students", students);
            return "students/index";
        } else if (searchType.equals("id")) {
            if (searchTerm == null || searchTerm.isEmpty()) {
                students = studentRepository.findAll();
                model.addAttribute("students", students);
                return "students/index";
            } else {
                Optional<Student> optionalStudent = studentRepository.findById(Integer.valueOf(searchTerm));
                if (optionalStudent.isPresent()) {
                    Student student = optionalStudent.get();
                    model.addAttribute("studentId", student.getId());
                    model.addAttribute("studentfirstname", student.getFirstname());
                    model.addAttribute("studentlastname", student.getLastname());
                    model.addAttribute("studentcontactemail", student.getContactEmail());
                    return "students/update";
                } else {
                    model.addAttribute("error","Id "+ searchTerm + " not found");
                    model.addAttribute("columns", columnChoices);
                    return "students/search";
                }
            }
        } else if (searchType.equals("studentfn")) {
            students = studentRepository.findAll();
            ArrayList<Student> searchstudent = new ArrayList<>();
            for (Student student : students) {
                if (student.getFirstname().toString().toLowerCase().contains(searchTerm.toLowerCase())) {
                    searchstudent.add(student);
                }
            }
            model.addAttribute("students", searchstudent);
            return "students/index";
        } else if (searchType.equals("studentln")) {
            students = studentRepository.findAll();
            ArrayList<Student> searchstudent = new ArrayList<>();
            for (Student student : students) {
                if (student.getLastname().toString().toLowerCase().contains(searchTerm.toLowerCase())) {
                    searchstudent.add(student);
                }
            }
            model.addAttribute("students", searchstudent);
            return "students/index";
        }
        return "students/update";
    }

}
