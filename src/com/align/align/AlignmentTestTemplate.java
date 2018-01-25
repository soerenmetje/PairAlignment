package com.align.align;

import com.align.AlignmentMain;
import com.align.fastaparser.Sequence;
import org.junit.Assert;

import static org.junit.runners.Parameterized.Parameter;

/**
 * Test-Klassen-Vorlage fuer {@link AlignmentGlobalTest} und {@link AlignmentLocalTest} zum testen der Klasse {@link Alignment}.
 *
 * @author Soeren Metje
 */
public abstract class AlignmentTestTemplate {

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
        alignmentResult = Alignment.align(local, sequences, gapPenalty, AlignmentMain.SUBSTI_MATRIX_DEFAULT);

        String[] alignments = alignmentResult.getAlignments();

        Assert.assertEquals(2, alignments.length);
        Assert.assertEquals(result, alignments[pos]);
    }
}
