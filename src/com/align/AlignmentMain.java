package com.align;

import com.align.argparser.ArgumentParser;
import com.align.argparser.ArgumentParserException;
import com.align.argparser.ParameterSet;
import com.align.argparser.Setting;
import com.align.fastaparser.FastaParser;
import com.align.fastaparser.FastaParserException;
import com.align.fastaparser.Sequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AlignmentMain {

    public static final String OUTPUT_FILE_NAME = "aligned.fasta";

    public static final char[] AMIN = new char[]{'W', 'V', 'T', 'S', 'R', 'Q', 'P', 'Y', 'G', 'F', 'E', 'D', 'C', 'A', 'N', 'M', 'L', 'K', 'I', 'H'};
    public static final int AMIN_COUNT = AMIN.length;

    private static int[][] substiMatrix;

    public static void main(String[] args) {
        // set up Parameter
        ParameterSet parameterSet = new ParameterSet();
        Setting paramFilePath = new Setting("file", true);
        Setting paramFilePathSub = new Setting("filesub", true);
        Setting paramGap = new Setting("gap", true);
        parameterSet.addSetting(paramFilePath);
        parameterSet.addSetting(paramFilePathSub);
        parameterSet.addSetting(paramGap);

        try {
            ArgumentParser parser = new ArgumentParser(parameterSet);
            parser.parseArgs(args);
        } catch (ArgumentParserException e) { // if parameter is missing or not intended
            System.err.println(e.getMessage());
            System.exit(1);
        }

        int gapPenalty = 0;
        try {
            gapPenalty = Integer.parseInt(paramGap.getValue());
        } catch (NumberFormatException e) {
            System.err.println("ERROR: GapPenalty is no Number");
            System.exit(1);
        }
        if (gapPenalty <= 0) {
            System.err.println("ERROR: GapPenalty should be a positive number");
            System.exit(1);
        }

        try {
            substiMatrix = SubstiMatrixParser.parseFile(paramFilePathSub.getValue());
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("ERROR: while parsing substitution matrix " + e.getMessage());
            System.exit(1);
        }

        // output substitution Matrix
        {
            StringBuilder out = new StringBuilder("Substitution Matrix:\n");
            for (int i = 0; i < substiMatrix.length; i++) {
                out.append(AMIN[i]).append(":  ");
                for (int j = 0; j < substiMatrix[i].length; j++) {
                    out.append(String.format("%3d", substiMatrix[i][j]));
                }
                out.append('\n');
            }
            System.out.println(out.toString());
        }

        String filePath = paramFilePath.getValue();
        Sequence sequenceOne = null, sequenceTwo = null;
        {
            List<Sequence> sequences = readFile(filePath);
            int seqCount = sequences.size();
            if (seqCount != 2) {
                System.err.println("ERROR: file " + filePath + " contains " + seqCount + " instead of 2 Sequences");
                System.exit(1);
            }
            sequenceOne = sequences.get(0);
            sequenceTwo = sequences.get(1);
        }

        List<Sequence> alignedSeq = Alignment.alginSequences(sequenceOne, sequenceTwo, gapPenalty);
        {
            File file = new File(filePath);
            saveToFile(file.getParent(), OUTPUT_FILE_NAME, alignedSeq);
        }

    }

    private static void saveToFile(String filePath, String fileName, List<Sequence> sequences) {
        //TODO implement
    }

    private static List<Sequence> readFile(String filePath) {
        List<Sequence> ret = null;

        System.out.println("reading " + filePath);
        try {
            ret = FastaParser.parseFile(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: file " + filePath + " not found");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("ERROR: while reading file " + filePath);
            System.exit(1);
        } catch (FastaParserException e) {
            System.err.println("ERROR: while parsing file " + filePath + ": " + e.getMessage());
            System.exit(1);
        }

        System.out.println("successfully finished reading file");
        return ret;
    }

    public static int aminoToIndex(char amino) {
        return charToIndex(AMIN, amino);
    }

    /**
     * Mappt Beaobachtung-Folge auf entsprechende Index-Folge
     *
     * @param space        Feld aller Beobachtungen
     * @param observations Beobachtungs-Folge
     * @return entsprechende Index-Folge
     * @throws IllegalArgumentException falls Beobachtung nicht im Feld gefunden wird
     */
    public static int[] charsToIndices(final char[] space, final char[] observations) throws IllegalArgumentException {
        int length = observations.length;
        int[] ret = new int[length];

        for (int i = 0; i < length; i++) {
            ret[i] = charToIndex(space, observations[i]);
        }
        return ret;
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
