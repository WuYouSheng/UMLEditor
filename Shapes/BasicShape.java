package Shapes;

import Interfaces.Connectable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本形狀類
 * 繼承 BaseShape 並實現 Connectable 介面
 * 包含矩形和橢圓等基本形狀的共同功能
 * 使用策略模式處理標籤繪製
 */
public abstract class BasicShape extends BaseShape implements Connectable {
    protected Rectangle bounds; // 形狀的邊界
    protected String name = ""; // 形狀的名稱
    protected LabelRenderer labelRenderer; // 標籤渲染器
    protected List<Point> ports = new ArrayList<>(); // 連接埠列表

    /**
     * 建構函數
     * @param start 起始點
     */
    public BasicShape(Point start) {
        bounds = new Rectangle(start.x, start.y, 0, 0);
        labelRenderer = new RectangleLabelRenderer(); // 預設使用矩形標籤
    }

    /**
     * 調整形狀大小
     * @param start 起始點
     * @param end 結束點
     */
    @Override
    public void resize(Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        bounds = new Rectangle(x, y, width, height);
        updatePorts(); // 更新連接埠位置
    }

    /**
     * 移動形狀
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    @Override
    public void move(int dx, int dy) {
        bounds.x += dx;
        bounds.y += dy;
        updatePorts(); // 更新連接埠位置
    }

    /**
     * 檢查點是否在形狀內
     * @param p 要檢查的點
     * @return 點是否在形狀內
     */
    @Override
    public boolean contains(Point p) {
        return bounds.contains(p);
    }

    /**
     * 取得最近的連接埠
     * @param p 參考點
     * @return 最近的連接埠位置
     */
    @Override
    public Point getNearestPort(Point p) {
        if (ports.isEmpty()) return null;

        Point nearest = ports.get(0);
        double minDistance = calculateDistance(p, nearest);

        for (Point port : ports) {
            double dist = calculateDistance(p, port);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = port;
            }
        }

        return nearest;
    }

    /**
     * 計算兩點之間的距離
     * @param p1 第一個點
     * @param p2 第二個點
     * @return 距離
     */
    private double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    /**
     * 取得所有連接埠
     * @return 連接埠列表
     */
    @Override
    public List<Point> getPorts() {
        return ports;
    }

    // Getter 和 Setter 方法
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLabelShape(String shape) {
        if ("oval".equals(shape)) {
            labelRenderer = new OvalLabelRenderer();
        } else {
            labelRenderer = new RectangleLabelRenderer();
        }
    }

    public void setLabelColor(Color color) {
        labelRenderer.setColor(color);
    }

    public void setFontSize(int fontSize) {
        labelRenderer.setFontSize(fontSize);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * 繪製標籤
     * @param g2d
     */
    protected void drawLabel(Graphics2D g2d) {
        if (!name.isEmpty()) {
            labelRenderer.renderLabel(g2d, name, bounds);
        }
    }

    /**
     * 繪製連接埠
     * @param g2d
     */
    protected void drawPorts(Graphics2D g2d) {
        if (!selected) return;

        g2d.setColor(Color.BLACK);
        for (Point port : ports) {
            g2d.fillRect(port.x - 2, port.y - 2, 5, 5);
        }
    }

    /**
     * 標籤渲染器 interface
     * 使用策略模式處理不同的標籤樣式
     */
    private interface LabelRenderer {
        void renderLabel(Graphics2D g2d, String text, Rectangle bounds);
        void setColor(Color color);
        void setFontSize(int fontSize);
    }

    /**
     * 實作矩形標籤
     */
    private static class RectangleLabelRenderer implements LabelRenderer {
        private Color color = Color.LIGHT_GRAY; //標籤外筐顏色固定
        private int fontSize = 12;

        @Override
        public void renderLabel(Graphics2D g2d, String text, Rectangle bounds) {
            g2d.setFont(new Font("Arial", Font.PLAIN, fontSize));
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();

            int x = bounds.x + (bounds.width - textWidth) / 2;
            int y = bounds.y + (bounds.height - textHeight) / 2 + metrics.getAscent();

            g2d.setColor(color);
            g2d.fillRect(x - 5, y - metrics.getAscent(), textWidth + 10, textHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - 5, y - metrics.getAscent(), textWidth + 10, textHeight);
            g2d.drawString(text, x, y);
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }
    }

    /**
     * 橢圓標籤渲染器
     */
    private static class OvalLabelRenderer implements LabelRenderer {
        private Color color = Color.LIGHT_GRAY;
        private int fontSize = 12;

        @Override
        public void renderLabel(Graphics2D g2d, String text, Rectangle bounds) {
            g2d.setFont(new Font("Arial", Font.PLAIN, fontSize));
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();

            int x = bounds.x + (bounds.width - textWidth) / 2;
            int y = bounds.y + (bounds.height - textHeight) / 2 + metrics.getAscent();

            g2d.setColor(color);
            g2d.fillOval(x - 5, y - metrics.getAscent(), textWidth + 10, textHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - 5, y - metrics.getAscent(), textWidth + 10, textHeight);
            g2d.drawString(text, x, y);
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }
    }
}