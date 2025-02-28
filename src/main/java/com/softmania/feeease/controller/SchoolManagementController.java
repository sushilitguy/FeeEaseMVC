package com.softmania.feeease.controller;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.*;
import com.softmania.feeease.service.SchoolManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fee_ease/school")
class SchoolManagementController {
    @Autowired
    private SchoolManagementService service;

    @GetMapping("/sessions")
    public String getAllSessions(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Session> allSessions = service.getAllSessions(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("sessions", allSessions);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "session";
    }

    @GetMapping("/sessions/add")
    public String showAddUserForm(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "sessionForm";
    }

    @GetMapping("/sessions/update/{sessionId}")
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

    @PostMapping("/sessions/add")
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

    @PostMapping("/sessions/update")
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

    @GetMapping("/standards")
    public String getAllStandards(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Standard> allStandards = service.getAllStandards(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        if(allStandards.isEmpty()) {
            model.addAttribute("errorMessage","No Standard Data is present, please add Standard Data.");
        }
        model.addAttribute("standards", allStandards);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "standard";
    }

    @PostMapping("/standards/add")
    public String addStandard(Authentication auth, Model model, @RequestParam("standardName") String standardName) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Standard standard = new Standard();
        standard.setStandardName(standardName);
        standard.setSchool(school);
        Standard savedStandard = service.addStandard(standard);
        String errorMsg = "";
        if(savedStandard == null) {
            errorMsg += "Error while Saving Standard. ";
        }
        List<Standard> allStandards = service.getAllStandards(school.getId());
        if (allStandards.isEmpty()) {
            errorMsg += "No Standard Data is present, please add Standard Data. ";
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("standards", allStandards);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        if(!errorMsg.isEmpty()) {
            model.addAttribute("errorMessage", errorMsg);
        }
        return "standard";
    }

    @PostMapping("/standards/update")
    public String updateStandard(Authentication auth, Model model, @RequestParam("standardId") int standardId, @RequestParam("standardName") String standardName) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Standard existingStandard = service.getStandardById(standardId);
        String errorMsg = "";
        if(existingStandard != null) {
            existingStandard.setStandardName(standardName);
            Standard savedStandard = service.updateStandard(existingStandard);
            if(savedStandard == null) {
                errorMsg = "Error while updating Standard. ";
            }
        } else {
            errorMsg = "No Standard exist with name : "+ standardName + ". ";
        }
        List<Standard> allStandards = service.getAllStandards(school.getId());
        if (allStandards.isEmpty()) {
            errorMsg += "No Standard Data is present, please add Standard Data. ";
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("standards", allStandards);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        if(!errorMsg.isEmpty()) {
            model.addAttribute("errorMessage", errorMsg);
        }
        return "standard";
    }

    @GetMapping("/sections")
    public String getAllSections(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Section> allSections = service.getAllSections(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        if(allSections.isEmpty()) {
            model.addAttribute("errorMessage","No Section Data is present, please add Section Data.");
        }
        model.addAttribute("sections", allSections);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "section";
    }

    @PostMapping("/sections/add")
    public String addSection(Authentication auth, Model model, @RequestParam("sectionName") String sectionName) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Section section = new Section();
        section.setSectionName(sectionName);
        section.setSchool(school);
        Section savedSection = service.addSection(section);
        String errorMsg = "";
        if(savedSection == null) {
            errorMsg += "Error while Saving Section. ";
        }
        List<Section> allSections = service.getAllSections(school.getId());
        if (allSections.isEmpty()) {
            errorMsg += "No Section Data is present, please add Section Data. ";
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("sections", allSections);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        if(!errorMsg.isEmpty()) {
            model.addAttribute("errorMessage", errorMsg);
        }
        return "section";
    }

    @PostMapping("/sections/update")
    public String updateSection(Authentication auth, Model model, @RequestParam("sectionId") int sectionId, @RequestParam("sectionName") String sectionName) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Section existingSection = service.getSectionById(sectionId).orElse(null);
        String errorMsg = "";
        if(existingSection != null) {
            existingSection.setSectionName(sectionName);
            Section savedSection = service.updateSection(existingSection);
            if(savedSection == null) {
                errorMsg = "Error while updating Section. ";
            }
        } else {
            errorMsg = "No Section exist with name : "+ sectionName + ". ";
        }
        List<Section> allSections = service.getAllSections(school.getId());
        if (allSections.isEmpty()) {
            errorMsg += "No Section Data is present, please add Section Data. ";
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("sections", allSections);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        if(!errorMsg.isEmpty()) {
            model.addAttribute("errorMessage", errorMsg);
        }
        return "standard";
    }
}