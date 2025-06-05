package Shapes;

import java.awt.*;

/**
 * 選取矩形類
 * 用於框選多個物件
 * 使用策略模式處理不同的選取框樣式
 */
public class SelectionRectangle extends BaseShape {
    private Rectangle rectangle;
    private SelectionStyle selectionStyle;

    /**
     * 建構函數
     * @param start 起始點
     * @param end 結束點
     */
    public SelectionRectangle(Point start, Point end) {
        this.selectionStyle = new DefaultSelectionStyle();
        updateRectangle(start, end);
    }

    /**
     * 更新矩形位置和大小
     * @param start 起始點
     * @param end 結束點
     */
    private void updateRectangle(Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        rectangle = new Rectangle(x, y, width, height);
    }

    /**
     * 繪製選取矩形
     * @param g2d 圖形
     */
    @Override
    public void draw(Graphics2D g2d) {
        selectionStyle.drawSelection(g2d, rectangle);
    }

    /**
     * 檢查點是否在選取矩形內
     * @param p 要檢查的點
     * @return 點是否在選取矩形內
     */
    @Override
    public boolean contains(Point p) {
        return rectangle.contains(p);
    }

    /**
     * 移動選取矩形
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    @Override
    public void move(int dx, int dy) {
        rectangle.x += dx;
        rectangle.y += dy;
    }

    /**
     * 調整選取矩形大小
     * @param start 起始點
     * @param end 結束點
     */
    @Override
    public void resize(Point start, Point end) {
        updateRectangle(start, end);
    }

    /**
     * 取得選取矩形
     * @return 矩形物件
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * 選取樣式interface
     * 處理不同的選取框繪製方式
     */
    public interface SelectionStyle {
        /**
         * 繪製選取框
         * @param g2d 圖形
         * @param rect 選取矩形
         */
        void drawSelection(Graphics2D g2d, Rectangle rect);
    }

    /**
     * 預設選取樣式
     * 半透明藍色填充加虛線邊框
     */
    private static class DefaultSelectionStyle implements SelectionStyle {
        @Override
        public void drawSelection(Graphics2D g2d, Rectangle rect) {
            // 繪製半透明的藍色填充
            g2d.setColor(new Color(0, 0, 255, 30));
            g2d.fill(rect);

            // 繪製虛線邊框
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                    10.0f, new float[]{5.0f}, 0.0f));
            g2d.draw(rect);
            g2d.setStroke(new BasicStroke());
        }
    }

}