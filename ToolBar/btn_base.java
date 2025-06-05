package ToolBar;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import Canvas.CanvasPanel;

/**
 * 工具列基礎按鈕類別
 * 所有工具列按鈕的父類別
 */
public class btn_base extends JToggleButton {
    protected CanvasPanel canvasPanel;

    // 深色系按鈕顏色
    private static final Color BUTTON_SELECTED_BG = new Color(80, 80, 80);
    private static final Color BUTTON_ROLLOVER_BG = new Color(70, 70, 70);

    /**
     * 建構函數，使用指定的圖示路徑
     * @param actionImagePath 圖示檔案的資源路徑
     */
    public btn_base(String actionImagePath) {
        try {
            // 嘗試載入圖示
            URL imageURL = getClass().getResource(actionImagePath);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                this.setIcon(icon);
                //System.out.println("成功載入圖示: " + actionImagePath);
            } else {
                System.out.println("無法找到圖示: " + actionImagePath);
            }
        } catch (Exception e) {
            System.out.println("載入圖示時出錯: " + e.getMessage());
        }

        setupButtonAppearance();
    }

    /**
     * 無參數建構函數
     */
    public btn_base() {
        setupButtonAppearance();
    }

    /**
     * 設定按鈕外觀
     */
    private void setupButtonAppearance() {
        // 設定按鈕屬性
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setPreferredSize(new Dimension(50, 50)); // 設定按鈕大小
        this.setFocusPainted(false);
        this.setBorderPainted(false);

        // 設定為不透明，以便繪製背景
        this.setOpaque(false);
        this.setContentAreaFilled(false);

        // 覆寫UI設定，使按鈕在深色背景上看起來更好
        this.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                if (b.isSelected()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(BUTTON_SELECTED_BG);
                    g2d.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 8, 8);
                    g2d.dispose();
                }
            }
        });

        // 添加滑鼠效果
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isSelected()) {
                    setContentAreaFilled(true);
                    setBackground(BUTTON_ROLLOVER_BG);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isSelected()) {
                    setContentAreaFilled(false);
                }
            }
        });
    }

    /**
     * 覆寫繪製方法，為選取狀態添加視覺效果
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 繪製選取狀態的背景
        if (isSelected()) {
            g2d.setColor(BUTTON_SELECTED_BG);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }

        g2d.dispose();
        super.paintComponent(g);
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }
}