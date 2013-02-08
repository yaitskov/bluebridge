package com.github.bluebridge.pclient.form;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * Daneel Yaitskov
 */
public class FileModelSelector {


    private JFileChooser chooser;

    public FileModelSelector() {
        chooser = new JFileChooser();
        chooser.setApproveButtonText("Print 3D model");
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        // setFileFilter actually add new only.
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory()
                        || file.getName().endsWith(".gcode");
            }

            @Override
            public String getDescription() {
                return "gcode extension";
            }
        });
    }

    /**
     * Returns selected file of 3d model or null otherwise.
     * @return selected file of 3d model or null otherwise
     */
    public File select(Component main) {
        int res = chooser.showOpenDialog(main);
        if (res != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }
}
