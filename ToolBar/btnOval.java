package ToolBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;
import Canvas.CanvasPanel.Mode;

/**
 * 橢圓按鈕類別
 */
public class btnOval extends btn_base {
    public btnOval() {
        // 調用父類構造函數，傳入特定圖示的路徑
        super("/Resource/UseCaseIcon.png");

        // 設定這個按鈕特有的屬性
        this.setToolTipText("橢圓");

        // 添加動作監聽器
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    canvasPanel.setMode(Mode.OVAL);
                }
            }
        });
    }
}