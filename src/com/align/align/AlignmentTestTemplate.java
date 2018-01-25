package com.align.align;

import com.align.fastaparser.Sequence;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.runners.Parameterized.Parameter;

/**
 * Test-Klassen-Vorlage fuer {@link AlignmentGlobalTest} und {@link AlignmentLocalTest} zum testen der Klasse {@link Alignment}.
 *
 * @author Soeren Metje
 */
@RunWith(Parameterized.class)
public class AlignmentTestTemplate {

    /**
     * Substitutions-Matrix
     */
    private static final int[][] SUBSTI_MATRIX = {
            {11, -3, -2, -3, -3, -2, -4, 2, -2, 1, -3, -4, -2, -3, -4, -1, -2, -3, -3, -2},
            {-3, 4, 0, -2, -3, -2, -2, -1, -3, -1, -2, -3, -1, 0, -3, 1, 1, -2, 3, -3},
            {-2, 0, 5, 1, -1, -1, -1, -2, -2, -2, -1, -1, -1, 0, 0, -1, -1, -1, -1, -2},
            {-3, -2, 1, 4, -1, 0, -1, -2, 0, -2, 0, 0, -1, 1, 1, -1, -2, 0, -2, -1},
            {-3, -3, -1, -1, 5, 1, -2, -2, -2, -3, 0, -2, -3, -1, 0, -1, -2, 2, -3, 0},
            {-2, -2, -1, 0, 1, 5, -1, -1, -2, -3, 2, 0, -3, -1, 0, 0, -2, 1, -3, 0},
            {-4, -2, -1, -1, -2, -1, 7, -3, -2, -4, -1, -1, -3, -1, -2, -2, -3, -1, -3, -2},
            {2, -1, -2, -2, -2, -1, -3, 7, -3, 3, -2, -3, -2, -2, -2, -1, -1, -2, -1, 2},
            {-2, -3, -2, 0, -2, -2, -2, -3, 6, -3, -2, -1, -3, 0, 0, -3, -4, -2, -4, -2},
            {1, -1, -2, -2, -3, -3, -4, 3, -3, 6, -3, -3, -2, -2, -3, 0, 0, -3, 0, -1},
            {-3, -2, -1, 0, 0, 2, -1, -2, -2, -3, 5, 2, -4, -1, 0, -2, -3, 1, -3, 0},
            {-4, -3, -1, 0, -2, 0, -1, -3, -1, -3, 2, 6, -3, -2, 1, -3, -4, -1, -3, -1},
            {-2, -1, -1, -1, -3, -3, -3, -2, -3, -2, -4, -3, 9, 0, -3, -1, -1, -3, -1, -3},
            {-3, 0, 0, 1, -1, -1, -1, -2, 0, -2, -1, -2, 0, 4, -2, -1, -1, -1, -1, -2},
            {-4, -3, 0, 1, 0, 0, -2, -2, 0, -3, 0, 1, -3, -2, 6, -2, -3, 0, -3, 1},
            {-1, 1, -1, -1, -1, 0, -2, -1, -3, 0, -2, -3, -1, -1, -2, 5, 2, -1, 1, -2},
            {-2, 1, -1, -2, -2, -2, -3, -1, -4, 0, -3, -4, -1, -1, -3, 2, 4, -2, 2, -3},
            {-3, -2, -1, 0, 2, 1, -1, -2, -2, -3, 1, -1, -3, -1, 0, -1, -2, 5, -3, -1},
            {-3, 3, -1, -2, -3, -3, -3, -1, -4, 0, -3, -3, -1, -1, -3, 1, 2, -3, 4, -3},
            {-2, -3, -2, -1, 0, 0, -2, 2, -2, -1, 0, -1, -3, -2, 1, -2, -3, -1, -3, 8}};


    /**
     * Parameter erste Sequenz
     */
    @Parameter(0)
    public String seqOne;
    /**
     * Parameter zweite Sequenz
     */
    @Parameter(1)
    public String seqTwo;
    /**
     * gegebenes Alignment der ersten Sequenz
     */
    @Parameter(2)
    public String resultOne;
    /**
     * gegebenes Alignment der zweiten Sequenz
     */
    @Parameter(3)
    public String resultTwo;
    /**
     * Gap-Penalty
     */
    @Parameter(4)
    public int gapPenalty;

    /**
     * testet das Alignment an uebergebener Position
     *
     * @param seqOne     erste Sequenz
     * @param seqTwo     zweite Sequenz
     * @param gapPenalty Gap-Penalty
     * @param local      lokales oder globales Alignment
     * @param result     erwartetes Alignment
     * @param pos        erstes oder zweites Alignment ueberpruefen
     */
    static void testAlign(String seqOne, String seqTwo, int gapPenalty, boolean local, String result, int pos) {
        Sequence[] sequences = new Sequence[2];
        sequences[0] = new Sequence("one", null, seqOne);
        sequences[1] = new Sequence("two", null, seqTwo);


        AlignmentResult alignmentResult;
        alignmentResult = Alignment.align(local, sequences, gapPenalty, SUBSTI_MATRIX);

        String[] alignments = alignmentResult.getAlignments();

        Assert.assertEquals(2, alignments.length);
        Assert.assertEquals(result, alignments[pos]);
    }
}
