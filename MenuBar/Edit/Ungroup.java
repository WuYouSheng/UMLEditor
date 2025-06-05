package MenuBar.Edit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 解除群組選單項目類
 */
public class Ungroup extends JMenuItem {
    private CanvasPanel canvasPanel;

    public Ungroup(JFrame frame){
        super("Ungroup"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.ungroupSelectedShape();
                } else {
                    JOptionPane.showMessageDialog(frame, "Ungroup");
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