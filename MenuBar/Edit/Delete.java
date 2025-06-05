package MenuBar.Edit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 刪除選單項目類
 */
public class Delete extends JMenuItem {
    private CanvasPanel canvasPanel;

    public Delete(JFrame frame){
        super("Delete"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.deleteSelectedShapes();
                } else {
                    JOptionPane.showMessageDialog(frame, "Delete clicked");
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