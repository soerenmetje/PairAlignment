package com.align.align;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

/**
 * Test-Klasse fuer {@link Alignment}.
 *
 * @author Soeren Metje
 */
@RunWith(Parameterized.class)
public class AlignmentLocalTest {

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


    @Parameter(0)
    public String seqOne;
    @Parameter(1)
    public String seqTwo;
    @Parameter(2)
    public String resultOne;
    @Parameter(3)
    public String resultTwo;
    @Parameter(4)
    public int gapPenalty;

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", "GSAQVKGHGKKV--A--DALTNAVAHVDDMPNAL--S-A-LSDLHAHKL", "GNPKVKAHGKKVLGAFSDGL----AHL-D--N-LKGTFATLSELHCDKL", 1},
                {"GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", "GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL     ", 5},
                {"VKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GSAQVKGHGKKVAKALTNAVAHVDDMPNALSA", "VKGHGKKVADALTNAVAHVDDMPNALSA", "VKGHGKKVAKALTNAVAHVDDMPNALSA", 1}, // remove at start and end and 1 substitution
                {"FSCQCAPGYTGARCETNIDDCLGEIKCQNNA", "FSCQCAPGYTGARCETNIDDCLGEIKCQNNA", "FSCQCAPGYTGARCETNIDDCLGEIKCQNNA", "FSCQCAPGYTGARCETNIDDCLGEIKCQNNA", 1}, // same seq
                {"FSCQCAPGYTGARCETNIDDCLGEIKCQNNATCIDGVESYKCECQPGFSGEFCDTKIQFC", "YKCECPRGFYDAHCLSDVDECASNPCVNEGRCEDGINEFICHCPPGYTGKRCELDIDEC", "FSCQCAP-G-YTGARCE-TNIDDCLGEIKCQNNATCI-DGV-ESYK-CECQPGFSGEF-C--DTKI-QFC", "YKCEC-PRGFY-DAHC-LSDVDECASN-PCVNEGRC-EDGINE-F-ICHCPPGYTGK-RCELD--IDE-C", 1},
                {"FSCQCAPGYTGARCETNIDDCLGEIKCQNNATCIDGVESYKCECQPGFSGEFCDTKIQFC", "YKCECPRGFYDAHCLSDVDECASNPCVNEGRCEDGINEFICHCPPGYTGKRCELDIDEC", "FSCQCAPGYTGARCETNIDDCLGEIKCQNNATCIDGVESYKCECQPGFSGEFCDTKIQFC", "YKCECPRGFYDAHCLSDVDECASN-PCVNEGRCEDGINEFICHCPPGYTGKRCELDIDEC", 5}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testAlignLocalOne() {
        AlignmentGlobalTest.testAlign(seqOne, seqTwo, gapPenalty, true, resultOne, 0);
    }

    @Test
    public void testAlignLocalTwo() {
        AlignmentGlobalTest.testAlign(seqOne, seqTwo, gapPenalty, true, resultTwo, 1);
    }
}
