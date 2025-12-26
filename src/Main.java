import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import data.CsvLoader;
import data.DataStore;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Nimbus is built-in (no external libs) and looks much cleaner than the default.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DataStore store = new DataStore();
            CsvLoader loader = new CsvLoader(store);

            // Load all CSVs on startup (shows friendly error dialogs inside the UI if something is missing).
            loader.loadAllFromDataFolder("data");

            MainFrame frame = new MainFrame(store);
            frame.setVisible(true);
        });
    }
}
