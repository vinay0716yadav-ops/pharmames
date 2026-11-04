package com.pharmames.controller;

import com.pharmames.model.AuditTrail;
import com.pharmames.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public String showAuditTrail(@RequestParam(value = "search", required = false) String search,
                                 Model model) {
        model.addAttribute("activeNav", "audit");
        List<AuditTrail> logs;

        if (search != null && !search.trim().isEmpty()) {
            logs = auditService.searchLogs(search);
            model.addAttribute("search", search);
        } else {
            logs = auditService.getAllLogs();
        }

        model.addAttribute("auditLogs", logs);
        return "audit/list";
    }
}
