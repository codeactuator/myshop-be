package com.skcodify.myshop.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * Represents a generated report in the system (e.g., sales, inventory).
 * This entity is mapped to the "reports" table in the database.
 */
@Entity
@Table(name = "reports")
public class Report {

    /**
     * The unique identifier for the report.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the report being generated.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    /**
     * The current status of the report generation process.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    /**
     * The date and time when the report generation was requested.
     */
    @Column(nullable = false)
    private ZonedDateTime requestedAt;

    /**
     * The start date for the report's data range (inclusive).
     */
    private ZonedDateTime startDate;

    /**
     * The end date for the report's data range (inclusive).
     */
    private ZonedDateTime endDate;

    /**
     * The user (typically an admin) who requested the report.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_user_id")
    private User requestedBy;

    /**
     * The URL where the generated report file can be accessed.
     * This could be a link to a file in cloud storage (e.g., S3).
     */
    private String fileUrl;

    /**
     * Default constructor for JPA.
     */
    public Report() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public ZonedDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(ZonedDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}