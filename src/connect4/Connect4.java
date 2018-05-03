package connect4;

import java.awt.*;
import javax.swing.*;
import actions.*;

public class Connect4 {

    public class MyGraphics extends JComponent {

        private static final long serialVersionUID = 1L;

        MyGraphics() {
            setPreferredSize(new Dimension(480, 480));
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    public void createGUI() {
        final JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(new MyGraphics());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Connect4 GUI = new Connect4();
                GUI.createGUI();
            }
        });
    }
}