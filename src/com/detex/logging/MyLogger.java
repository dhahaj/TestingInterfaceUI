/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.detex.logging;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.*;

/**
 * <b>MyLogger</b> Handles logging of data and maintaining an archive of logs.
 *
 * @author dhahaj
 */
public class MyLogger {

    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;
    static public String FilePath;
    static public boolean Append = true;
    static public String User = null;
    static public String ARCHIVE_FOLDER_NAME = "Log_Archive";
    public static boolean SAVE_TEXT_LOG = true;
    public static boolean SAVE_HTML_LOG = true;

    /**
     * <b>setup</b> Initializes the logger/FileHandlers/formatter, and creates
     * both the text and html log files.
     *
     * @throws IOException
     */
    static public void setup() throws IOException {

        checkDate(FilePath);

        // Create Logger
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);

        // Create the FileHandlers
        fileTxt = new FileHandler(FilePath + ".txt", Append);
        fileHTML = new FileHandler(FilePath + ".html", Append);

        // Create txt Formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // Create HTML Formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);

    }

    /**
     * Checks the modified date of latest log to the current date. If they are
     * different it renames the log file and relocates it to an archive folder.
     *
     * @param FilePath The path to the current log file.
     */
    @SuppressWarnings("deprecation")
    private static void checkDate(String FilePath) {

        // Check the dates to see if the logs should be archived
        Date lastModified = new Date(new File(FilePath + ".txt").lastModified());
        Calendar rightNow = Calendar.getInstance();
        Date now = rightNow.getTime();
        System.out.println("modified: " + lastModified.getDate() + " now: " + now.getDate());

        if (now.getDate() == lastModified.getDate()) {
            return;
        }

        // Paths for the text and html files to be archived to
        final String textFilePath = (FilePath + ".txt");
        final String htmlFilePath = (FilePath + ".html");

        // Handle the text formatted log.
        File textFile = new File(textFilePath);
        final String absPath = textFile.getAbsolutePath();
        final String newBasePath = absPath.substring(0, absPath.lastIndexOf(File.separator)) + File.separator
                + ARCHIVE_FOLDER_NAME;
        final String newname = "Log_" + (lastModified.getMonth() + 1) + "." + lastModified.getDate() + "."
                + (lastModified.getYear() + 1900);

        // Create the new text file
        String newFileName = newBasePath + File.separator + newname + ".txt";
        int n = 1;

        while (fileExists(newFileName)) {
            newFileName = newBasePath + File.separator + newname + "_" + n + ".txt";
            n++;
        }

        boolean ok, archived;
        if (SAVE_TEXT_LOG) {
            File newTextFile = new File(newFileName);
            ok = textFile.renameTo(newTextFile);

            archived = archiveFile(newTextFile);
            if (!archived || !ok) {
                System.err.println("Error archiving file: " + newTextFile);
            }
        }

        // try {
        // FileWriter out = new FileWriter(newTextFile, true);
        // } catch (IOException e) {
        // e.getMessage();
        // try {
        // FileWriter out = new FileWriter(newTextFile, false);
        // } catch (IOException e1) {
        // e1.getMessage();
        // }
        // }
        if (SAVE_HTML_LOG) {
            // Create the new html file
            File htmlFile = new File(htmlFilePath);
            File newHtmlFile = new File(newFileName.replaceAll(".txt", ".html"));
            ok = htmlFile.renameTo(newHtmlFile);

            archived = archiveFile(newHtmlFile);
            if (!archived || !ok) {
                System.err.println("Error archiving file: " + newHtmlFile);
            }
        }

        // try {
        // FileWriter out1 = new FileWriter(newHtmlFile, true);
        // } catch (IOException e) {
        // e.getMessage();
        // try {
        // FileWriter out1 = new FileWriter(newHtmlFile, false);
        // } catch (IOException e1) {
        // e1.getMessage();
        // }
        // }
    }

    private static boolean archiveFile(File file) {
        FileWriter out = null;
        boolean b = false;
        try {
            out = new FileWriter(file, true);
            b = true;
        } catch (IOException e) {
            try {
                out = new FileWriter(file, false);
                b = true;
            } catch (IOException e1) {
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return b;
    }

    /**
     * <b>fileExists</b> Checks for the existence of a file.
     *
     * @param filename The filename in String format.
     * @return boolean True if the file exists, False otherwise.
     */
    public static boolean fileExists(String filename) {
//        boolean found = false;
        FileInputStream stream = null;
        try {
            File file = new File(filename);
            stream = new FileInputStream(file);
            // The inputstream will throw an exception if the file does not
            // exist. Hence, if we get here, the file exists.
            return true;
        } catch (FileNotFoundException e) {
//            found = false;
            return false;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                ex.getMessage();
            }
        }
    }

    /**
     * <b>fileExists</b> Checks for the existence of a file.
     *
     * @param f The file to check.
     * @return True if file is found, False otherwise.
     */
    public static boolean fileExists(File f) {
        return fileExists(f.getAbsolutePath());
    }

}
