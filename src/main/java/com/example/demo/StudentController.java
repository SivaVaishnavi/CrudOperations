package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // DASHBOARD (HOME PAGE)
    @GetMapping("/")
    public String viewDashboard(Model model) {

        List<Student> allStudents = studentRepository.findAll();

        long totalStudents    = allStudents.size();
        long activeStudents   = studentRepository.countStudentsByStatus("Active");
        long inactiveStudents = studentRepository.countStudentsByStatus("Inactive");

        // Build course chart data safely
        List<Object[]> courseData  = studentRepository.getCountByCourse();
        List<String>   courseLabels = new ArrayList<>();
        List<Long>     courseCounts = new ArrayList<>();

        for (Object[] row : courseData) {
            courseLabels.add(row[0] != null ? row[0].toString() : "Unknown");
            courseCounts.add(((Number) row[1]).longValue());
        }

        model.addAttribute("listStudents",    allStudents);
        model.addAttribute("totalStudents",   totalStudents);
        model.addAttribute("activeStudents",  activeStudents);
        model.addAttribute("inactiveStudents", inactiveStudents);
        model.addAttribute("courseLabels",    courseLabels);
        model.addAttribute("courseCounts",    courseCounts);

        return "index";
    }

    // SHOW ADD FORM
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("student", new Student());
        return "new_student";
    }

    // SAVE NEW STUDENT
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentRepository.save(student);
        return "redirect:/";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id: " + id));
        model.addAttribute("student", student);
        return "edit_student";
    }

    // UPDATE STUDENT
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") Student student) {
        student.setId(id);
        studentRepository.save(student);
        return "redirect:/";
    }

    // DELETE STUDENT
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/";
    }
}