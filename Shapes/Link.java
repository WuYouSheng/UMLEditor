package Shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

/**
 * 連結的Abstract Class
 * 用於連接兩個基本形狀
 * 處理包含連接形狀的變化
 * 處理不同的箭頭繪製方式
 */
public abstract class Link extends BaseShape {
    protected Point startPoint;
    protected Point endPoint;
    protected BasicShape startShape;
    protected BasicShape endShape;
    // 追踪起始形狀和終止形狀上的連接點索引
    protected int startPortIndex = -1;
    protected int endPortIndex = -1;
    int Distance_GAP = 5;

    // 處理距離計算
    protected DistanceCalculator distanceCalculator;

    /**
     * 建構函數
     * @param start 起始點
     */
    public Link(Point start) {
        this.startPoint = start;
        this.endPoint = start; // 初始時終點與起點相同
        this.distanceCalculator = new LineDistanceCalculator();
    }

    /**
     * 檢查點是否在連結線附近
     * @param p 要檢查的點
     * @return 點是否在連結線附近，附近的判斷依據是根據預先寫好的 Distance_GAP 做判斷
     */
    @Override
    public boolean contains(Point p) {
        double distance = distanceCalculator.calculateDistance(p, startPoint, endPoint);
        return distance < Distance_GAP;
    }

    /**
     * 移動連結線
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    @Override
    public void move(int dx, int dy) {
        // 更新連結的起點和終點
        startPoint.x += dx;
        startPoint.y += dy;
        endPoint.x += dx;
        endPoint.y += dy;
    }

    /**
     * 連結不支援直接調整大小
     * @param start 起始點
     * @param end 結束點
     */
    @Override
    public void resize(Point start, Point end) {
        // 連結不支援調整大小
    }

    // Getter 和 Setter 方法
    public void setStartPoint(Point start) {
        this.startPoint = start;
    }

