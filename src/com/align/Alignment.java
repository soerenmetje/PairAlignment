package com.align;

import com.align.fastaparser.Sequence;

import java.util.ArrayList;
import java.util.List;

public class Alignment {

    private static final int[][] SHIFTS = new int[][]{{1, 1}, {0, 1}, {1, 0}};

    private static final char GAP = '-';

    public static List<Sequence> alignGlobal(final Sequence one, final Sequence two, int gapPanelty, int[][] substiMatrix) {
        final List<Sequence> alignedSeq = new ArrayList<>(2);

        String nucleotideSeqOne = one.getNucleotideSequence();
        String nucleotideSeqTwo = two.getNucleotideSequence();

        int lengthOne = nucleotideSeqOne.length();
        int lengthTwo = nucleotideSeqTwo.length();

        int[][] score = new int[lengthOne + 1][lengthTwo + 1];
        int[][] scoreArg = new int[lengthOne + 1][lengthTwo + 1];


        for (int i = 1; i < lengthOne + 1; i++) {
            score[i][0] = i * -1 * gapPanelty;
        }
        for (int j = 1; j < lengthTwo + 1; j++) {
            score[0][j] = j * -1 * gapPanelty;
        }

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
                        scoreOrGap = -1 * gapPanelty;
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
        StringBuilder alignedSeqOne = new StringBuilder();
        StringBuilder alignedSeqTwo = new StringBuilder();
        {
            int i = lengthOne, j = lengthTwo;
            while (i > 0 && j > 0) {
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

        String totalScore = String.valueOf(score[lengthOne][lengthTwo]);

        alignedSeq.add(new Sequence(one.getDescription(), totalScore, alignedSeqOne.toString()));
        alignedSeq.add(new Sequence(two.getDescription(), totalScore, alignedSeqTwo.toString()));
        //TODO implement align

        return alignedSeq;
    }
}
