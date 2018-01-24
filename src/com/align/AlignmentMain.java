package com.align;

import com.align.align.Alignment;
import com.align.align.AlignmentResult;
import com.align.argparser.*;
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
        final ParameterSet parameterSet = new ParameterSet();
        final Setting paramFilePath = new Setting("file", true);
        final Setting paramFilePathSub = new Setting("filesub", true);
        final Setting paramGap = new Setting("gap", true);
        final Flag paramTypeLocal = new Flag("local", false);
        parameterSet.addSetting(paramFilePath);
        parameterSet.addSetting(paramFilePathSub);
        parameterSet.addSetting(paramGap);
        parameterSet.addFlag(paramTypeLocal);

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

        final String filePath = paramFilePath.getValue();
        Sequence[] sequences = null;
        {
            List<Sequence> sequenceList = readFile(filePath);
            int seqCount = sequenceList.size();
            if (seqCount != 2) {
                System.err.println("ERROR: file " + filePath + " contains " + seqCount + " instead of 2 Sequences");
                System.exit(1);
            }
            sequences = (Sequence[]) sequenceList.toArray();
        }

        AlignmentResult alignmentResult = null;
        try {
            boolean local = paramTypeLocal.isSet();
            alignmentResult = Alignment.align(local, sequences, gapPenalty, substiMatrix);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: alignment failed. " + e.getMessage());
            System.exit(1);
        }


        {
            long score = alignmentResult.getScore();
            String[] alignments = alignmentResult.getAlignments();
            System.out.println(String.format("Optimal alignment: \nscore = %d\n%s\n%s", score, alignments[0], alignments[1]));
        }

        {
            String fileDir = new File(filePath).getParent();
            saveToFile(fileDir, OUTPUT_FILE_NAME, alignmentResult);
        }

    }

    private static void saveToFile(String fileDir, String fileName, AlignmentResult alignmentResult) {
        //TODO implement
    }

    private static List<Sequence> readFile(final String filePath) {
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
