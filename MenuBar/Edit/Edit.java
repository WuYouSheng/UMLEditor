package MenuBar.Edit;

import MenuBar.File.Exit;
import MenuBar.File.NewFile;
import Canvas.CanvasPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 編輯選單類別
 */
public class Edit extends JMenu {
    private CanvasPanel canvasPanel;
    private Rename renameItem;
    private Group groupItem;
    private Ungroup ungroupItem;
    private Delete deleteItem;
    private ClearAll clearAllItem;
    private Label labelItem;

    public Edit(JFrame frame) {
        super("Edit");

        // 建立選單項目
        renameItem = new Rename(frame);
        groupItem = new Group(frame);
        ungroupItem = new Ungroup(frame);
        deleteItem = new Delete(frame);
        clearAllItem = new ClearAll(frame);
        labelItem = new Label(frame);

        // 加入選單
        this.add(renameItem);
        this.add(groupItem);
        this.add(ungroupItem);
        this.add(deleteItem);
        this.add(clearAllItem);
        this.addSeparator();
        this.add(labelItem);
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;

        // 更新所有子選單項目
        renameItem.setCanvasPanel(canvasPanel);
        groupItem.setCanvasPanel(canvasPanel);
        ungroupItem.setCanvasPanel(canvasPanel);
        deleteItem.setCanvasPanel(canvasPanel);
        clearAllItem.setCanvasPanel(canvasPanel);
        labelItem.setCanvasPanel(canvasPanel);
    }
}