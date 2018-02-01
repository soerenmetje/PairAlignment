package com.align.align;

import com.align.AlignmentsMain;
import com.align.fastaparser.Sequence;

/**
 * Enthaelt Methoden zur Suche des optimalen globale oder lokalen Alignments zweier Sequenzen.
 * <p>
 * Smith-Waterman-Algorithmus
 * Quelle: https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm
 *
 * @author Soeren Metje
 */
public class Alignments {

    /**
     * Versatz-Kombinationen (bei der suche des Maximums) erster wert ist i, zweite j
     */
    private static final int[][] SHIFTS = new int[][]{{1, 1}, {0, 1}, {1, 0}};

    /**
     * Zeichen einer Gap
     */
    private static final char GAP = '-';

    /**
     * Sucht optimales lokales Alignment der zwei uebergebenen Sequenzen.
     * Liefert das gefundene Aligment zurueck.
     *
     * @param sequences    Array mit zwei Sequnzen, dessen Alignment gesucht werden soll
     * @param gapPenalty   score-Abzug fuer Gaps
     * @param substiMatrix Substitutuions-Matrix
     * @return optimales lokales Alignment
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static AlignmentResult alignLocal(final Sequence[] sequences, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        return align(true, sequences, gapPenalty, substiMatrix);
    }

    /**
     * Sucht optimales globales Alignment der zwei uebergebenen Sequenzen.
     * Liefert das gefundene Aligment zurueck.
     *
     * @param sequences    Array mit zwei Sequnzen, dessen Alignment gesucht werden soll
     * @param gapPenalty   score-Abzug fuer Gaps
     * @param substiMatrix Substitutuions-Matrix
     * @return optimales lokales Alignment
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static AlignmentResult alignGlobal(final Sequence[] sequences, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        return align(false, sequences, gapPenalty, substiMatrix);
    }

    /**
     * Sucht optimales Alignment und gibt es zurueck.
     *
     * @param local        true falls lokales Alignment gesucht werden soll
     * @param sequences    Array mit zwei Sequnzen, dessen Alignment gesucht werden soll
     * @param gapPenalty   score-Abzug fuer Gaps
     * @param substiMatrix Substitutuions-Matrix
     * @return optimales Alignment
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static AlignmentResult align(final boolean local, final Sequence[] sequences, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        if (sequences == null)
            throw new IllegalArgumentException("sequences is null");
        if (gapPenalty < 0)
            throw new IllegalArgumentException("gapPenalty is negative");
        if (substiMatrix == null)
            throw new IllegalArgumentException("substitution-matrix is null");

        final int sequenceCount = sequences.length;
        if (sequenceCount != 2)
            throw new IllegalArgumentException(sequenceCount + " Sequences passed. Pass 2 Sequences for pair-alignment");

        final String nucleotideSeqOne = sequences[0].getNucleotideSequence();
        final String nucleotideSeqTwo = sequences[1].getNucleotideSequence();

        final int lengthOne = nucleotideSeqOne.length();
        final int lengthTwo = nucleotideSeqTwo.length();

        // Init Matrix --------------------------------------------------------
        int[][] score = new int[lengthOne + 1][lengthTwo + 1];
        int[][] scoreArg = new int[lengthOne + 1][lengthTwo + 1];

        for (int i = 1; i < lengthOne + 1; i++) {
            if (!local) {
                score[i][0] = i * -1 * gapPenalty;
            }
            scoreArg[i][0] = local ? 3 : 2; // SHIFTS insert gap in second seq
        }
        for (int j = 1; j < lengthTwo + 1; j++) {
            if (!local) {
                score[0][j] = j * -1 * gapPenalty;
            }
            scoreArg[0][j] = local ? 3 : 1; // SHIFTS insert gap in first seq
        }


        int maxScoreMatrix = Integer.MIN_VALUE; // hold for start backtrace in case of local alignment
        int iMaxScoreMatrix = -1;
        int jMaxScoreMatrix = -1;

        // fill / Iterate Matrix -----------------------------------------------
        for (int i = 1; i < lengthOne + 1; i++) {
            for (int j = 1; j < lengthTwo + 1; j++) {

                // find max
                int maxScore = local ? 0 : Integer.MIN_VALUE; // local alignment fourth case
                int maxArg = local ? 3 : -1;  // local alignment fourth case

                for (int k = 0; k < SHIFTS.length; k++) {
                    int[] s = SHIFTS[k];

                    int scoreOrGapPenalty;
                    if (k == 0)
                        scoreOrGapPenalty = substiMatrix[AlignmentsMain.aminoToIndex(nucleotideSeqOne.charAt(i - 1))][AlignmentsMain.aminoToIndex(nucleotideSeqTwo.charAt(j - 1))];
                    else
                        scoreOrGapPenalty = -1 * gapPenalty;

                    int currentScore = score[i - s[0]][j - s[1]] + scoreOrGapPenalty;
                    if (currentScore > maxScore) {
                        maxScore = currentScore;
                        maxArg = k;
                    }
                }

                // update max score
                if (maxScore > maxScoreMatrix) {
                    maxScoreMatrix = maxScore;
                    iMaxScoreMatrix = i;
                    jMaxScoreMatrix = j;
                }

                // fill matrix
                score[i][j] = maxScore;
                scoreArg[i][j] = maxArg;
            }
        }

        final long totalScore = score[lengthOne][lengthTwo];
        score = null; // no reference missing -> allow garbage collector to trash

        // Backtrace ------------------------------------------------------
        final StringBuilder alignmentOne = new StringBuilder();
        final StringBuilder alignmentTwo = new StringBuilder();
        {
            int i = local ? iMaxScoreMatrix : lengthOne;
            int j = local ? jMaxScoreMatrix : lengthTwo;
            while (i >= 0 && j >= 0 && (i > 0 || j > 0)) {
                int maxArg = scoreArg[i][j];

                switch (maxArg) {
                    case 0: {
                        alignmentOne.insert(0, nucleotideSeqOne.charAt(i - 1));
                        alignmentTwo.insert(0, nucleotideSeqTwo.charAt(j - 1));
                        break;
                    }
                    case 1: {
                        alignmentOne.insert(0, GAP);
                        alignmentTwo.insert(0, nucleotideSeqTwo.charAt(j - 1));
                        break;
                    }
                    case 2: {
                        alignmentOne.insert(0, nucleotideSeqOne.charAt(i - 1));
                        alignmentTwo.insert(0, GAP);
                        break;
                    }
                    case 3: {
                        break;
                    }
                    default:
                        throw new RuntimeException("maxing score Argument not found");
                }

                if (maxArg < 3) {
                    int[] s = SHIFTS[maxArg];
                    i -= s[0];
                    j -= s[1];
                } else {
                    i--;
                    j--;
                }
            }
        }

        return new AlignmentResult(sequences, totalScore, new String[]{alignmentOne.toString(), alignmentTwo.toString()});
    }
}
