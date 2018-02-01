package com.align.logger;

import java.text.SimpleDateFormat;

/**
 * simpler Logger
 *
 * @author Soeren Metje
 */
public class Log {

    /**
     * Zeit-Format zur formatierten Zeitausgabe
     */
    private static SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * gibt an, ob Debug-Ausgaben ausgegeben werden
     */
    private static boolean printDebug = true;

    /**
     * gibt an, ob Fehler-Ausgaben ausgegeben werden
     */
    private static boolean printError = true;

    /**
     * gibt an, ob Info-Ausgaben ausgegeben werden
     */
    private static boolean printInfo = true;

    /**
     * gibt an, ob jeder Zeile die Zeit voran gestellt wird
     */
    private static boolean addTime = false;

    /**
     * setzt Fehler-Ausgabe
     *
     * @param printDebug true = fehler werden ausgegeben. false = fehler werden nicht ausgegeben
     */
    public static void setPrintDebug(boolean printDebug) {
        Log.printDebug = printDebug;
    }

    /**
     * Liefert true zurueck, falls Fehler-Ausgabe aktiv ist. Ansonsten false
     *
     * @return true, falls Fehler-Ausgabe aktiv ist. Ansonsten false
     */
    public static boolean isPrintDebug() {
        return printDebug;
    }

    /**
     * setzt Info-Ausgabe
     *
     * @param printInfo true = Infos werden ausgegeben. false = Infos werden nicht ausgegeben
     */
    public static void setPrintInfo(boolean printInfo) {
        Log.printInfo = printInfo;
    }

    /**
     * Liefert true zurueck, falls Info-Ausgabe aktiv ist. Ansonsten false
     *
     * @return true, falls Info-Ausgabe aktiv ist. Ansonsten false
     */
    public static boolean isPrintInfo() {
        return printInfo;
    }

    public synchronized static void e(String text) {
        if (printError)
            System.err.print(text);
    }

    public synchronized static void eLine(String text) {
        if (printError)
            System.err.println(time() + text);
    }

    public synchronized static void iLine() {
        if (printInfo)
            System.out.println();
    }

    public synchronized static void iLine(String text) {
        if (printInfo)
            System.out.println(time() + text);
    }

    public static void iLine(double d) {
        if (printInfo)
            System.out.println(time() + String.valueOf(d));
    }

    public synchronized static void i(String text) {
        if (printInfo)
            System.out.print(text);
    }

    public synchronized static void i(int text) {
        i(String.valueOf(text));
    }

    public static void i(double v) {
        i(String.valueOf(v));
    }

    public synchronized static void dLine() {
        if (printDebug)
            System.out.println();
    }

    public synchronized static void dLine(char i) {
        if (printDebug)
            System.out.println(time() + i);
    }

    public synchronized static void dLine(int i) {
        if (printDebug)
            System.out.println(time() + i);
    }

    public synchronized static void dLine(String text) {
        if (printDebug)
            System.out.println(time() + text);
    }

    public synchronized static void d(String text) {
        if (printDebug)
            System.out.print(text);
    }

    public synchronized static void d(int text) {
        d(String.valueOf(text));
    }

    public synchronized static void d(double v) {
        d(String.valueOf(v));
    }

    /**
     * Liefert, falls Zeit-Ausgabe aktiv ist, einen String mit Zeit-infos zurueck
     *
     * @return falls Zeit-Ausgabe aktiv ist, String mit Zeit-infos
     */
    private static String time() {
        if (!addTime)
            return "";
        return time_formatter.format(System.currentTimeMillis()) + "  ";

    }
}
