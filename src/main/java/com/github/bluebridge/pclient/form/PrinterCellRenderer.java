package com.github.bluebridge.pclient.form;

import com.github.bluebridge.pclient.printer.Printer;

import javax.swing.*;
import java.awt.*;

/**
 * Daneel Yaitskov
 */
public class PrinterCellRenderer implements ListCellRenderer {


    /**
     * It's called multiple times. Each time for redraw.
     * Result object is not reused.
     * @param jList
     * @param o
     * @param i
     * @param selected
     * @param b1
     * @return
     */
    @Override
    public Component getListCellRendererComponent(
            JList jList, Object o,
            int i, boolean selected, boolean b1) {
        Printer printer = (Printer)o;
        PrinterListCell cell = new PrinterListCell(selected, printer);

        return cell;
    }
}
