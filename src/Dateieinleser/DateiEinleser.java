package Dateieinleser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Eine Klasse zum Einlesen von Dateien.
 */
public class DateiEinleser
{
    private static JFrame _frame = null;
    private static JFileChooser _fileChooser = null;
    
    /**
     * Lies eine Datei ein und liefere diese. Die Datei wird interaktiv vom Benutzer gewaehlt.
     */
    public static File liesBilddaten()
    {
        if (_fileChooser == null)
        {
            _fileChooser = new JFileChooser("D:/Musik");
            _frame = new JFrame();
            _frame.pack();
            _frame.setAlwaysOnTop(true);
        }
        
        int returnVal = _fileChooser.showOpenDialog(_frame);
        
        if (returnVal != JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        
        return _fileChooser.getSelectedFile();
    }
}
