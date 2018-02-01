package com.align;

import com.align.align.AlignmentResult;
import com.align.align.Alignments;
import com.align.argparser.*;
import com.align.fastaparser.FastaParser;
import com.align.fastaparser.FastaParserException;
import com.align.fastaparser.Sequence;
import com.align.logger.Log;
import com.align.substiparser.SubstiMatrixParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Ausfuehrbare Klasse. Berechnet mittels {@link Alignments} ein Alignment.
 * Die auf der Komandozeile uebergebenen Argumente werden mittels {@link ArgumentParser} geparset.
 * Die Sequnezen werden aus der angegebenen Datei mittels {@link FastaParser} eingelesen.
 * Die Substitutions-Matrix BLOSUM62 wird mittels {@link SubstiMatrixParser} eingelesen.
 *
 * @author Soeren Metje
 */
public class AlignmentMain {

    /**
     * Standart-Substitutions-Matrix
     */
    public static final int[][] SUBSTI_MATRIX_DEFAULT = {
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
     * Kuerzel der Aminosaeueren
     */
    private static final char[] AMIN = new char[]{'W', 'V', 'T', 'S', 'R', 'Q', 'P', 'Y', 'G', 'F', 'E', 'D', 'C', 'A', 'N', 'M', 'L', 'K', 'I', 'H'};

    /**
     * Anzahl der Aminosaeuren
     */
    public static final int AMIN_COUNT = AMIN.length;

    /**
     * Ausfuehrbare Methode
     *
     * @param args Argumente
     */
    public static void main(String[] args) {
        // set up Parameter
        final ParameterSet parameterSet = new ParameterSet();
        final Setting paramFilePath = new Setting("file", true);
        final Setting paramFilePathSub = new Setting("filesub", false);
        final Setting paramGap = new Setting("gap", true);
        final Flag paramTypeLocal = new Flag("local", false);
        Flag paramInfo = new Flag("info", false);
        parameterSet.addSetting(paramFilePath);
        parameterSet.addSetting(paramFilePathSub);
        parameterSet.addSetting(paramGap);
        parameterSet.addFlag(paramTypeLocal);
        parameterSet.addFlag(paramInfo);

        try {
            ArgumentParser parser = new ArgumentParser(parameterSet);
            parser.parseArgs(args);
        } catch (ArgumentParserException e) { // if parameter is missing or not intended
            Log.eLine(e.getMessage());
            System.exit(1);
        }

        // setup logger
        Log.setPrintInfo(paramInfo.isSet());

        int gapPenalty = 0;
        try {
            gapPenalty = Integer.parseInt(paramGap.getValue());
        } catch (NumberFormatException e) {
            Log.eLine("ERROR: GapPenalty is no Number");
            System.exit(1);
        }
        if (gapPenalty <= 0) {
            Log.eLine("ERROR: GapPenalty should be a positive number");
            System.exit(1);
        }

        int[][] substiMatrix = null;
        if (!paramFilePathSub.isSet()) {
            Log.iLine("hardcoded substitution-matrix is used");
            substiMatrix = SUBSTI_MATRIX_DEFAULT;
        }
        // path passed -> reading substitution matrix
        else {
            String filePath = paramFilePathSub.getValue();
            Log.iLine("reading " + filePath);
            try {
                substiMatrix = SubstiMatrixParser.parseFile(filePath);
                Log.iLine("successfully finished reading file");
            } catch (IOException | IllegalArgumentException e) {
                Log.eLine("ERROR: while parsing substitution matrix " + e.getMessage());
                System.exit(1);
            }
        }

        // read Sequences
        final String filePath = paramFilePath.getValue();
        Sequence[] sequences;
        {
            List<Sequence> sequenceList = readFile(filePath);
            int seqCount = sequenceList.size();
            if (seqCount != 2) {
                Log.eLine("ERROR: file " + filePath + " contains " + seqCount + " instead of 2 Sequences");
                System.exit(1);
            }
            sequences = new Sequence[sequenceList.size()];
            int i = 0;
            for (Sequence sequence : sequenceList) {
                sequences[i] = sequence;
                i++;
            }
        }

        // calc optimal alignment
        AlignmentResult alignmentResult = null;
        try {
            boolean local = paramTypeLocal.isSet();
            alignmentResult = Alignments.align(local, sequences, gapPenalty, substiMatrix);
        } catch (IllegalArgumentException e) {
            Log.eLine("ERROR: alignment failed. " + e.getMessage());
            System.exit(1);
        }

        // output alignment
        {
            long score = alignmentResult.getScore();
            String[] alignments = alignmentResult.getAlignments();
            Log.iLine("\nOptimal alignment:");
            System.out.println(String.format("score = %d\n%s\n%s", score, alignments[0], alignments[1]));
        }
    }

    /**
     * Liesst Datei an uebergebenem Pfad mittels {@link FastaParser} ein und liefert eine Liste mit den eingelesenen Sequenzen zurueck.
     * Bei einem Fehler wird die VM beendet.
     *
     * @param filePath Datei-Pfad
     * @return Liste mit den eingelesenen Sequenzen
     */
    private static List<Sequence> readFile(final String filePath) {
        List<Sequence> ret = null;

        Log.iLine("reading " + filePath);
        try {
            ret = FastaParser.parseFile(filePath);
        } catch (FileNotFoundException e) {
            Log.eLine("ERROR: file " + filePath + " not found");
            System.exit(1);
        } catch (IOException e) {
            Log.eLine("ERROR: while reading file " + filePath);
            System.exit(1);
        } catch (FastaParserException e) {
            Log.eLine("ERROR: while parsing file " + filePath + ": " + e.getMessage());
            System.exit(1);
        }

        Log.iLine("successfully finished reading file");
        return ret;
    }

    /**
     * Mappt Aminosaeure-Kuerzel auf entsprechenden Index
     *
     * @param amino Aminosaeure-Kuerzel
     * @return entsprechender Index
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static int aminoToIndex(char amino) throws IllegalArgumentException {
        return charToIndex(AMIN, amino);
    }

    /**
     * Mappt Beaobachtung auf entsprechenden Index
     *
     * @param space       Feld aller Beobachtungen
     * @param observation Beobachtung
     * @return entsprechender Index
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static int charToIndex(final char[] space, final char observation) throws IllegalArgumentException {
        for (int i = 0, basesLength = space.length; i < basesLength; i++) {
            if (space[i] == observation)
                return i;
        }
        throw new IllegalArgumentException("Character " + observation + " not found");
    }
}
