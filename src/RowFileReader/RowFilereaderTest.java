package RowFileReader;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class for RowFileReader
 */

public class RowFileReaderTest
{
    File _testFile = new File("Test.txt");
    String _line1 = "ad dwd TeST";
    String _line2 = "asf";

    @Test
    public void testConstructor() {
        //write test file
        try (FileWriter wr = new FileWriter(_testFile, StandardCharsets.UTF_8)) {
            wr.write(_line1 + "\n"); //Write tested String into file
            wr.write(_line2 + "\n");
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Unexpected error while reading from the file!");
        }

        //actual test class reading the test file
        RowFileReader reader = RowFileReader.getInstance(_testFile);
        assertNotNull(reader);
        assertEquals(reader.getList().get(0), _line1);
        assertEquals(reader.getList().get(1), _line2);
        assertThrows(IndexOutOfBoundsException.class, () -> reader.getList().get(2));

        //delete test file
        assertTrue(_testFile.delete());
    }
}
