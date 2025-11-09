package com.skcodify.myshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skcodify.myshop.domain.Report;
import com.skcodify.myshop.service.ReportService;

@RestController
@RequestMapping("/reports")
// @CrossOrigin(origins = "*") // CORS is configured globally in WebConfig.java
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<Report> getReports() {
        return reportService.findAllReports();
    }
}