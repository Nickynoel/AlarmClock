package RowFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class that gets a file and reads line by line, putting each of the lines into a list/array of String
 */

public class RowFilereader
{
    private File _file;
    private List<String> _list;
    private String[] _listArray;
    
    private RowFilereader(File file)
    {
        _file = file;
        _list = loadfile(_file);
        _listArray = _list.toArray(new String[_list.size()]);
    }
    
    /**
     * factorymethod to call a filereader from a file, which reads each line
     * calls constructor if input is valid, null if invalid
     */
    public static RowFilereader getInstance(File file)
    {
        if (!file.isFile())
        {
            try (FileWriter wr = new FileWriter(file.getName(), StandardCharsets.UTF_8))
            {
                return new RowFilereader(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (file.canRead())
        {
            return new RowFilereader(file);
        }
        return null;
    }
    
    /**
     * factorymethod to call a filereader from a string, which reads each line
     * calls constructor if input is valid, null if invalid
     */
    public static RowFilereader getInstance(String file)
    {
        return getInstance(new File(file));
    }
    
    /**
     * reads the file and saves each non-empty line as string into a list
     */
    public List<String> loadfile(File file) //file = File.txt
    {
        List<String> liste = new ArrayList<>();
        
        try (Scanner res = new Scanner(file))
        {
            while (res.hasNextLine())
            {
                String line = res.nextLine();
                if(line.trim()!="")
                {
                    liste.add(line);
                }
            }
        } catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        
        return liste;
    }
    
    /**
     * Returns the created list as a List
     * @return _list
     */
    public List<String> getList()
    {
        return _list;
    }
    
    /**
     * Returns the created list as an array
     * @return _listArray
     */
    public String[] getArray()
    {
        return _listArray;
    }
}

