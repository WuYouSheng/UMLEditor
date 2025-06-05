package Shapes;

import java.awt.*;

/**
 * 組合連結類
 * 繼承 Link，實現組合連結的具體繪製邏輯
 * 使用模板方法模式實現特定的實心菱形箭頭樣式
 */
public class CompositionLink extends Link {

    /**
     * 建構函數
     * @param start 起始點
     */
    public CompositionLink(Point start) {
        super(start);
    }

    /**
     * 繪製組合連結
     * @param g2d 圖形上下文
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);

        // 使用父類別方法繪製連結
        drawLine(g2d);
        drawArrowHead(g2d);
    }

    /**
     * 繪製組合連結的箭頭
     * 實現抽象方法，定義組合連結特有的實心菱形箭頭樣式
     * @param g2d 圖形上下文
     */
    @Override
    protected void drawArrowHead(Graphics2D g2d) {
        // 繪製實心菱形箭頭
        DiamondArrowRenderer renderer = new DiamondArrowRenderer();
        renderer.renderSolidDiamond(g2d, endPoint, startPoint, 10);
    }

    /**
     * 菱形箭頭渲染器
     * 專門處理組合連結的實心菱形箭頭
     */
    private static class DiamondArrowRenderer {

        /**
         * 渲染實心菱形箭頭
         * @param g2d 圖形
         * @param tip 箭頭尖端
         * @param tail 箭頭尾部
         * @param size 箭頭大小
         */
        public void renderSolidDiamond(Graphics2D g2d, Point tip, Point tail, int size) {
            double dx = tip.x - tail.x;
            double dy = tip.y - tail.y;
            double length = Math.sqrt(dx * dx + dy * dy);

            if (length == 0) return;

            double unitDx = dx / length;
            double unitDy = dy / length;

            // 計算菱形的四個頂點
            int x1 = (int) (tip.x - size * unitDx + size * unitDy / 2);
            int y1 = (int) (tip.y - size * unitDy - size * unitDx / 2);
            int x2 = (int) (tip.x - size * 2 * unitDx);
            int y2 = (int) (tip.y - size * 2 * unitDy);
            int x3 = (int) (tip.x - size * unitDx - size * unitDy / 2);
            int y3 = (int) (tip.y - size * unitDy + size * unitDx / 2);

            int[] xPoints = {tip.x, x1, x2, x3};
            int[] yPoints = {tip.y, y1, y2, y3};

            // 繪製實心菱形
            g2d.fillPolygon(xPoints, yPoints, 4);
        }
    }
}