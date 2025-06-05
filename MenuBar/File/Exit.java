package MenuBar.File;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Exit extends JMenuItem{
    // 建構子
    public Exit(JFrame frame) {
        super("Exit"); // 設定 JMenuItem 標題

        // 為 Exit 添加事件
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
