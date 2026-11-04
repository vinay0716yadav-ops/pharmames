package com.pharmames.model;

public enum BatchStage {
    DISPENSING("Weighing & Dispensing", 1),
    GRANULATION("Granulation & Blending", 2),
    COMPRESSION("Compression / Filling", 3),
    COATING("Coating & Inspection", 4),
    PACKAGING("Packaging & Serialization", 5),
    QUALITY_CONTROL("QC Lab Testing & Review", 6),
    RELEASED("Finished Goods Released", 7);

    private final String displayName;
    private final int sequence;

    BatchStage(String displayName, int sequence) {
        this.displayName = displayName;
        this.sequence = sequence;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSequence() {
        return sequence;
    }

    public BatchStage getNextStage() {
        BatchStage[] stages = BatchStage.values();
        for (int i = 0; i < stages.length; i++) {
            if (stages[i] == this && i + 1 < stages.length) {
                return stages[i + 1];
            }
        }
        return this; // At final stage
    }

    public int getProgressPercentage() {
        return (int) Math.round((double) sequence / BatchStage.values().length * 100);
    }
}
