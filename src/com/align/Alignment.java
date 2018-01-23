package com.align;

import com.align.fastaparser.Sequence;

import java.util.ArrayList;
import java.util.List;

public class Alignment {

    public static List<Sequence> alginSequences(final Sequence one, final Sequence two) {
        final List<Sequence> alignedSeq = new ArrayList<>(2);

        String nucleotideSeqOne = one.getNucleotideSequence();
        String nucleotideSeqTwo = two.getNucleotideSequence();

        int lengthOne = nucleotideSeqOne.length();
        int lengthTwo = nucleotideSeqTwo.length();

        int[][] score = new int[lengthOne][lengthTwo];

        //TODO implement align

        return alignedSeq;
    }
}
