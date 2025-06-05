package Interfaces;

import java.awt.*;

/**
 * 可調整大小介面
 * 定義物件大小調整相關的行為
 */
public interface Resizable {
    /**
     * 調整物件大小
     * @param start 起始點
     * @param end 結束點
     */
    void resize(Point start, Point end);
}