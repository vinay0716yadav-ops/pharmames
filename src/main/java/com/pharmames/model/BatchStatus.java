package com.pharmames.model;

public enum BatchStatus {
    PENDING("Pending Schedule", "badge-secondary"),
    IN_PROGRESS("In Progress", "badge-primary"),
    ON_HOLD("On Hold", "badge-warning"),
    COMPLETED("Completed", "badge-success"),
    REJECTED("Rejected", "badge-danger");

    private final String displayName;
    private final String badgeClass;

    BatchStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
