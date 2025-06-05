package Shapes;

import Interfaces.Shape;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 複合形狀類
 * 使用組合模式管理多個形狀
 * 實現了 Shape interface，可以像單一形狀一樣被操作
 */
public class CompositeShape extends BaseShape {
    private List<BaseShape> shapes = new ArrayList<>();
    private Rectangle bounds = new Rectangle();
    private BoundsCalculator boundsCalculator;
    private int GAP_Value = 5;

    /**
     * 建構函數
     */
    public CompositeShape() {
        this.boundsCalculator = new StandardBoundsCalculator();
    }

    /**
     * 繪製複合形狀
     * @param g2d 圖形
     */
    @Override
    public void draw(Graphics2D g2d) {
        // 繪製所有子形狀
        for (BaseShape shape : shapes) {
            shape.draw(g2d);
        }

        // 如果被選取，繪製群組邊界框
        if (selected) {
            drawSelectionBorder(g2d);
        }
    }

    /**
     * 繪製選取邊界框
     * @param g2d 圖形
     */
    private void drawSelectionBorder(Graphics2D g2d) {
        SelectionBorderRenderer renderer = new SelectionBorderRenderer();
        renderer.renderBorder(g2d, bounds);
    }

    /**
     * 檢查點是否在複合形狀內
     * @param p 要檢查的點
     * @return 點是否在複合形狀內
     */
    @Override
    public boolean contains(Point p) {
        // 首先檢查是否在整個群組的邊界內
        if (!bounds.contains(p)) {
            return false;
        }

        // 然後檢查是否在任何子形狀內
        for (BaseShape shape : shapes) {
            if (shape.contains(p)) {
                return true;
            }
        }

        // 如果不在任何子形狀內，但在群組邊界內，
        // 檢查是否點擊在群組邊界的邊框上（用於選取群組本身）
        return isOnGroupBorder(p);
    }

    /**
     * 檢查點是否在群組邊框上
     * 用於選取整個群組
     */
    private boolean isOnGroupBorder(Point p) {
        int borderWidth = GAP_Value; // 邊框寬度容差

        // 檢查是否在上下邊框附近
        if ((Math.abs(p.y - bounds.y) <= borderWidth ||
                Math.abs(p.y - (bounds.y + bounds.height)) <= borderWidth) &&
                p.x >= bounds.x - borderWidth && p.x <= bounds.x + bounds.width + borderWidth) {
            return true;
        }

        // 檢查是否在左右邊框附近
        if ((Math.abs(p.x - bounds.x) <= borderWidth ||
                Math.abs(p.x - (bounds.x + bounds.width)) <= borderWidth) &&
                p.y >= bounds.y - borderWidth && p.y <= bounds.y + bounds.height + borderWidth) {
            return true;
        }

        return false;
    }

    /**
     * 移動複合形狀
     * @param dx x軸位移量
     * @param dy y軸位移量
     */
    @Override
    public void move(int dx, int dy) {
        // 移動所有子形狀，包括連結
        for (BaseShape shape : shapes) {
            shape.move(dx, dy);
        }

        // 更新連結線位置，確保兩個形狀之間的連結線跟著移動
        updateChildLinks();

        // 更新邊界
        bounds.x += dx;
        bounds.y += dy;
    }

    /**
     * 更新子連結
     */
    private void updateChildLinks() {
        for (BaseShape shape : shapes) {
            if (shape instanceof Link) {
                Link link = (Link) shape;
                link.updatePosition();
            }
        }
    }

    /**
     * 複合形狀不支援直接調整大小
     * @param start 起始點
     * @param end 結束點
     */
    @Override
    public void resize(Point start, Point end) {
        // 複合形狀不支援直接調整大小
    }

    /**
     * 添加形狀到群組
     * @param shape 要添加的形狀
     */
    public void addShape(BaseShape shape) {
        if (shape != this && !shapes.contains(shape)) { // 防止自我包含和重複添加
            shapes.add(shape);
            updateBounds();
        }
    }

    /**
     * 取得群組中的所有形狀
     * @return 形狀列表的副本
     */
    public List<BaseShape> getShapes() {
        return new ArrayList<>(shapes);
    }


