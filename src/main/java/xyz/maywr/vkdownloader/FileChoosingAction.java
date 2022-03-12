package xyz.maywr.vkdownloader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChoosingAction implements ActionListener {

    private JTextField field;

    public FileChoosingAction(JTextField field) {
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowsFileChooser fChooser = new WindowsFileChooser(System.getProperty("user.home"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML docs", "html");
        fChooser.setFileFilter(filter);

        int stage = fChooser.showOpenDialog(null);

        if(stage == WindowsFileChooser.APPROVE_OPTION){
            field.setText(fChooser.getSelectedFile().getAbsolutePath());
        }
    }
}