    public void setEndPoint(Point end) {
        this.endPoint = end;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * 設定終點形狀
     * @param shape 終點形狀
     */
    public void setEndShape(BasicShape shape) {
         //驗證是否自己連到自己
        if (this.startShape != null && !LinkValidator.isValidLink(this.startShape, shape)) {
            throw new IllegalArgumentException("無效的連接，不能同一個 port 自己連到自己啦！！！");
        }
        this.endShape = shape;
        // 找到並記錄對應的連接點索引
        if (shape != null && endPoint != null) {
            findAndSetEndPortIndex();
        }
    }

    /**
     * 設定起點形狀
     * @param shape 起點形狀
     */
    public void setStartShape(BasicShape shape) {
        if (this.endShape != null && !LinkValidator.isValidLink(shape, this.endShape)) {
            throw new IllegalArgumentException("無效的連接，不能同一個 port 自己連到自己啦！！！");
        }

        this.startShape = shape;
        // 找到並記錄對應的連接點索引
        if (shape != null && startPoint != null) {
            findAndSetStartPortIndex();
        }
    }

    /**
     * 記錄起始連接點的索引
     */
    private void findAndSetStartPortIndex() {
        if (startShape != null && startPoint != null) {
            List<Point> ports = startShape.getPorts();
            for (int i = 0; i < ports.size(); i++) {
                Point port = ports.get(i);
                if (port.x == startPoint.x && port.y == startPoint.y) {
                    startPortIndex = i;
                    break;
                }
            }
        }
    }

    /**
     * 記錄終止連接點的索引
     */
    private void findAndSetEndPortIndex() {
        if (endShape != null && endPoint != null) {
            List<Point> ports = endShape.getPorts();
            for (int i = 0; i < ports.size(); i++) {
                Point port = ports.get(i);
                if (port.x == endPoint.x && port.y == endPoint.y) {
                    endPortIndex = i;
                    break;
                }
            }
        }
    }

    /**
     * 更新連結的位置
     * 當連接的形狀移動時，應該調用此方法更新連結的位置
     */
    public void updatePosition() {
        // 當形狀移動時，我們使用索引來找到相同的連接點
        if (startShape != null && startPortIndex >= 0 && startPortIndex < startShape.getPorts().size()) {
            startPoint = startShape.getPorts().get(startPortIndex);
        }

        if (endShape != null && endPortIndex >= 0 && endPortIndex < endShape.getPorts().size()) {
            endPoint = endShape.getPorts().get(endPortIndex);
        }
    }

    /**
     * 當形狀移動時，更新連接點位置
     * @param shape 正在移動的形狀
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    public void updateEndpointForShape(BaseShape shape, int dx, int dy) {
        // 使用記錄的連接點索引來更新端點位置
        if (startShape == shape) {
            updateStartPoint(dx, dy);
        }

        if (endShape == shape) {
            updateEndPoint(dx, dy);
        }
    }

    /**
     * 更新起始點
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    private void updateStartPoint(int dx, int dy) {
        // 如果還沒有記錄索引，則記錄它
        if (startPortIndex == -1) {
            findAndSetStartPortIndex();
        }

        // 使用索引獲取更新後的連接點位置
        if (startPortIndex >= 0 && startPortIndex < startShape.getPorts().size()) {
            startPoint = startShape.getPorts().get(startPortIndex);
        } else {
            // 如果找不到索引，則使用偏移
            startPoint.x += dx;
            startPoint.y += dy;
        }
    }

    /**
     * 更新終點
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    private void updateEndPoint(int dx, int dy) {
        // 如果還沒有記錄索引，則記錄它
        if (endPortIndex == -1) {
            findAndSetEndPortIndex();
        }

        // 使用索引獲取更新後的連接點位置
        if (endPortIndex >= 0 && endPortIndex < endShape.getPorts().size()) {
            endPoint = endShape.getPorts().get(endPortIndex);
        } else {
            // 如果找不到索引，則使用偏移
            endPoint.x += dx;
            endPoint.y += dy;
        }
    }

    /**
     * 檢查連結是否與指定形狀關聯
     * @param shape 要檢查的形狀
     * @return 是否關聯
     */
    public boolean isRelatedTo(BaseShape shape) {
        return (startShape == shape || endShape == shape);
    }

    /**
     * 繪製箭頭的抽象方法
     * 由子類實現具體的箭頭繪製邏輯
     * @param g2d 圖形上下文
     */
    protected abstract void drawArrowHead(Graphics2D g2d);

    /**
     * 繪製連結線主體
     * @param g2d 圖形上下文
     */
    protected void drawLine(Graphics2D g2d) {
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    /**
     * 繪製基本箭頭的輔助方法
     * @param g2d 圖形
     * @param tip 箭頭尖端
     * @param tail 箭頭尾部
     * @param size 箭頭大小
     */
    protected void drawArrow(Graphics2D g2d, Point tip, Point tail, int size) {
        ArrowRenderer renderer = new StandardArrowRenderer();
        renderer.renderArrow(g2d, tip, tail, size);
    }
    /**
     * 距離計算interface
     * 處理距離計算
     */
    private interface DistanceCalculator {
        double calculateDistance(Point p, Point start, Point end);
    }

    /**
     * 線段距離計算器
     */
    private static class LineDistanceCalculator implements DistanceCalculator {
        //向量內積計算
        @Override
        public double calculateDistance(Point p, Point start, Point end) {

            // 單一條線的長度，使用畢氏定理
            double normalLength = Math.sqrt((end.x - start.x) * (end.x - start.x) +
                    (end.y - start.y) * (end.y - start.y));
            if (normalLength == 0) return Double.MAX_VALUE;

            // 點到start end 這條直線的距離
            double distance = Math.abs((p.x - start.x) * (end.y - start.y) -
                    (p.y - start.y) * (end.x - start.x)) / normalLength;

            // 檢查點是否在線段的投影範圍內
            double dot = (p.x - start.x) * (end.x - start.x) + (p.y - start.y) * (end.y - start.y);
            if (dot < 0) {
                return Math.sqrt((p.x - start.x) * (p.x - start.x) + (p.y - start.y) * (p.y - start.y));
            }//在 start 的左邊
            double lengthSquared = normalLength * normalLength;
            if (dot > lengthSquared) {
                return Math.sqrt((p.x - end.x) * (p.x - end.x) + (p.y - end.y) * (p.y - end.y));
            }// 在end 的右邊

            return distance;
        }
    }

    /**
     * 箭頭渲染器interface
     * 使用策略模式處理不同的箭頭樣式
     */
    private interface ArrowRenderer {
        void renderArrow(Graphics2D g2d, Point tip, Point tail, int size);
    }

    /**
     * 標準箭頭渲染器實作
     */
    private static class StandardArrowRenderer implements ArrowRenderer {
        @Override
        public void renderArrow(Graphics2D g2d, Point tip, Point tail, int size) {
            double dx = tip.x - tail.x;
            double dy = tip.y - tail.y;
            double length = Math.sqrt(dx * dx + dy * dy);

            if (length == 0) return;

            double unitDx = dx / length;
            double unitDy = dy / length;

            // 箭頭兩邊的點
            int x1 = (int) (tip.x - size * unitDx + size * unitDy / 2);
            int y1 = (int) (tip.y - size * unitDy - size * unitDx / 2);
            int x2 = (int) (tip.x - size * unitDx - size * unitDy / 2);
            int y2 = (int) (tip.y - size * unitDy + size * unitDx / 2);

            int[] xPoints = {tip.x, x1, x2};
            int[] yPoints = {tip.y, y1, y2};

            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    /**
     * 連結驗證器
     * 驗證連結的有效性
     */
    protected static class LinkValidator {

        /**
         * 驗證連結是否有效
         * @param startShape 起始形狀
         * @param endShape 結束形狀
         * @return 是否有效
         */
        public static boolean isValidLink(BaseShape startShape, BaseShape endShape) {
            // 不能連接到自己
            if (startShape == endShape) {
                return false;
            }

            // 起始和結束形狀都不能為空
            if (startShape == null || endShape == null) {
                return false;
            }

            // 只能連接基本形狀
            if (!(startShape instanceof BasicShape) || !(endShape instanceof BasicShape)) {
                return false;
            }

            return true;
        }
    }
}