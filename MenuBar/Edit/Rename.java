package MenuBar.Edit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 重命名選單項目類
 */
public class Rename extends JMenuItem {
    private CanvasPanel canvasPanel;

    public Rename(JFrame frame) {
        super("Rename"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null && canvasPanel.hasSelectedShapes()) {
                    // 取得目前選取物件的名稱
                    String currentName = canvasPanel.getSelectedShapeName();

                    // 顯示對話框讓使用者輸入新名稱
                    String newName = JOptionPane.showInputDialog(
                            frame,
                            "輸入新的名稱:",
                            "重命名",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            currentName
                    ).toString();

                    // 檢查輸入是否有效
                    if (newName != null && !newName.trim().isEmpty()) {
                        canvasPanel.renameSelectedShape(newName);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "請先選取一個物件");
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