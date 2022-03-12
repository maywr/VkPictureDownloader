package xyz.maywr.vkdownloader;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartAction implements ActionListener {

    private String pathToFile;
    private JTextField field;
    private JProgressBar bar;
    public ArrayList<String> links;

    public StartAction(JTextField field, JProgressBar bar) {
        this.bar = bar;
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pathToFile = field.getText();
        VkPicturesDownloader.logClear();
        VkPicturesDownloader.log("запуск...");
        try {
            Thread.sleep(80);
        } catch (InterruptedException ignored) {}
        VkPicturesDownloader.log("читаем файл...");
        if(pathToFile == null || pathToFile.isEmpty()){
            VkPicturesDownloader.log("ОШИБКА: файл который вы выбрали пустой или не существует");
            JOptionPane.showMessageDialog(null, "Файл который вы выбрали пустой или не существует", "ОШИБКА", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File file = new File(pathToFile);
        BufferedReader reader= null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception exception) {
            VkPicturesDownloader.log("ОШИБКА: невозможно прочитать файл");
        }

        String line; StringBuilder sb = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ignored) {}
        String html = sb.toString();

        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        File imageDir = new File(System.getProperty("user.home") + "\\" + new File(pathToFile).getName().replace(".html", ""));
        imageDir.mkdir();

        ArrayList<Element> elements = Jsoup.parse(html).getAllElements();
        links = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (!element.attributes().get("id").contains("msg")) continue;
            if (element.getElementsByClass("attacment").hasText() && element.getElementsByClass("attacment").text().contains("photo")) {
                String text = element.getElementsByClass("attacment").get(0).getAllElements().get(0).toString();
                Matcher urlMatcher = pattern.matcher(text);
                while (urlMatcher.find()) {
                    String link = text.substring(urlMatcher.start(0), urlMatcher.end(0)).replace("&amp", "&").replace(";", "");
                    if (!link.contains("userapi")) continue;
                    links.add(link);
                }
            }
        }
        VkPicturesDownloader.log(links.size() + " картинок успешно найдено...");

        Thread downloadinThread = new Thread(){
            @Override
            public void run(){
                for (int i = 1; i < links.size(); i++) {

                    String link = links.get(i);
                    try {
                        FileUtils.copyURLToFile(new URL(link), new File(imageDir.getAbsolutePath() + "\\" + i + ".png"));
                        System.out.println("downloadin " + i);
                    } catch (Exception e) {
                        VkPicturesDownloader.log("ОШИБКА: невозможно скачать изображение номер " + i);
                    }
                    bar.setMaximum(links.size());
                    bar.setValue(i);
                }
                try {
                    Desktop.getDesktop().open(imageDir);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }
                VkPicturesDownloader.log("Готово! Скачанно " + links.size() + "картинок");
            }
        };
        downloadinThread.start();
    }
}
