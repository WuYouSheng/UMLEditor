package Shapes;

import java.awt.*;

/**
 * Association Link Class
 * 繼承 Link，實現關聯連結的具體繪製邏輯
 */
public class AssociationLink extends Link {

    /**
     * 建構函數
     * @param start 起始點
     */
    public AssociationLink(Point start) {
        super(start);
    }

    /**
     * 繪製Association Link Class
     * @param g2d 圖形上下文
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);

        // 使用模板方法繪製連結
        drawLine(g2d);
        drawArrowHead(g2d);
    }

    /**
     * 繪製Association Link Class的箭頭
     * 實現抽象方法，定義關聯連結特有的箭頭樣式
     * @param g2d 圖形上下文
     */
    @Override
    protected void drawArrowHead(Graphics2D g2d) {
        // 繪製簡單的實心三角形箭頭
        drawArrow(g2d, endPoint, startPoint, 10);
    }
}