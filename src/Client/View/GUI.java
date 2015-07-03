package Client.View;

import Client.Controller.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI {
    Engine dataEngine = new Engine();
    JFrame frame = new JFrame("0.001A");

    public static JTextField titleField;
    public static JTextArea textField;

    JPanel NewMessagePanel;
    JPanel HelpPanel;
    JPanel textPanel;
    JPanel monitor;

    JButton [] buttonsHelp;
    JButton [] buttonsAction;
    public static int nNum, dbConnectionB, chatConnectionB;
    public static String dbConnection = "DB: OFF";
    public static String chatConnection = "Chat: OFF";
    JButton num, textColor, conDB, conChat, send, runServer;

    int textColorN = 0;

    public void setTitleField(String titleField) {
        this.titleField.setText(titleField);
    }
    public void setTextField(String textField) {
        this.textField.setText(textField);
    }

    public void setFrame () {

        Font f1 = new Font("Courier", Font.BOLD + Font.ITALIC, 13),
                f2 = new Font("Courier", Font.PLAIN, 13);

        num = new JButton("Всего записей: " + nNum + "");
        conDB = new JButton(dbConnection);
        conChat = new JButton(chatConnection);
        send = new JButton("Send");

        NewMessagePanel = new JPanel();
        HelpPanel = new JPanel();
        textPanel = new JPanel();
        monitor = new JPanel();

        titleField = new JTextField(40);
        textField = new JTextArea(10, 1);

        titleField.setFont(f1);
        textField.setFont(f2);

        textField.setWrapStyleWord(true);

        titleField.setText("Привет Мир!");
        textField.setText("Я - тестовая (учебная) версия виртуального дневника для всякой всячины.\n\n" +
                "Справа Вы найдете панель с кнопками: нажмите на \"?\" чтобы узнать, что делает кнопка.\n\n" +
                "На данный момент я умею:\n" +
                "- Запоминать, сортировать, искать, показывать и удалять записи.\n" +
                "- Cохранять записи в файлах, читать из файлов (попробуйте загрузить\"file.txt\"" +
                "- для этого достаточно ввести в верхнее поле \"file\" и нажать кнопку \"Загрузить из файла\").\n" +
                "- Cоздавать таблицы в базе данных (DB).\n" +
                "- Загружать записи в DB и обратно.\n" +
                "- Отправлять сообщения другим таким программам через сеть\n(не забудьте запустить Main.ChatServer!).");

        titleField.setForeground(Color.blue);
        textField.setForeground(Color.blue);
        textField.setSelectedTextColor(Color.white);
        textField.repaint();

        String[] buttonNames = new String[]{"Новая запись", "Удаление по имени", "Удаление по индексу",
                "Поиск по имени", "Сортировать по имени", "Сортировать по содержанию", "Удалить все записи",
                "Загрузить из файла", "Сохранить в файл", "Загрузить из DB",
                "Сохранить в DB", "Создать таблицу в DB"};
        buttonsAction = new JButton[buttonNames.length];
        for (int i = 0; i < buttonsAction.length; i++) {

            buttonsAction[i] = new JButton(buttonNames[i]);
            buttonsAction[i].addActionListener(new buttonActionListener());
            buttonsAction[i].setFont(f2);
            NewMessagePanel.add(buttonsAction[i]);
        }

        buttonsHelp = new JButton[buttonNames.length];
        for (int i = 0; i < buttonsHelp.length; i++) {

            buttonsHelp[i] = new JButton("?");
            buttonsHelp[i].addActionListener(new HelpListener());
            HelpPanel.add(buttonsHelp[i]);
        }

        frame.getContentPane().add(BorderLayout.WEST, textPanel);
        frame.getContentPane().add(BorderLayout.CENTER, NewMessagePanel);
        frame.getContentPane().add(BorderLayout.EAST, HelpPanel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textField.setLineWrap(true);
        titleField.addActionListener(new PressEnterListener());

        JScrollPane scroller = new JScrollPane(textField);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        NewMessagePanel.setLayout(new GridLayout(buttonNames.length, 1));

        textPanel.setBackground(Color.white);
        textPanel.setLayout(new BorderLayout());

        HelpPanel.setLayout(new GridLayout(buttonNames.length, 1));

        textColor = new JButton("Color");
        textColor.addActionListener(new monitorActionListener());

        JPanel monCent = new JPanel();
        JPanel monLeft = new JPanel();
        JPanel monRight = new JPanel();

        conChat.addActionListener(new monitorActionListener());
        conDB.addActionListener(new monitorActionListener());
        num.addActionListener(new monitorActionListener());
        send.addActionListener(new monitorActionListener());

        monLeft.add(send);
        monLeft.add(textColor);
        monCent.add(num);
        monRight.add(conChat);
        monRight.add(conDB);

        monitor.setLayout(new BorderLayout());
        monitor.add(BorderLayout.WEST, monLeft);
        monitor.add(BorderLayout.CENTER, monCent);
        monitor.add(BorderLayout.EAST,monRight);

        textPanel.add(BorderLayout.NORTH, titleField);
        textPanel.add(BorderLayout.CENTER, scroller);
        textPanel.add(BorderLayout.SOUTH, monitor);
        frame.pack();
        frame.setSize(791, 414);
        frame.setBackground(Color.gray);
        frame.setVisible(true);
    }

    public String getTextField() {
        return textField.getText();
    }

    public String getTitleField() {
        return titleField.getText();
    }

    class PressEnterListener implements ActionListener {
        public void actionPerformed (ActionEvent event) {
            textField.requestFocus();
            textField.selectAll();
        }
    }

    class monitorActionListener  implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            Object src = event.getSource();

            if (src == send) {
                titleField.setText(dataEngine.outgoingStream(getTitleField(), getTextField()));
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == num) {
                titleField.setText("");
                textField.setText(dataEngine.showAll());
                titleField.requestFocus();
                titleField.selectAll();
            }  else if (src == conDB && dbConnectionB == 0) {
                dataEngine.connectDB();
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == conDB && dbConnectionB == 1) {
                dataEngine.disconnectDB();
                titleField.requestFocus();
                titleField.selectAll();
            } else  if (src == conChat && chatConnectionB == 0) {
                dataEngine.setUpConnection();
                titleField.requestFocus();
                titleField.selectAll();
                conChat.setText(chatConnection);
                dataEngine.newThread();
            } else if (src == conChat && chatConnectionB == 1) {
                dataEngine.disconnectChat();
                titleField.requestFocus();
                titleField.selectAll();
            }else if (src == textColor && textColorN == 1) {
                titleField.setForeground(Color.blue);
                textField.setForeground(Color.blue);
                textColorN = 0;
            } else if(src == textColor && textColorN == 0){
                titleField.setForeground(Color.black);
                textField.setForeground(Color.black);
                textColorN = 1;
            }
            conChat.setText(chatConnection);
            conDB.setText(dbConnection);
        }
    }

    class buttonActionListener  implements ActionListener  {

        public void actionPerformed (ActionEvent event) {

            Object src = event.getSource();

            if (src == buttonsAction[0]){
                textField.setText(dataEngine.CreateNews(getTitleField(), getTextField()));
                titleField.setText("Вы записали:");
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[1]){
                textField.setText(dataEngine.deleteByName(getTitleField()));
                titleField.setText("Вы удалили запись:");
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[2]){
                if (dataEngine.deleteByIndex(getTitleField())){textField.setText("Запись удалена!");}
                else {textField.setText("Ошибка! Сообщение не найдено!");}
                titleField.setText("Вы удалили запись:");
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[3]){
                textField.setText(dataEngine.SearchByIndex(getTitleField()));
                titleField.setText("");
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[4]){
                titleField.setText("Сортировка по имени.");
                textField.setText(dataEngine.sortByName());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[5]){
                titleField.setText("Сортировка по содержимому.");
                textField.setText(dataEngine.sortByContent());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[6]){
                titleField.setText(dataEngine.clearAll());
                titleField.requestFocus();
                titleField.selectAll();
                textField.setText("");
            } else if (src == buttonsAction[7]){
                try {
                    dataEngine.readFile(getTitleField());} catch (FileNotFoundException e) {} catch (IOException e){}
                titleField.setText("Сейчас в памяти:");
                textField.setText(dataEngine.showAll());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[8]){
                dataEngine.saveToFile(titleField.getText());
                titleField.setText("Вы сохранили в файл \""+ titleField.getText() + "\":");
                textField.setText(dataEngine.showAll());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[9]){
                titleField.setText(dataEngine.readDB(getTitleField()));
                textField.setText(dataEngine.showAll());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[10]){
                dataEngine.saveToDB(getTitleField());
                titleField.setText("Вы сохранили в DB:");
                textField.setText(dataEngine.showAll());
                titleField.requestFocus();
                titleField.selectAll();
            } else if (src == buttonsAction[11]) {
                titleField.setText(dataEngine.createDBTable(getTitleField()));
                textField.setText("");
                titleField.requestFocus();
                titleField.selectAll();
            }
            num.setText("Всего записей: " + nNum + "");
        }
    }

    class HelpListener  implements ActionListener  {

        public void actionPerformed (ActionEvent event) {

            Object src = event.getSource();
            String setTitle = null;
            String setText = null;

            if (src == buttonsHelp[0]){
                setTitle = "Введите заголовок заметки";
                setText = "Введите текст заметки";
            } else if (src == buttonsHelp[1]){
                setTitle = "";
                setText = "Введите имя заметки для удаления";
            } else if (src == buttonsHelp[2]){
                setTitle = "";
                setText = "Введите индекс заметки для удаления";
            } else if (src == buttonsHelp[3]){
                setTitle = "";
                setText = "Введите имя заметки для поиска";
            } else if (src == buttonsHelp[4]){
                setTitle = "";
                setText = "Нажмите, чтобы сортировать массив по имени";
            } else if (src == buttonsHelp[5]){
                setTitle = "";
                setText = "Нажмите, чтобы сортировать массив по содержимому";
            } else if (src == buttonsHelp[6]){
                setTitle = "";
                setText = "Нажмите, чтобы удалить все записи в массиве";
            } else if (src == buttonsHelp[7]){
                setTitle = "";
                setText = "Введите имя файла (без расширения) для загрузки";
            } else if (src == buttonsHelp[8]){
                setTitle = "";
                setText = "Введите имя файла (включая расширение) для сохранения";
            } else if (src == buttonsHelp[9]){
                setTitle = "";
                setText = "Нажмите чтобы загрузить записи из базы данных";
            } else if (src == buttonsHelp[10]){
                setTitle = "";
                setText = "Введите имя таблицы DB для записи";
            } else if (src == buttonsHelp[11]){
                setTitle = "";
                setText = "Введите имя новой таблицы DB";
            }
            titleField.setText(setTitle);
            textField.setText(setText);
            titleField.selectAll();
            titleField.requestFocus();
        }
    }
}