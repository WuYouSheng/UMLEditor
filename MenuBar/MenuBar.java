package MenuBar;

import MenuBar.Edit.Edit;
import MenuBar.File.File;
import Canvas.CanvasPanel;

import javax.swing.*;

/**
 * 選單欄類別
 */
public class MenuBar extends JMenuBar {
    private File fileMenu;
    private Edit editMenu;
    private CanvasPanel canvasPanel;

    // 構造函數
    public MenuBar(JFrame frame) {
        // 建立選單
        fileMenu = new File(frame);
        editMenu = new Edit(frame);

        // 加入選單
        this.add(fileMenu);
        this.add(editMenu);
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;

        // 更新子選單
        fileMenu.setCanvasPanel(canvasPanel);
        editMenu.setCanvasPanel(canvasPanel);
    }
}