package com.softmania.feeease.controller;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import com.softmania.feeease.model.School;
import com.softmania.feeease.model.SessionType;
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

    @GetMapping("/add")
    public String showAddUserForm(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "sessionForm";
    }

    @GetMapping("/update/{sessionId}")
    public String editUserForm(Authentication auth, Model model, @PathVariable int sessionId) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        AcademicSession academicSession = service.getSessionById(sessionId).orElse(null);
        if(academicSession != null) {
            Session sessionData = new Session(academicSession.getId(), academicSession.getSessionName(), academicSession.getSessionType().toString());
            model.addAttribute("sessionData", sessionData);
        }
        return "sessionForm";
    }

    @PostMapping("/add")
    public String createSession(Authentication auth, Model model, @RequestParam("sessionName") String sessionName, @RequestParam("sessionType") String sessionType) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        AcademicSession session = new AcademicSession();
        session.setSessionName(sessionName);
        session.setSessionType(SessionType.valueOf(sessionType.toUpperCase()));
        session.setSchool(school);
        AcademicSession savedSession = service.createSession(session);
        if(savedSession != null) {
            model.addAttribute("successMessage", "Session Added Successfully");
        } else {
            model.addAttribute("errorMessage","Error while adding session, Please try again");
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "sessionForm";
    }

    @PostMapping("/update")
    public String updateSession(Authentication auth, Model model, @RequestParam("sessionId") int sessionId, @RequestParam("sessionName") String sessionName, @RequestParam("sessionType") String sessionType) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        AcademicSession session = service.getSessionById(sessionId).orElse(null);
        if(session != null) {
            session.setSessionName(sessionName);
            session.setSessionType(SessionType.valueOf(sessionType.toUpperCase()));
            AcademicSession savedSession = service.updateSession(session);
            if (savedSession != null) {
                model.addAttribute("successMessage", "Session Added Successfully");
            } else {
                model.addAttribute("errorMessage", "Error while adding session, Please try again");
            }
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        List<Session> allSessions = service.getAllSessions(school.getId());
        model.addAttribute("sessions", allSessions);
        return "session";
    }
}