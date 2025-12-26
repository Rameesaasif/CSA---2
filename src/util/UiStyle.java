package util;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

public final class UiStyle {
    private UiStyle() {}

    public static Border pagePadding() {
        return BorderFactory.createEmptyBorder(12, 12, 12, 12);
    }

    public static void applyTableStyle(JTable table) {
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        Font base = UIManager.getFont("Label.font");
        if (base != null) {
            table.setFont(base.deriveFont(Font.PLAIN, base.getSize() + 1));
            table.getTableHeader().setFont(base.deriveFont(Font.BOLD, base.getSize() + 1));
        }
    }

    public static void setFontIfAvailable(JComponent c, String uiKey, int styleDelta, float sizeDelta) {
        Font f = UIManager.getFont(uiKey);
        if (f != null) c.setFont(f.deriveFont(f.getStyle() + styleDelta, f.getSize2D() + sizeDelta));
    }
}
