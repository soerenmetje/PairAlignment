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
                {"GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", "GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", 5},
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
