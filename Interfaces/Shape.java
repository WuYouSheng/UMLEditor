package Interfaces;

/**
 * 形狀介面
 * 定義所有形狀物件需要實現的基本行為
 * 整合了多個基礎介面
 */
public interface Shape extends Drawable, Selectable, Movable, Containable, Resizable {
    /**
     * 設定深度
     * @param depth 深度值
     */
    void setDepth(int depth);

    /**
     * 取得深度
     * @return 深度值
     */
    int getDepth();
}