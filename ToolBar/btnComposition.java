package ToolBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;
import Canvas.CanvasPanel.Mode;

/**
 * 組合連結按鈕類別
 */
public class btnComposition extends btn_base {
    public btnComposition() {
        // 調用父類構造函數，傳入特定圖示的路徑
        super("/Resource/CompositionLineIcon.png");

        // 設定這個按鈕特有的屬性
        this.setToolTipText("組合連結");

        // 添加動作監聽器
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.setMode(Mode.COMPOSITION);
                }
            }
        });
    }
}