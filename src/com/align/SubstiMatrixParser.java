package com.align;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parser, der die Datei BLOSUM62 parset.
 *
 * @author Soeren Metje
 */
public class SubstiMatrixParser {


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
