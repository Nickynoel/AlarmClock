package RowFileReader;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class that gets a file and reads line by line, putting each of the lines into a list/array of String
 */

public class RowFileReader
{
    /**
     * factory method to call a fileReader from a string, which reads each line
     * calls constructor if input is valid, null if invalid
     */
    public static RowFileReader getInstance(String file) {
        return getInstance(new File(file));
    }

    /**
     * factory method to call a fileReader from a file, which reads each line
     * calls constructor if input is valid, null if invalid
     */
    public static RowFileReader getInstance(File file) {
        return isValidFilePath(file) ? new RowFileReader(file) : null;
    }

    /**
     * File is created if it doesn't exist and then gets tested if readable
     *
     * @param file Tested File
     * @return boolean if file is readable
     */
    private static boolean isValidFilePath(File file) {
        try {
            file.createNewFile(); // creates the file if not existent
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid Filepath");
            return false;
        }
        return file.canRead();
    }

    private final List<String> _list;

    private RowFileReader(File file) {
        _list = loadFile(file);
    }

    /**
     * reads the file and saves each non-empty line as string into a list
     */
    private List<String> loadFile(File file) {
        List<String> stringList = new ArrayList<>();

        try (Scanner res = new Scanner(file, StandardCharsets.UTF_8)) {
            while (res.hasNextLine()) {
                String line = res.nextLine();
                if (!(line.trim().isEmpty()))
                    stringList.add(line);
            }
        }
        catch (IOException e1) {
            javax.swing.JOptionPane.showMessageDialog(new JFrame(),
                    "Unexpected error while reading from the file!");
        }

        return stringList;
    }

    /**
     * Returns the created list as a List
     *
     * @return _list
     */
    public List<String> getList() {
        return _list;
    }
}

