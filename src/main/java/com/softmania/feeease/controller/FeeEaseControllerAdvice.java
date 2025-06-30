package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class FeeEaseControllerAdvice {
    private final UploadService uploadService;

    @Autowired
    public FeeEaseControllerAdvice(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication auth) {
        if(auth != null) {
            School school = ((UserData) auth.getPrincipal()).getUser().getSchool();
            model.addAttribute("SchoolName", school.getName().toUpperCase());
            model.addAttribute("Role", ((UserData) auth.getPrincipal()).getUser().getRole());
            String logourl = "/images/logo.png";
            if(school.getSchoolLogoKey() != null) {
                logourl = uploadService.generatePresignedUrl(school.getSchoolLogoKey()).getUrl();
            }
            model.addAttribute("SchoolLogo", logourl);
        }
    }
}
