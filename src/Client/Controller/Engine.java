package Client.Controller; /**
 * Created by Алексей on 01.04.2015.
 */

import Client.Core.ParentObject;
import Client.Core.SimpleElement;
import Client.View.GUI;
import Main.StartPoint;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Engine {

    ParentObject NewObject = new ParentObject();
    SimpleElement NewElement;
    BufferedReader fileReader;
    Connection connection;
    Statement statement;
    PrintWriter writer;
    Socket sock;
    BufferedReader netReader;
    Boolean serverFlag;

    final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final String CONNECTION = "jdbc:derby:AccountDatabase;create=true";

    public boolean deleteByIndex(String sIndex) {
        boolean isDeleted = false;
        try {
            int index = Integer.parseInt(sIndex);
            NewObject.delIndMyObject(index - 1);
            GUI.nNum--;
            isDeleted = true;
        } finally {
            return isDeleted;
        }
    }

    public String deleteByName(String nameToDel) {
        String Deleted = "Ошибка! Сообщение не найдено!";
        for (SimpleElement simpleElement : NewObject.getMyObject()) {
            if (simpleElement.getTitle().equals(nameToDel)) {
                Deleted = NewObject.aboutMyObject(simpleElement);
                NewObject.delMyObject(simpleElement);
                GUI.nNum--;
                break;
            }
        }
        return Deleted;
    }

    public boolean isNew(String title, String content) {
        boolean isNew = true;
        NewElement = new SimpleElement(title, content);
        for (SimpleElement simpleElement : NewObject.getMyObject()) {
            if (simpleElement.equals(NewElement) && simpleElement.hashCode() == NewElement.hashCode()) {
                isNew = false;
                break;
            }
        }
        return isNew;
    }

    public String CreateNews(String title, String content) {
        NewElement = new SimpleElement(title, content);
        String newNote;
        //NewElement.setCalendar(keyboardInt.nextInt(), keyboardInt.nextInt(), keyboardInt.nextInt(), keyboardInt.nextInt(), keyboardInt.nextInt());
        if (NewObject.sizeMyObject() > 0) {
            if (isNew(title, content) == true) {
                NewElement = new SimpleElement(title, content);
                NewObject.addToMyObject(NewElement);
                GUI.nNum = NewObject.sizeMyObject();
                newNote = NewObject.aboutMyObject(NewElement);
            } else {
                newNote = ("Такая запись уже существует!");
            }
        } else {
            NewObject.addToMyObject(NewElement);
            newNote = NewObject.aboutMyObject(NewElement);
            GUI.nNum++;
        }
        return newNote;
    }

    public String sortByContent() {
        NewObject.sortMyObjectByContent();
        String sorted = "В памяти нет ни одной записи!";
        if (NewObject.sizeMyObject() != 0) {
            sorted = "Результаты сортировки:";
            for (SimpleElement simpleElement : NewObject.getMyObject()) {
                sorted = sorted + "\n" + NewObject.aboutMyObject(simpleElement) + "\n";
            }
        }
        return sorted;
    }

    public String showAll() {
        String notSorted = "Записей нет!";
        if (NewObject.sizeMyObject() != 0) {
            notSorted = "Сейчас в памяти:";
            for (SimpleElement simpleElement : NewObject.getMyObject()) {
                notSorted = notSorted + "\n" + NewObject.aboutMyObject(simpleElement) + "\n";
            }
        }
        return notSorted;
    }

    public String sortByName() {
        NewObject.sortMyObjectByName();
        String sorted = "В памяти нет ни одной записи!";
        if (NewObject.sizeMyObject() != 0) {
            sorted = "Результаты сортировки:";
            for (SimpleElement simpleElement : NewObject.getMyObject()) {
                sorted = sorted + "\n" + NewObject.aboutMyObject(simpleElement) + "\n";
            }
        }
        return sorted;
    }

    public String SearchByIndex(String nameForSearch) {
        String searchResult = "Результаты поиска";
        for (SimpleElement simpleElement : NewObject.getMyObject()) {
            if (simpleElement.getTitle().equals(nameForSearch)) {
                searchResult = NewObject.aboutMyObject(simpleElement);
            }
        }
        return searchResult;
    }

    public String clearAll() {
        String searchResult = "Все записи удалены!";
        NewObject.clearMyObject();
        GUI.nNum = 0;
        return searchResult;
    }

    public void readFile(String fileName) throws IOException {
        File myFile = new File(fileName + ".txt");
        fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
        String line;
        while ((line = fileReader.readLine()) != null) {
            String[] tokens = line.split("/");
            if (isNew(tokens[0], tokens[1]) == true) {
                NewObject.addToMyObject(new SimpleElement(tokens[0], tokens[1]));
                GUI.nNum++;
            }
        }
    }

    public void saveToFile(String fileName) {
        Writer writer = null;
        try {
            writer = new FileWriter(fileName);
            int i = 0;
            for (SimpleElement simpleElement : NewObject.getMyObject()) {
                writer.write(NewObject.getMyObject(i).getTitle() + "/" + NewObject.getMyObject(i).getContent());
                writer.write(System.getProperty("line.separator"));
                i++;
            }
        } catch (Exception e) {
            Logger.getLogger(StartPoint.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public String connectDB() {
        String result = "Ошибка! Соединение не установлено!";
        try {
            Class.forName(DRIVER).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(CONNECTION);
            statement = connection.createStatement();
            GUI.dbConnection = "DB: ON  ";
            GUI.dbConnectionB = 1;
            result = "Соединение установлено.";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String disconnectDB (){
       String result = "Ошибка!";
        try{
            connection.close();
            result = "Соединение разорвано!";
            GUI.dbConnection = "DB: OFF";
            GUI.dbConnectionB = 0;
        }catch (SQLException e){}
        return result;
    }

    public String createDBTable(String tableName) {
        String result = "";
        try {
            statement.executeUpdate(
                    "create table " + tableName
                            + "(NAME VARCHAR (32) NOT NULL PRIMARY KEY, CONTENT VARCHAR (32))");
            result = "Таблица " + tableName + " успешно создана.";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String saveToDB(String tableName) {
        String result = "";
        String dbNAME;
        String dbCONTENT;
        try {
            for (SimpleElement simpleElement : NewObject.getMyObject()) {
                dbNAME = simpleElement.getTitle();
                dbCONTENT = simpleElement.getContent();
                statement.executeUpdate("insert into " + tableName + " values ('" + dbNAME + "', '" + dbCONTENT + "')");
            }
        } catch (SQLException e) {
        }
        return result;
    }

    public String readDB(String tableName) {
        String result = "Ошибка! Данные не загружены!";
        ResultSet resultset;
        try {
            resultset = statement.executeQuery("select * from " + tableName);
            while (resultset.next()) {
                if (isNew(resultset.getString("NAME"), resultset.getString("CONTENT"))) {
                    CreateNews(resultset.getString("NAME"), resultset.getString("CONTENT"));
                    GUI.nNum++;
                }
            }
            result = "Данные загружены успешно.";
        } catch (SQLException e) {
        }
        return result;
    }

    public String setUpConnection() {
        String result = "Ошибка! Соединение не установлено!";
        try  {
            sock = new Socket("127.0.0.1", 5001);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            netReader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            result = "Соединение установлено!";
            GUI.chatConnection = "Chat: ON  ";
            GUI.chatConnectionB = 1;
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    class IncomingReader implements Runnable {
        public void run() {
            incomingStream();}
    }

    public String outgoingStream(String title, String content) {
        String result = "Ошибка! Данные не отправлены!";
        try {
            writer.write(title + "/" + content);
            writer.write(System.getProperty("line.separator"));
            result = "Данные отправлены успешно!";
            writer.flush();
        } catch (Exception e) {
            Logger.getLogger(StartPoint.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public void newThread () {
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    public void incomingStream() {
        try {
            String line;
            while ((line = netReader.readLine()) != null) {
                String[] tokens = line.split("/");
                GUI.titleField.setText(tokens[0]);
                GUI.textField.setText(tokens[1]);
            }
            netReader.close();
        } catch (Exception e) {
            Logger.getLogger(StartPoint.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String disconnectChat (){
        String result = "Ошибка!";
        try{
            sock.close();
            writer = null;
            netReader = null;
            GUI.chatConnection = "Chat: OFF";
            GUI.chatConnectionB = 0;
            result = "Соединение разорвано!";
        }catch (IOException e){}
        return result;
    }
}