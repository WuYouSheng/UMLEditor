package MenuBar.Edit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Canvas.CanvasPanel;

/**
 * 標籤設定選單項目類
 */
public class Label extends JMenuItem {
    private CanvasPanel canvasPanel;

    public Label(JFrame frame) {
        super("Label Style"); // 設定 JMenuItem 標題

        // 添加動作監聽器
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    // 建立自定義標籤樣式對話框
                    createLabelDialog(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "請先選取一個物件");
                }
            }
        });
    }

    /**
     * 建立標籤設定對話框
     */
    private void createLabelDialog(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Custom label Style", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 名稱
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Name"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(10);
        dialog.add(nameField, gbc);

        // 形狀
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Shape"), gbc);

        gbc.gridx = 1;
        String[] shapes = {"rect", "oval"};
        JComboBox<String> shapeCombo = new JComboBox<>(shapes);
        dialog.add(shapeCombo, gbc);

        // 顏色
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Color"), gbc);

        gbc.gridx = 1;
        String[] colors = {"yellow", "cyan", "pink", "light gray"};
        JComboBox<String> colorCombo = new JComboBox<>(colors);
        dialog.add(colorCombo, gbc);

        // 字體大小
        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("FontSize"), gbc);

        gbc.gridx = 1;
        Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20};
        JComboBox<Integer> fontSizeCombo = new JComboBox<>(fontSizes);
        fontSizeCombo.setSelectedItem(12); // 預設選擇12
        dialog.add(fontSizeCombo, gbc);

        // 按鈕
        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(cancelButton, gbc);

        gbc.gridx = 1;
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 取得所有設定值
                String name = nameField.getText();
                String shape = (String) shapeCombo.getSelectedItem();
                String colorName = (String) colorCombo.getSelectedItem();
                int fontSize = (Integer) fontSizeCombo.getSelectedItem();

                // 轉換顏色名稱為Color物件
                Color color = null;
                switch (colorName) {
                    case "yellow":
                        color = Color.YELLOW;
                        break;
                    case "cyan":
                        color = Color.CYAN;
                        break;
                    case "pink":
                        color = Color.PINK;
                        break;
                    default:
                        color = Color.LIGHT_GRAY;
                        break;
                }

                // 套用設定到選取的物件
                canvasPanel.customizeLabelStyle(name, shape, color, fontSize);
                dialog.dispose();
            }
        });
        dialog.add(okButton, gbc);

        // 設定對話框大小和位置
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }
}