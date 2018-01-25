package com.align.align;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.Parameters;

/**
 * Test-Klasse fuer das globale Alignment der Klasse {@link Alignment}.
 *
 * @author Soeren Metje
 */
@RunWith(Parameterized.class)
public class AlignmentGlobalTest extends AlignmentTestTemplate {

    /**
     * Parameter mit Resultaten  der Testfaelle
     *
     * @return Liste mit Parametern und Resultaten der Testfaelle
     */
    //seqOne, seqTwo, resultOne, resultTwo, gapPenalty
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", "GSAQVKGHGKKV--A--DALTNAVAHVDDMPNAL--S-A-LSDLHAHKL", "GNPKVKAHGKKVLGAFSDGL----AHLD---N-LKGTFATLSELHCDKL", 1},
                {"GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", "GSAQVKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GNPKVKAHGKKVLGAFSDGLAHLDNLKGTFATLSELHCDKL", 5},
                {"VKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GSAQVKGHGKKVAKALTNAVAHVDDMPNALSA", "----VKGHGKKVADALTNAVAHVDDMPNALSALSDLHAHKL", "GSAQVKGHGKKVAKALTNAVAHVDDMPNALSA---------", 1}, // insert gaps at start and end
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
    public void testAlignGlobalOne() {
        testAlign(seqOne, seqTwo, gapPenalty, false, resultOne, 0);
    }

    /**
     * Test fuer zweite Sequenz
     */
    @Test
    public void testAlignGlobalTwo() {
        testAlign(seqOne, seqTwo, gapPenalty, false, resultTwo, 1);
    }
}
