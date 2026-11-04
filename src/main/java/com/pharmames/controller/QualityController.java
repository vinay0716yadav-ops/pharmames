package com.pharmames.controller;

import com.pharmames.model.Batch;
import com.pharmames.model.QualityRecord;
import com.pharmames.service.BatchService;
import com.pharmames.service.QualityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quality")
public class QualityController {

    private final QualityService qualityService;
    private final BatchService batchService;

    public QualityController(QualityService qualityService, BatchService batchService) {
        this.qualityService = qualityService;
        this.batchService = batchService;
    }

    @GetMapping
    public String listQualityRecords(Model model) {
        model.addAttribute("activeNav", "quality");
        model.addAttribute("records", qualityService.getAllRecords());
        model.addAttribute("batches", batchService.getAllBatches());
        model.addAttribute("approvedCount", qualityService.countByDisposition("APPROVED"));
        model.addAttribute("reviewCount", qualityService.countByDisposition("UNDER_REVIEW"));
        model.addAttribute("quarantinedCount", qualityService.countByDisposition("QUARANTINED"));
        model.addAttribute("rejectedCount", qualityService.countByDisposition("REJECTED"));
        return "quality/list";
    }

    @GetMapping("/{batchId}/review")
    public String showReviewForm(@PathVariable("batchId") Long batchId, Model model) {
        model.addAttribute("activeNav", "quality");
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Batch ID: " + batchId));

        QualityRecord record = new QualityRecord();
        record.setBatch(batch);
        record.setAssayPercentage(99.5);
        record.setDissolutionRate(92.0);
        record.setMoistureContent(1.8);
        record.setMicrobialStatus("Conforms to USP/EP Specifications (No growth observed)");

        model.addAttribute("batch", batch);
        model.addAttribute("record", record);
        return "quality/review";
    }

    @PostMapping("/{batchId}/review")
    public String submitReview(@PathVariable("batchId") Long batchId,
                               @Valid @ModelAttribute("record") QualityRecord record,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeNav", "quality");
            batchService.getBatchById(batchId).ifPresent(b -> model.addAttribute("batch", b));
            return "quality/review";
        }

        try {
            qualityService.submitReview(batchId, record);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Quality review submitted. Disposition: " + record.getDisposition());
            return "redirect:/batches/" + batchId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving QC review: " + e.getMessage());
            return "redirect:/quality/" + batchId + "/review";
        }
    }
}