    /**
     * 獲取形狀數量
     * @return 形狀數量
     */
    public int getShapeCount() {
        return shapes.size();
    }

    /**
     * 更新邊界
     */
    private void updateBounds() {
        bounds = boundsCalculator.calculateBounds(shapes);
    }

    /**
     * 取得邊界
     * @return 邊界矩形
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * 設定選取狀態（同時設定所有子形狀）
     * @param selected 是否被選取
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        // 同時設定所有子形狀的選取狀態
        for (BaseShape shape : shapes) {
            shape.setSelected(selected);
        }
    }

    /**
     * 檢查群組是否與特定連結相關，用於刪除群組時同時刪除相關連結
     * @param link 要檢查的連結
     * @return 是否相關
     */
    public boolean isRelatedToLink(Link link) {
        RelationshipChecker checker = new RelationshipChecker();
        return checker.isGroupRelatedToLink(this, link);
    }

    /**
     * 邊界計算器interface
     * 處理不同的邊界計算方式
     */
    private interface BoundsCalculator {
        Rectangle calculateBounds(List<BaseShape> shapes);
    }

    /**
     * 計算Group後的區域最小矩形
     */
    private static class StandardBoundsCalculator implements BoundsCalculator {
        @Override
        public Rectangle calculateBounds(List<BaseShape> shapes) {
            if (shapes.isEmpty()) {
                return new Rectangle();
            }//回傳空白矩形

            // 找出包含所有形狀的最小矩形
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            for (BaseShape shape : shapes) {
                Rectangle shapeBounds = getShapeBounds(shape);

                minX = Math.min(minX, shapeBounds.x);
                minY = Math.min(minY, shapeBounds.y);
                maxX = Math.max(maxX, shapeBounds.x + shapeBounds.width);
                maxY = Math.max(maxY, shapeBounds.y + shapeBounds.height);
            }

            return new Rectangle(minX, minY, maxX - minX, maxY - minY);
        }

        private Rectangle getShapeBounds(BaseShape shape) {
            if (shape instanceof BasicShape) {
                return ((BasicShape) shape).getBounds();
            } else if (shape instanceof Link) {
                // 對於連結，取其起點和終點的邊界
                Link link = (Link) shape;
                Point startPoint = link.getStartPoint();
                Point endPoint = link.getEndPoint();
                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(endPoint.x - startPoint.x);
                int height = Math.abs(endPoint.y - startPoint.y);
                return new Rectangle(x, y, width, height);
            } else if (shape instanceof CompositeShape) {
                // 遞迴處理複合形狀
                return ((CompositeShape) shape).getBounds();
            } else {
                // 對於其他類型，使用一個預設的小矩形
                return new Rectangle(0, 0, 10, 10);
            }
        }
    }

    /**
     * 選取邊界渲染器
     */
    private static class SelectionBorderRenderer {
        public void renderBorder(Graphics2D g2d, Rectangle bounds) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                    10.0f, new float[]{5.0f}, 0.0f));
            g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // 繪製選取點
            renderSelectionHandles(g2d, bounds);

            g2d.setStroke(new BasicStroke());
        }

        private void renderSelectionHandles(Graphics2D g2d, Rectangle bounds) {
            g2d.fillRect(bounds.x - 3, bounds.y - 3, 6, 6); // 左上
            g2d.fillRect(bounds.x + bounds.width - 3, bounds.y - 3, 6, 6); // 右上
            g2d.fillRect(bounds.x - 3, bounds.y + bounds.height - 3, 6, 6); // 左下
            g2d.fillRect(bounds.x + bounds.width - 3, bounds.y + bounds.height - 3, 6, 6); // 右下
        }
    }

    /**
     * Shape 是否與 Link 有關聯，避免Shape移動 Link還在原位
     */
    private static class RelationshipChecker {
        public boolean isGroupRelatedToLink(CompositeShape group, Link link) {
            for (BaseShape shape : group.shapes) {
                if (shape instanceof BasicShape) {
                    if (link.isRelatedTo(shape)) {
                        return true;
                    }
                } else if (shape instanceof CompositeShape) {
                    // 遞迴檢查子群組
                    CompositeShape cs = (CompositeShape) shape;
                    if (cs.isRelatedToLink(link)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}