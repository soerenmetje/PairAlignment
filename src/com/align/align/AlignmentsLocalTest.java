package com.align.align;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

/**
 * Test-Klasse fuer das lokale Alignment der Klasse {@link Alignments}.
 *
 * @author Soeren Metje
 */
@RunWith(Parameterized.class)
public class AlignmentsLocalTest extends AlignmentsTestTemplate {

    /**
     * Parameter mit Resultaten  der Testfaelle
     *
     * @return Liste mit Parametern und Resultaten der Testfaelle
     */
    //seqOne, seqTwo, resultOne, resultTwo, gapPenalty
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

    /**
     * Test fuer erste Sequenz
     */
    @Test
    public void testAlignLocalOne() {
        testAlign(seqOne, seqTwo, gapPenalty, true, resultOne, 0);
    }

    /**
     * Test fuer zweite Sequenz
     */
    @Test
    public void testAlignLocalTwo() {
        testAlign(seqOne, seqTwo, gapPenalty, true, resultTwo, 1);
    }
}
