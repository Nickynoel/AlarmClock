package RowFileReader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The Testclass for RowFilereader
 */

public class RowFilereaderTest
{
    @Test
    public void testConstructor()
    {
        //write testfile
        try (FileWriter wr = new FileWriter("Test123.txt", StandardCharsets.UTF_8))
        {
            wr.write("ad dwd TeST\n"); //Write tested String into file
            wr.write("asf");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        //actual testclass reading the testfile
        File file = new File("Test123.txt");
        RowFilereader reader = RowFilereader.getInstance(file);
        
        RowFilereader readerString = RowFilereader.getInstance("Test123.txt");
        assertNotNull(reader);
        assertTrue(reader.getList().get(0).equals("ad dwd TeST"));
        assertTrue(readerString.getArray()[0].equals("ad dwd TeST"));
        assertTrue(reader.getArray()[1].equals("asf"));
        
        //delete testfile
        file.delete();
    }
}
