package Dateieinleser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
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
     * Lies eine GIF-Datei ein und liefere die Bilddaten als Array. Die Datei wird interaktiv vom
     * Benutzer gewaehlt.
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
