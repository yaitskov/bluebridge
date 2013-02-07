package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param b
     * @param b1
     * @return
     */
    @Override
    public Component getListCellRendererComponent(
            JList jList, Object o,
            int i, boolean b, boolean b1) {
        Printer printer = (Printer)o;
        PrinterListCell cell = new PrinterListCell(printer);
        return cell;
    }
}
