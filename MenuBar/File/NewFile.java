package MenuBar.File;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 新建檔案選單項目類
 */
public class NewFile extends JMenuItem {
    private CanvasPanel canvasPanel;

    // 建構子
    public NewFile(JFrame frame) {
        super("New File"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 詢問是否確定要新建檔案
                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "要建立新檔案嗎？這將清空當前內容。",
                        "新建檔案",
                        JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    if (canvasPanel != null) {
                        canvasPanel.clearAll();
                        JOptionPane.showMessageDialog(frame, "已建立新檔案");
                    } else {
                        JOptionPane.showMessageDialog(frame, "無法建立新檔案");
                    }
                }
            }
        });
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }
}