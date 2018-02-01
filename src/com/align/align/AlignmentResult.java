package com.align.align;

import com.align.fastaparser.Sequence;

/**
 * Rusultat der align Methoden in {@link Alignments}.
 * Beihaltet Sequenzen und das Alignment, sowie den Score
 *
 * @author Soeren Metje
 */
public class AlignmentResult {

    /**
     * Sequenzen
     */
    private final Sequence[] sequences;

    /**
     * score des Alignments
     */
    private final long score;

    /**
     * Alignments
     */
    private final String[] alignments;

    /**
     * Konstruktor
     *
     * @param sequences  Sequenzen
     * @param score      Score des Alignments
     * @param alignments Alignment
     */
    public AlignmentResult(Sequence[] sequences, long score, String[] alignments) {
        this.sequences = sequences;
        this.score = score;
        this.alignments = alignments;
    }

    /**
     * liefert Sequenzen zurueck
     *
     * @return Sequenzen
     */
    public Sequence[] getSequences() {
        return sequences;
    }

    /**
     * liefert Score des Alignments zurueck
     *
     * @return Score des Alignments
     */
    public long getScore() {
        return score;
    }

    /**
     * liefert Alignment zurueck
     *
     * @return Alignment
     */
    public String[] getAlignments() {
        return alignments;
    }
}
