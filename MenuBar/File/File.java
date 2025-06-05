package MenuBar.File;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 檔案選單類別
 */
public class File extends JMenu {
    private CanvasPanel canvasPanel;
    private NewFile newFileItem;
    private SaveFile saveFileItem;
    private Exit exitItem;

    public File(JFrame frame) {
        super("File");

        // 建立選單項目
        newFileItem = new NewFile(frame);
        saveFileItem = new SaveFile(frame);
        exitItem = new Exit(frame);

        // 加入選單
        this.add(newFileItem);
        this.add(saveFileItem);
        this.addSeparator(); // 分隔線
        this.add(exitItem);
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;

        // 更新子選單項目的畫布引用
        newFileItem.setCanvasPanel(canvasPanel);
        saveFileItem.setCanvasPanel(canvasPanel);
    }
}