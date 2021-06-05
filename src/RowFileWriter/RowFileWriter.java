package RowFileWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A class that takes a list of strings and writes each on a line in a given file
 */

public class RowFileWriter
{
    private File _file;
    private List<String> _list;
    
    private RowFileWriter(List<String> list, File file)
    {
        _file = file;
        _list = list;
    }
    
    /**
     * factorymethod to call a filewriter for a file, which saves every String in a seperate line
     * calls constructor if input is valid, null if invalid
     */
    public static RowFileWriter getInstance(List<String> list, File file)
    {
        if (!file.isFile())
        {
            try (FileWriter wr = new FileWriter(file.getName(), StandardCharsets.UTF_8))
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                javax.swing.JOptionPane.showMessageDialog(new JFrame(),"File cant be created");
                System.err.println("Error creating a new file");
            }
        }
        if (!file.canWrite())
        {
            return null;
        }
        return new RowFileWriter(list, file);
    }
    
    /**
     * reads the file and saves each line as string into a list
     */
    public void saveFile() //file = File.txt
    {
        try (FileWriter wr = new FileWriter(_file))
        {
            for (String s : _list)
            {
                wr.write(s + "\n");
            }
        }
        catch (IOException e)
        {
            System.err.println("Error writing on a file");
        }
    }
    
    /**
     * adds another row to the file
     */
    public void addRow(String s)
    {
        try (FileWriter wr = new FileWriter(_file, true))
        {
            wr.append(s + "\n");
        }
        catch (IOException e)
        {
            System.err.println("Error writing on a file");
        }
    }
}
