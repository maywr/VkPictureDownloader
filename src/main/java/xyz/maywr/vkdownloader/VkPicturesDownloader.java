package xyz.maywr.vkdownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class VkPicturesDownloader {

    private static JTextArea log;
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("VkPicturesDownloader");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(screenSize.width / 2 - (500 / 2), screenSize.height / 2 - (500 / 2), 500, 500);

        try {
            frame.setIconImage(ImageIO.read(VkPicturesDownloader.class.getClassLoader().getResourceAsStream("smallLogo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image logo = null;
        try {
            logo = ImageIO.read(VkPicturesDownloader.class.getClassLoader().getResourceAsStream("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon imageIcon = new ImageIcon(logo);
        JLabel logoPicLabel = new JLabel(imageIcon);
        logoPicLabel.setBounds(0, 0, 500, 150);
        frame.add(logoPicLabel);

        JTextField path = new JTextField();
        path.setBounds(10, 155, 425, 25);
        path.setEditable(false);
        frame.add(path);

        JButton fileChoosingButton = new JButton("...");
        fileChoosingButton.setBounds(440, 155, 30, 25);
        fileChoosingButton.setSelected(false);
        fileChoosingButton.addActionListener(new FileChoosingAction(path));
        frame.add(fileChoosingButton);

        log = new JTextArea(16, 58);
        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        log.setBounds(10, 190, 460, 180);
        log.setFont(new Font("Arial", Font.PLAIN, 13));
        frame.add(log);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setBounds(10, 380, 460, 30);
        frame.add(bar);

        JButton startButton = new JButton("Запуск");
        startButton.setBounds(5, 415, 470, 30);
        startButton.addActionListener(new StartAction(path, bar));
        frame.add(startButton);

        frame.setVisible(true);
    }

    public static void log(String text){
        log.append(text + "\n");
    }

    public static void logClear(){
        log.setText("");
    }
}
