package Shapes;

import java.awt.*;

/**
 * Generalization Link Class
 * 繼承 Link，實現Generalization Link的具體繪製邏輯
 */
public class GeneralizationLink extends Link {

    /**
     * 建構函數
     * @param start 起始點
     */
    public GeneralizationLink(Point start) {
        super(start);
    }

    /**
     * 繪製連結
     * @param g2d 圖形
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);

        // 使用父節點繪製連結
        drawLine(g2d);
        drawArrowHead(g2d);
    }

    /**
     * 繪製Generalization Link的箭頭
     * 實現Abstract的方法，定義泛化連結特有的空心三角形箭頭樣式
     * @param g2d 圖形上下文
     */
    @Override
    protected void drawArrowHead(Graphics2D g2d) {
        // 繪製空心三角形箭頭
        TriangleArrowRenderer renderer = new TriangleArrowRenderer();
        renderer.renderHollowTriangle(g2d, endPoint, startPoint, 10);
    }

    /**
     * 三角形箭頭渲染器
     * 專門處理Generalization Link的空心三角形箭頭
     */
    private static class TriangleArrowRenderer {

        /**
         * 渲染空心三角形箭頭
         * @param g2d 圖形上下文
         * @param tip 箭頭尖端
         * @param tail 箭頭尾部
         * @param size 箭頭大小
         */
        public void renderHollowTriangle(Graphics2D g2d, Point tip, Point tail, int size) {
            double dx = tip.x - tail.x;
            double dy = tip.y - tail.y;
            double length = Math.sqrt(dx * dx + dy * dy);

            if (length == 0) return;

            double unitDx = dx / length;
            double unitDy = dy / length;

            int x1 = (int) (tip.x - size * unitDx + size * unitDy / 2);
            int y1 = (int) (tip.y - size * unitDy - size * unitDx / 2);
            int x2 = (int) (tip.x - size * unitDx - size * unitDy / 2);
            int y2 = (int) (tip.y - size * unitDy + size * unitDx / 2);

            int[] xPoints = {tip.x, x1, x2};
            int[] yPoints = {tip.y, y1, y2};

            // 先填充白色背景，再繪製黑色邊框
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
    }
}