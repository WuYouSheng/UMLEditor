package MenuBar.Edit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 刪除選單項目類
 */
public class ClearAll extends JMenuItem {
    private CanvasPanel canvasPanel;

    public ClearAll(JFrame frame){
        super("ClearAll"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.clearAll();
                } else {
                    JOptionPane.showMessageDialog(frame, "clear All Selected");
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