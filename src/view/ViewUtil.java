package view;

import javax.swing.JTable;

public final class ViewUtil {
    private ViewUtil() {}

    public static int selectedModelRow(JTable table) {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return -1;
        return table.convertRowIndexToModel(viewRow);
    }
}
