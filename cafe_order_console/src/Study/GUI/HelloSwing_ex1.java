package Study.GUI;

import javax.swing.*;

public class HelloSwing_ex1 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("내 두번째 GUI");

        JButton button = new JButton("클릭해봐");

        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "반갑습니다!");
        });

        frame.add(button);

        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
