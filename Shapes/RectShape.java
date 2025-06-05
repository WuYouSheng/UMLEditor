package Shapes;

import java.awt.*;

/**
 * 矩形類
 * 繼承 BasicShape，實現具體的矩形繪製
 * 因為BasicShape中已經有contains 等可以直接套用的方法，所以不用override
 */
public class RectShape extends BasicShape {

    /**
     * 建構函數
     * @param start 起始點
     */
    public RectShape(Point start) {
        super(start);
        updatePorts();
    }

    /**
     * 繪製矩形
     * @param g2d 圖形上下文
     */
    @Override
    public void draw(Graphics2D g2d) {
        // 使用組合模式繪製矩形的各個部分
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
        g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * 繪製邊框
     * @param g2d 圖形上下文
     */
    private void drawBorder(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * 更新連接埠位置
     * 實現模板方法，定義矩形特有的連接埠配置
     */
    @Override
    public void updatePorts() {
        ports.clear();
        // 8個連接埠 - 矩形的四個角落和四條邊的中點
        ports.add(new Point(bounds.x, bounds.y)); // 左上
        ports.add(new Point(bounds.x + bounds.width / 2, bounds.y)); // 上中
        ports.add(new Point(bounds.x + bounds.width, bounds.y)); // 右上
        ports.add(new Point(bounds.x, bounds.y + bounds.height / 2)); // 左中
        ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2)); // 右中
        ports.add(new Point(bounds.x, bounds.y + bounds.height)); // 左下
        ports.add(new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height)); // 下中
        ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height)); // 右下
    }
}