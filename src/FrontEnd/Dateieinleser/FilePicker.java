package FrontEnd.Dateieinleser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * A class that allows a user to interactively pick a file.
 */
public class FilePicker
{
    private static JFrame _frame = null;
    private static JFileChooser _fileChooser = null;

    /**
     * Picks a file interactively chosen by the user:
     *
     * @return Chosen music file, null if aborted
     */
    public static File pickFile(String path) throws NullPointerException {
        if (path == null)
            return null;

        if (_fileChooser == null) {
            _fileChooser = new JFileChooser(path);
            _frame = new JFrame();
            _frame.pack();
            _frame.setAlwaysOnTop(true);
        }

        int returnVal = _fileChooser.showOpenDialog(_frame);

        if (returnVal != JFileChooser.APPROVE_OPTION)
            return null;

        return _fileChooser.getSelectedFile();
    }
}
