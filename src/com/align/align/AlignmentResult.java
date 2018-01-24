package com.align.align;

import com.align.fastaparser.Sequence;

public class AlignmentResult {

    private final Sequence sequence;
    private final long score;
    private final String alignedSequence;

    public AlignmentResult(Sequence sequence, long score, String alignedSequence) {
        this.sequence = sequence;
        this.score = score;
        this.alignedSequence = alignedSequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public long getScore() {
        return score;
    }

    public String getAlignedSequence() {
        return alignedSequence;
    }
}
