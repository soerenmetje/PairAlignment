package com.align.align;

import com.align.fastaparser.Sequence;

public class AlignmentResult {

    private final Sequence[] sequences;
    private final long score;
    private final String[] alignments;

    public AlignmentResult(Sequence[] sequences, long score, String[] alignments) {
        this.sequences = sequences;
        this.score = score;
        this.alignments = alignments;
    }

    public Sequence[] getSequences() {
        return sequences;
    }

    public long getScore() {
        return score;
    }

    public String[] getAlignments() {
        return alignments;
    }
}
