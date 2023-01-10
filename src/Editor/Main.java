package Editor;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        FileWindow inter = new FileWindow();
        inter.pack();
        // 设置关闭退出程序
        inter.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //设置窗口大小
        inter.setBounds(200, 180, 550, 360);
        inter.setVisible(true);
    }
}