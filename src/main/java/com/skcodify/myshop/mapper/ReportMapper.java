package com.skcodify.myshop.mapper;

import com.skcodify.myshop.domain.Report;
import com.skcodify.myshop.dto.ReportDto;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportDto toDto(Report report) {
        if (report == null) {
            return null;
        }

        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setReportType(report.getReportType());
        dto.setStatus(report.getStatus());
        dto.setRequestedAt(report.getRequestedAt());
        dto.setStartDate(report.getStartDate());
        dto.setEndDate(report.getEndDate());
        dto.setFileUrl(report.getFileUrl());

        if (report.getRequestedBy() != null) {
            dto.setRequestedByUserId(report.getRequestedBy().getId());
        }

        return dto;
    }
}