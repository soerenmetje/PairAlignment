package com.align.substiparser;

import com.align.AlignmentMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parser, der die Datei BLOSUM62 parset. Erstellt anhand der Datei eine Substitutuions-Matrix.
 *
 * @author Soeren Metje
 */
public class SubstiMatrixParser {

    /**
     * Liesst Datei an uebergebenen Pfad ein. Fuellt anhand der Daten die Substitutuions-Matrix und liefert sie zurueck.
     *
     * @param filePath Datei-Pfad
     * @return Substitutuions-Matrix
     * @throws IllegalArgumentException Falls filePath == null
     *                                  oder fallsangegebene Aminosaeure nicht im Feld gefunden wird
     * @throws IOException              Falls beim Einlesen ein Fahler auftritt
     */
    public static int[][] parseFile(String filePath) throws IllegalArgumentException, IOException {
        if (filePath == null)
            throw new IllegalArgumentException("filePath is null");

        File file = new File(filePath);

        int[][] substiMatrix = new int[AlignmentMain.AMIN_COUNT][AlignmentMain.AMIN_COUNT];

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) { // closes reader

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();

                char aminoFrom = line.charAt(0);
                line = line.substring(1).trim();

                char aminoTo = line.charAt(0);
                line = line.substring(1).trim();

                int score = Integer.parseInt(line);

                substiMatrix[AlignmentMain.aminoToIndex(aminoFrom)][AlignmentMain.aminoToIndex(aminoTo)] = score;
            }
        }
        return substiMatrix;
    }
}
