package com.pharmames.controller;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.ElectronicBatchRecord;
import com.pharmames.service.BatchService;
import com.pharmames.service.ElectronicBatchRecordService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ebr")
public class EbrController {

    private final ElectronicBatchRecordService ebrService;
    private final BatchService batchService;

    public EbrController(ElectronicBatchRecordService ebrService, BatchService batchService) {
        this.ebrService = ebrService;
        this.batchService = batchService;
    }

    @GetMapping
    public String listEbrRecords(Model model) {
        model.addAttribute("activeNav", "ebr");
        model.addAttribute("records", ebrService.getAllRecords());
        return "ebr/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "batchId", required = false) Long batchId,
                                 Model model) {
        model.addAttribute("activeNav", "ebr");
        ElectronicBatchRecord record = new ElectronicBatchRecord();

        if (batchId != null) {
            Batch batch = batchService.getBatchById(batchId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Batch ID: " + batchId));
            record.setBatch(batch);
            record.setStage(batch.getCurrentStage());
            record.setStepNumber(batch.getEbrRecords().size() + 1);
            model.addAttribute("selectedBatch", batch);
        }

        model.addAttribute("record", record);
        model.addAttribute("batches", batchService.getAllBatches());
        model.addAttribute("stages", BatchStage.values());

        return "ebr/create";
    }

    @PostMapping
    public String createEbrRecord(@RequestParam("batchId") Long batchId,
                                  @Valid @ModelAttribute("record") ElectronicBatchRecord record,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeNav", "ebr");
            model.addAttribute("batches", batchService.getAllBatches());
            model.addAttribute("stages", BatchStage.values());
            if (batchId != null) {
                batchService.getBatchById(batchId).ifPresent(b -> model.addAttribute("selectedBatch", b));
            }
            return "ebr/create";
        }

        try {
            ElectronicBatchRecord saved = ebrService.recordStep(batchId, record);
            redirectAttributes.addFlashAttribute("successMessage",
                    "EBR Step #" + saved.getStepNumber() + " ('" + saved.getStepName() + "') signed off and logged!");
            return "redirect:/batches/" + batchId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving EBR step: " + e.getMessage());
            return "redirect:/ebr/new?batchId=" + batchId;
        }
    }
}
