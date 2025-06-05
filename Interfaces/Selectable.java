
package Interfaces;

/**
 * 可選取介面
 * 定義物件選取相關的行為
 */
public interface Selectable {
    /**
     * 設定選取狀態
     * @param selected 是否被選取
     */
    void setSelected(boolean selected);

    /**
     * 取得選取狀態
     * @return 是否被選取
     */
    boolean isSelected();
}