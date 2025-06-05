package Interfaces;

import java.awt.*;

/**
 * 可包含檢測介面
 * 定義檢查點是否在物件內部的行為
 */
public interface Containable {
    /**
     * 檢查點是否在物件內
     * @param p 要檢查的點
     * @return 點是否在物件內
     */
    boolean contains(Point p);
}