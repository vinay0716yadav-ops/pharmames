package com.pharmames.controller;

import com.pharmames.service.AuditService;
import com.pharmames.service.BatchService;
import com.pharmames.service.DashboardService;
import com.pharmames.service.ElectronicBatchRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final BatchService batchService;
    private final ElectronicBatchRecordService ebrService;
    private final AuditService auditService;

    public DashboardController(DashboardService dashboardService,
                               BatchService batchService,
                               ElectronicBatchRecordService ebrService,
                               AuditService auditService) {
        this.dashboardService = dashboardService;
        this.batchService = batchService;
        this.ebrService = ebrService;
        this.auditService = auditService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("activeNav", "dashboard");
        model.addAttribute("totalBatches", dashboardService.getTotalBatches());
        model.addAttribute("activeBatches", dashboardService.getActiveBatches());
        model.addAttribute("completedBatches", dashboardService.getCompletedBatches());
        model.addAttribute("pendingBatches", dashboardService.getPendingBatches());
        model.addAttribute("onHoldBatches", dashboardService.getOnHoldBatches());
        model.addAttribute("rejectedBatches", dashboardService.getRejectedBatches());
        model.addAttribute("stageDistribution", dashboardService.getStageDistribution());
        model.addAttribute("recentBatches", dashboardService.getRecentBatches());
        model.addAttribute("recentEbrRecords", ebrService.getRecentRecords());
        model.addAttribute("recentAuditLogs", auditService.getRecentLogs());

        return "dashboard";
    }
}
