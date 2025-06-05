package Shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * 橢圓類
 * 繼承 BasicShape，實現具體的橢圓繪製邏輯
 * 使用模板方法模式實現具體的繪製行為
 */
public class OvalShape extends BasicShape {
    protected Ellipse2D.Double ellipse;

    /**
     * 建構函數
     * @param start 起始點
     */
    public OvalShape(Point start) {
        super(start);
        ellipse = new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
        updatePorts();
    }

    /**
     * 繪製橢圓
     * @param g2d 圖形
     */
    @Override
    public void draw(Graphics2D g2d) {
        // 使用組合模式繪製橢圓的各個部分
        drawBackground(g2d);
        drawBorder(g2d);
        drawLabel(g2d);
        drawPorts(g2d);
    }

    /**
     * 繪製背景
     * @param g2d 圖形上下文
     */
    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(ellipse);
    }

    /**
     * 繪製邊框
     * @param g2d 圖形上下文
     */
    private void drawBorder(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.draw(ellipse);
    }

    /**
     * 調整大小
     * @param start 起始點
     * @param end 結束點
     */
    @Override
    public void resize(Point start, Point end) {
        super.resize(start, end);
        updateEllipseGeometry();
    }

    /**
     * 移動橢圓
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        updateEllipseGeometry();
    }

    /**
     * 檢查點是否在橢圓內
     * @param p 要檢查的點
     * @return 點是否在橢圓內
     */
    @Override
    public boolean contains(Point p) {
        return ellipse.contains(p);
    }

    /**
     * 更新橢圓幾何形狀
     */
    private void updateEllipseGeometry() {
        ellipse = new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * 更新連接埠位置
     * 實現模板方法，定義橢圓特有的連接埠配置
     */
    @Override
    public void updatePorts() {
        ports.clear();
        // 4個連接埠 - 橢圓的上下左右中點
        ports.add(new Point(bounds.x + bounds.width / 2, bounds.y)); // 上
        ports.add(new Point(bounds.x, bounds.y + bounds.height / 2)); // 左
        ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2)); // 右
        ports.add(new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height)); // 下
    }
}