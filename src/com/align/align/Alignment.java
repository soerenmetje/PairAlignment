package com.align.align;

import com.align.AlignmentMain;
import com.align.fastaparser.Sequence;

import java.util.ArrayList;
import java.util.List;

public class Alignment {

    private static final int[][] SHIFTS = new int[][]{{1, 1}, {0, 1}, {1, 0}};

    private static final char GAP = '-';

    public static List<AlignmentResult> align(final boolean local, final Sequence one, final Sequence two, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        if (local)
            return alignLocal(one, two, gapPenalty, substiMatrix);
        else
            return alignGlobal(one, two, gapPenalty, substiMatrix);
    }

    public static List<AlignmentResult> alignGlobal(final Sequence one, final Sequence two, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        if (one == null)
            throw new IllegalArgumentException("first Sequence is null");
        if (two == null)
            throw new IllegalArgumentException("second Sequence is null");
        if (gapPenalty < 0)
            throw new IllegalArgumentException("gapPenalty is negative");
        if (substiMatrix == null)
            throw new IllegalArgumentException("substitution-matrix is null");

        final String nucleotideSeqOne = one.getNucleotideSequence();
        final String nucleotideSeqTwo = two.getNucleotideSequence();

        final int lengthOne = nucleotideSeqOne.length();
        final int lengthTwo = nucleotideSeqTwo.length();

        // Init Matrix
        final int[][] score = new int[lengthOne + 1][lengthTwo + 1];
        final int[][] scoreArg = new int[lengthOne + 1][lengthTwo + 1];

        for (int i = 1; i < lengthOne + 1; i++) {
            score[i][0] = i * -1 * gapPenalty;
            scoreArg[i][0] = 2; // SHIFTS insert gap in second seq
        }
        for (int j = 1; j < lengthTwo + 1; j++) {
            score[0][j] = j * -1 * gapPenalty;
            scoreArg[0][j] = 1; // SHIFTS insert gap in first seq
        }

        // fill / Iterate Matrix
        for (int i = 1; i < lengthOne + 1; i++) {
            for (int j = 1; j < lengthTwo + 1; j++) {

                int maxScore = Integer.MIN_VALUE;
                int maxArg = -1;

                for (int k = 0; k < SHIFTS.length; k++) {

                    int[] s = SHIFTS[k];

                    int scoreOrGap;
                    if (k == 0)
                        scoreOrGap = substiMatrix[AlignmentMain.aminoToIndex(nucleotideSeqOne.charAt(i - 1))][AlignmentMain.aminoToIndex(nucleotideSeqTwo.charAt(j - 1))];
                    else
                        scoreOrGap = -1 * gapPenalty;
                    int currentScore = score[i - s[0]][j - s[1]] + scoreOrGap;

                    if (currentScore > maxScore) {
                        maxScore = currentScore;
                        maxArg = k;
                    }
                }
                score[i][j] = maxScore;
                scoreArg[i][j] = maxArg;
            }
        }

        // Backtrace
        final StringBuilder alignedSeqOne = new StringBuilder();
        final StringBuilder alignedSeqTwo = new StringBuilder();
        {
            int i = lengthOne, j = lengthTwo;
            while (i >= 0 && j >= 0 && (i > 0 || j > 0)) {
                int maxArg = scoreArg[i][j];

                switch (maxArg) {
                    case 0: {
                        alignedSeqOne.insert(0, nucleotideSeqOne.charAt(i - 1));
                        alignedSeqTwo.insert(0, nucleotideSeqTwo.charAt(j - 1));
                        break;
                    }
                    case 1: {
                        alignedSeqOne.insert(0, GAP);
                        alignedSeqTwo.insert(0, nucleotideSeqTwo.charAt(j - 1));
                        break;
                    }
                    case 2: {
                        alignedSeqOne.insert(0, nucleotideSeqOne.charAt(i - 1));
                        alignedSeqTwo.insert(0, GAP);
                        break;
                    }
                }

                int[] s = SHIFTS[maxArg];
                i -= s[0];
                j -= s[1];
            }
        }

        // finalize
        final long totalScore = score[lengthOne][lengthTwo];

        final List<AlignmentResult> alignResults = new ArrayList<>(2);
        alignResults.add(new AlignmentResult(one, totalScore, alignedSeqOne.toString()));
        alignResults.add(new AlignmentResult(two, totalScore, alignedSeqTwo.toString()));

        return alignResults;
    }

    public static List<AlignmentResult> alignLocal(final Sequence one, final Sequence two, final int gapPenalty, final int[][] substiMatrix) throws IllegalArgumentException {
        if (one == null)
            throw new IllegalArgumentException("first Sequence is null");
        if (two == null)
            throw new IllegalArgumentException("second Sequence is null");
        if (gapPenalty < 0)
            throw new IllegalArgumentException("gapPenalty is negative");
        if (substiMatrix == null)
            throw new IllegalArgumentException("substitution-matrix is null");

        // TODO find local alignment

        final StringBuilder alignedSeqOne = new StringBuilder();
        final StringBuilder alignedSeqTwo = new StringBuilder();

        // TODO create aligned seq

        final long totalScore = 0;

        final List<AlignmentResult> alignResults = new ArrayList<>(2);
        alignResults.add(new AlignmentResult(one, totalScore, alignedSeqOne.toString()));
        alignResults.add(new AlignmentResult(two, totalScore, alignedSeqTwo.toString()));

        return alignResults;
    }
}
