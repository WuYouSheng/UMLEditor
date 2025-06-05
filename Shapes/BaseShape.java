package Shapes;

import Interfaces.Shape;

/**
 * 基本形狀Abstract Class
 * 實現了 Shape 介面，提供所有形狀的基本功能
 * 使用模板方法模式定義形狀的基本行為
 */
public abstract class BaseShape implements Shape {
    protected boolean selected = false; // 是否被選中
    protected int depth = 0; // 深度值，用於繪製順序

    /**
     * 設定選取狀態
     * @param selected 是否被選取
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * 取得選取狀態
     * @return 是否被選取
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * 設定深度
     * @param depth 深度值
     */
    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * 取得深度
     * @return 深度值
     */
    @Override
    public int getDepth() {
        return depth;
    }
}