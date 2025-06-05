package ToolBar;

import javax.swing.*;
import java.awt.*;
import Canvas.CanvasPanel;

/**
 * 工具列類別 - 使用深色背景
 * 包含各種繪圖工具按鈕
 */
public class ToolBar extends JToolBar {
    private CanvasPanel canvasPanel;
    private ButtonGroup buttonGroup; // 按鈕群組

    // 按鈕成員變數
    private btnSelect selectBtn;
    private btnAssociation associationBtn;
    private btnGeneralization generalizationBtn;
    private btnComposition compositionBtn;
    private btnRect rectBtn;
    private btnOval ovalBtn;

    // 深色系顏色
    private static final Color TOOLBAR_BG_COLOR = new Color(50, 50, 50); // 深灰色
    private static final Color SEPARATOR_COLOR = new Color(100, 100, 100); // 中灰色

    public ToolBar() {
        // 設定工具列屬性
        setOrientation(SwingConstants.VERTICAL);
        setFloatable(false);
        setBackground(TOOLBAR_BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 建立按鈕群組
        buttonGroup = new ButtonGroup();

        // 建立並添加所有按鈕
        createButtons();
    }

    /**
     * 建立所有工具按鈕
     */
    private void createButtons() {
        // 建立各種按鈕
        selectBtn = new btnSelect();
        associationBtn = new btnAssociation();
        generalizationBtn = new btnGeneralization();
        compositionBtn = new btnComposition();
        rectBtn = new btnRect();
        ovalBtn = new btnOval();

        // 設定按鈕外觀
        configureButtonAppearance(selectBtn);
        configureButtonAppearance(associationBtn);
        configureButtonAppearance(generalizationBtn);
        configureButtonAppearance(compositionBtn);
        configureButtonAppearance(rectBtn);
        configureButtonAppearance(ovalBtn);

        // 添加到按鈕群組
        buttonGroup.add(selectBtn);
        buttonGroup.add(associationBtn);
        buttonGroup.add(generalizationBtn);
        buttonGroup.add(compositionBtn);
        buttonGroup.add(rectBtn);
        buttonGroup.add(ovalBtn);

        // 添加按鈕到工具列，並添加間隔
        add(selectBtn);
        addCustomSeparator();
        add(associationBtn);
        add(generalizationBtn);
        add(compositionBtn);
        addCustomSeparator();
        add(rectBtn);
        add(ovalBtn);

        // 預設選取選取工具
        selectBtn.setSelected(true);
    }

    /**
     * 設定按鈕外觀
     */
    private void configureButtonAppearance(JToggleButton button) {
        // 設定按鈕背景色為透明
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(null);

        // 設定按鈕外觀
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // 設定選取時的外觀
        button.setMargin(new Insets(3, 3, 3, 3));
    }

    /**
     * 添加自定義分隔符
     */
    private void addCustomSeparator() {
        JPanel separator = new JPanel();
        separator.setBackground(SEPARATOR_COLOR);
        separator.setPreferredSize(new Dimension(40, 1));
        separator.setMaximumSize(new Dimension(40, 1));
        add(separator);
        add(Box.createVerticalStrut(5)); // 添加一點空間
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;

        // 設定所有按鈕的畫布引用
        selectBtn.setCanvasPanel(canvasPanel);
        associationBtn.setCanvasPanel(canvasPanel);
        generalizationBtn.setCanvasPanel(canvasPanel);
        compositionBtn.setCanvasPanel(canvasPanel);
        rectBtn.setCanvasPanel(canvasPanel);
        ovalBtn.setCanvasPanel(canvasPanel);
    }
}