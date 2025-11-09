package com.skcodify.myshop.domain;

/**
 * Defines the status of a report generation task.
 */
public enum ReportStatus {
    /** The report generation request is queued and waiting to be processed. */
    PENDING,
    /** The report is currently being generated. */
    PROCESSING,
    /** The report has been successfully generated and is available. */
    COMPLETED,
    /** The report generation failed due to an error. */
    FAILED
}