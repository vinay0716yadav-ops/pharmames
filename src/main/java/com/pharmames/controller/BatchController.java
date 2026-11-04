package com.pharmames.controller;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import com.pharmames.service.AuditService;
import com.pharmames.service.BatchService;
import com.pharmames.service.ElectronicBatchRecordService;
import com.pharmames.service.QualityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/batches")
public class BatchController {

    private final BatchService batchService;
    private final ElectronicBatchRecordService ebrService;
    private final QualityService qualityService;
    private final AuditService auditService;

    public BatchController(BatchService batchService,
                           ElectronicBatchRecordService ebrService,
                           QualityService qualityService,
                           AuditService auditService) {
        this.batchService = batchService;
        this.ebrService = ebrService;
        this.qualityService = qualityService;
        this.auditService = auditService;
    }

    @GetMapping
    public String listBatches(@RequestParam(value = "status", required = false) BatchStatus status,
                              @RequestParam(value = "search", required = false) String search,
                              Model model) {
        model.addAttribute("activeNav", "batches");
        List<Batch> batches;

        if (search != null && !search.trim().isEmpty()) {
            batches = batchService.searchBatches(search);
            model.addAttribute("search", search);
        } else if (status != null) {
            batches = batchService.filterByStatus(status);
            model.addAttribute("selectedStatus", status);
        } else {
            batches = batchService.getAllBatches();
        }

        model.addAttribute("batches", batches);
        model.addAttribute("statuses", BatchStatus.values());
        return "batches/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("activeNav", "batches");
        model.addAttribute("batch", new Batch());
        model.addAttribute("stages", BatchStage.values());
        model.addAttribute("statuses", BatchStatus.values());
        return "batches/create";
    }

    @PostMapping
    public String createBatch(@Valid @ModelAttribute("batch") Batch batch,
                              BindingResult bindingResult,
                              @RequestParam(value = "operatorName", required = false) String operatorName,
                              @RequestParam(value = "reason", required = false) String reason,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeNav", "batches");
            model.addAttribute("stages", BatchStage.values());
            model.addAttribute("statuses", BatchStatus.values());
            return "batches/create";
        }

        try {
            batchService.createBatch(batch, operatorName, reason);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Batch " + batch.getBatchNumber() + " successfully initiated!");
            return "redirect:/batches";
        } catch (Exception e) {
            model.addAttribute("activeNav", "batches");
            model.addAttribute("errorMessage", "Error creating batch: " + e.getMessage());
            model.addAttribute("stages", BatchStage.values());
            model.addAttribute("statuses", BatchStatus.values());
            return "batches/create";
        }
    }

    @GetMapping("/{id}")
    public String viewBatch(@PathVariable("id") Long id, Model model) {
        model.addAttribute("activeNav", "batches");
        Batch batch = batchService.getBatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Batch ID: " + id));

        model.addAttribute("batch", batch);
        model.addAttribute("ebrRecords", ebrService.getRecordsForBatch(id));
        model.addAttribute("qualityRecords", qualityService.getRecordsForBatch(id));
        model.addAttribute("auditLogs", auditService.getLogsForBatch(batch.getBatchNumber()));
        model.addAttribute("stages", BatchStage.values());
        model.addAttribute("statuses", BatchStatus.values());

        return "batches/view";
    }

    @PostMapping("/{id}/advance")
    public String advanceStage(@PathVariable("id") Long id,
                               @RequestParam(value = "operatorName", defaultValue = "Line Supervisor") String operatorName,
                               @RequestParam(value = "reason", defaultValue = "Unit operation signed off and validated") String reason,
                               RedirectAttributes redirectAttributes) {
        try {
            Batch updated = batchService.advanceStage(id, operatorName, reason);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Batch " + updated.getBatchNumber() + " advanced to: " + updated.getCurrentStage().getDisplayName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not advance batch: " + e.getMessage());
        }
        return "redirect:/batches/" + id;
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") BatchStatus status,
                               @RequestParam(value = "operatorName", defaultValue = "Shift Lead") String operatorName,
                               @RequestParam(value = "reason", defaultValue = "Operational directive") String reason,
                               RedirectAttributes redirectAttributes) {
        try {
            Batch updated = batchService.updateStatus(id, status, operatorName, reason);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Batch " + updated.getBatchNumber() + " status updated to: " + updated.getStatus().getDisplayName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not update status: " + e.getMessage());
        }
        return "redirect:/batches/" + id;
    }
}
