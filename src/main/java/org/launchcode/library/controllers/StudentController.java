package org.launchcode.library.controllers;

import jakarta.validation.Valid;
import org.launchcode.library.data.StudentRepository;
import org.launchcode.library.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.launchcode.library.controllers.ListController.columnChoices;

@Controller
@RequestMapping ("students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public String displayAllStudents(@RequestParam(required=false) Integer studentId, Model model) {
        model.addAttribute("title", "Student Management");
        if (studentId == null) {
            model.addAttribute("students", studentRepository.findAll());
        } else {
            model.addAttribute("students", studentRepository.findById(studentId));
            }
        return "students/index";
    }


    @GetMapping("add")
    public String renderCreateStudentForm(Model model){
        model.addAttribute("title", "Create Student");
        model.addAttribute(new Student());
 //       model.addAttribute("students", studentRepository.findAll());
        return "students/add";
    }

    @PostMapping("add")
    public String createEvent(@ModelAttribute @Valid Student newStudent, Errors errors, Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Student");
            return "students/add";
        }
        studentRepository.save(newStudent);
        return "redirect:/students";

    }

    @GetMapping ("delete")
    public String displayDeleteStudentForm (Model model){
        model.addAttribute("title", "Delete Student");
        model.addAttribute("students", studentRepository.findAll());
        return "students/delete";
    }

    @PostMapping ("delete")
    public String processDeleteStudent(@RequestParam(required = false) int[] StudentIds){
        if (StudentIds != null)
        {
            for (int id : StudentIds) {
                studentRepository.deleteById(id);
            }
        }
        return "redirect:/students";

    }

    @PostMapping ("update")
    public String updateStudents(@RequestParam(required=false) Model model) {
//        Student newStudent1 = new Student();
//        model.addAttribute("title", "Student Management");
//        if (studentId == null) {
//            model.addAttribute("students", studentRepository.findAll());
//        } else {
//            newStudent1.setFirstname() = newStudent.getFirstname();
//
//            studentRepository.findById(studentId).gfirstname)
//            model.addAttribute("students", studentRepository.findById(studentId));
//        }
        return "students/index";
    }
//    @RequestMapping("search")
//    public String search(Model model) {
//        model.addAttribute("columns", columnChoices);
////        model.addAttribute("title", "Search Student");
////        model.addAttribute("Students", studentRepository.findAll());
//        return "search";
//    }
    @PostMapping ("add/view")
    public String processViewStudent(@RequestParam(required = false) int[] studentId, Model model){
        if (studentId != null)
        {
            for (int id : studentId) {
                model.addAttribute("Student ID", studentRepository.findById(id));
            }
        }
        return "students/search";

    }
}
