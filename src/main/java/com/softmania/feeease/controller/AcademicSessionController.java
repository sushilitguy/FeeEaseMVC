package com.softmania.feeease.controller;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.service.AcademicSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fee_ease/sessions")
class AcademicSessionController {
    @Autowired
    private AcademicSessionService service;

    @GetMapping
    public String getAllSessions(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Session> allSessions = service.getAllSessions(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("sessions", allSessions);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "session";
    }

    @PostMapping
    @ResponseBody
    public AcademicSession createSession(@RequestBody AcademicSession session) {
        return service.createSession(session);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AcademicSession> updateSession(@PathVariable int id, @RequestBody AcademicSession session) {
        AcademicSession updatedSession = service.updateSession(id, session);
        return updatedSession != null ? ResponseEntity.ok(updatedSession) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSession(@PathVariable int id) {
        service.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}