package Interfaces;

/**
 * 可移動介面
 * 定義物件移動相關的行為
 */
public interface Movable {
    /**
     * 移動物件
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    void move(int dx, int dy);
}