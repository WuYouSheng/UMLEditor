package Interfaces;

import java.awt.*;
import java.util.List;

/**
 * 可連接介面
 * 定義可以被連結線連接的物件行為
 */
public interface Connectable {
    /**
     * 取得最近的連接埠
     * @param p 參考點
     * @return 最近的連接埠位置
     */
    Point getNearestPort(Point p);

    /**
     * 取得所有連接埠
     * @return 連接埠列表
     */
    List<Point> getPorts();

    /**
     * 更新連接埠位置
     */
    void updatePorts();
}