package MenuBar.Edit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 群組選單項目類
 */
public class Group extends JMenuItem {
    private CanvasPanel canvasPanel;

    public Group(JFrame frame){
        super("Group"); // 設定 JMenuItem 標題

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.groupSelectedShapes();
                } else {
                    JOptionPane.showMessageDialog(frame, "Group clicked");
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