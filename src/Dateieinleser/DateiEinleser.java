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
     * Default method, if no path given, choose default path C:/ (rip Linux)
     * @return the musicfile chosen below
     */
    public static File liesDatei()
    {
        return liesDatei("C:/");
    }
    
    /**
     * Lies eine Datei ein und liefere diese. Die Datei wird interaktiv vom Benutzer gewaehlt.
     * @return Chosen musicfile, null if aborted
     */
    public static File liesDatei(String path) throws NullPointerException
    {
        assert path != null: "path is null";
        
        if (_fileChooser == null)
        {
            _fileChooser = new JFileChooser(path);
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
