package ToolBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;
import Canvas.CanvasPanel.Mode;

/**
 * 矩形按鈕類別
 */
public class btnRect extends btn_base {
    public btnRect() {
        // 調用父類構造函數，傳入特定圖示的路徑
        super("/Resource/ClassIcon.png");

        // 設定這個按鈕特有的屬性
        this.setToolTipText("矩形");

        // 添加動作監聽器
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.setMode(Mode.RECT);
                }
            }
        });
    }
}