package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Report;
import com.skcodify.myshop.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }
}