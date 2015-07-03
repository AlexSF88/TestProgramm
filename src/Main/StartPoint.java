package Main;

import Client.View.GUI;

import javax.swing.*;

/**
 * Created by Алексей on 02.04.2015.
 */
public class StartPoint {
    public static void main(String[] args) {
        GUI frame = new GUI();
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
        frame.setFrame();
    }
}
