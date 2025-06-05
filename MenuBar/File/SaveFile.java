package MenuBar.File;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import Canvas.CanvasPanel;

/**
 * 儲存檔案選單項目類
 */
public class SaveFile extends JMenuItem {
    private CanvasPanel canvasPanel;
    private JFrame parentFrame;

    // 建構子
    public SaveFile(JFrame frame) {
        super("Save as PNG"); // 設定 JMenuItem 標題
        this.parentFrame = frame;

        // 加入事件監聽
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvasPanel != null) {
                    saveCanvasAsPNG();
                } else {
                    JOptionPane.showMessageDialog(frame, "無法儲存檔案");
                }
            }
        });
    }

    /**
     * 將畫布儲存為PNG檔案
     */
    private void saveCanvasAsPNG() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("儲存為PNG");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG 圖像檔", "png"));

        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                // 取得選擇的文件
                File file = fileChooser.getSelectedFile();

                // 確保檔案有.png副檔名
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }

                // 建立與畫布相同大小的緩衝圖像
                Component canvas = canvasPanel;
                BufferedImage image = new BufferedImage(
                        canvas.getWidth(),
                        canvas.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );

                // 繪製畫布內容到緩衝圖像
                Graphics2D g2d = image.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.paint(g2d);
                g2d.dispose();

                // 儲存圖像
                if (ImageIO.write(image, "png", file)) {
                    JOptionPane.showMessageDialog(parentFrame, "已成功儲存檔案：" + file.getName());
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "儲存失敗：無法寫入檔案格式");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "儲存時發生錯誤：" + ex.getMessage(),
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * 設定畫布面板
     */
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }
}