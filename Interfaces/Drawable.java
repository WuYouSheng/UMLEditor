
package Interfaces;

import java.awt.*;

/**
 * 可繪製介面
 * 定義所有可在畫布上繪製的物件需要實現的方法
 */
public interface Drawable {
    /**
     * 繪製物件
     * @param g2d 圖形上下文
     */
    void draw(Graphics2D g2d);
}