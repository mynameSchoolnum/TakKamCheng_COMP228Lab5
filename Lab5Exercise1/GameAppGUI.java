package Lab5Exercise1;

import javax.swing.*;
import java.awt.*;

public class GameAppGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Management Application");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Insert Player", InsertPlayer.createInsertPlayerTab());
            tabbedPane.addTab("Insert Game", InsertGame.createInsertGameTab());
            tabbedPane.addTab("Insert Player & Game", InsertPlayerAndGame.createInsertPlayerAndGamePanel());

            frame.add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
