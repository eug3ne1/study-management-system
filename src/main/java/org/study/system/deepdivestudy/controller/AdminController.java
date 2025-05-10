package org.study.system.deepdivestudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/admin")
public class AdminController {
//
//    AdminService adminService;
//
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping("/students")
//    public ResponseEntity<List<User>> getStudents() {
//        List<User> allStudents = adminService.getAllStudents();
//        return new ResponseEntity<>(allStudents, HttpStatus.OK);
//    }
//
//    @GetMapping("/teachers")
//    public ResponseEntity<List<User>> getTeachers() {
//        List<User> allTeachers = adminService.getAllTeachers();
//        return new ResponseEntity<>(allTeachers, HttpStatus.OK);
//    }
//
//
//
//    @GetMapping("/settings")
//    public String getAdminSettings() {
//        return "Тут налаштування для адміністратора.";
//    }
}
