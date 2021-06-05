package RowFileWriter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Testclass for RowFileWriter
 */

public class RowFileWriterTest
{
    @Test
    public void testConstructor()
    {
        List<String> list = new ArrayList<>();
        list.add("test");
        File file = new File("Test123.txt");
        
        //write testfile
        RowFileWriter wr = RowFileWriter.getInstance(list, file);
        wr.saveFile();
        wr.addRow("t m p");
        
        //actual testclass
        try (Scanner res = new Scanner(file))
        {
            assertTrue(res.nextLine().equals("test"));
            assertTrue(res.nextLine().equals("t m p"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        //delete testfile
        file.delete();
    }
}
