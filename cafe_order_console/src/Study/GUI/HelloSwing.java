package Study.GUI;

import javax.swing.*;

public class HelloSwing {
    public static void main(String[] args) {
        // 1. 창 만들기
        JFrame frame = new JFrame("내 첫 GUI");

        // 2. 버튼 만들기
        JButton button = new JButton("눌러봐!");

        // 3. 버튼 클릭시 동작 정의
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "반갑습니다!");
        });

        // 4. 창에 버튼 추가
        frame.add(button);

        // 5. 창 설정
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
