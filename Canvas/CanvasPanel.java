package Canvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import Shapes.*;

/**
 * 畫布面板類別
 * 使用命令模式處理各種操作
 * 使用觀察者模式處理形狀變化
 * 使用狀態模式管理不同的操作模式
 */
public class CanvasPanel extends JPanel {
    public enum Mode {
        SELECT, ASSOCIATION, GENERALIZATION, COMPOSITION, RECT, OVAL
    }

    private Mode currentMode = Mode.SELECT; // 當前模式
    private List<BaseShape> shapes = new ArrayList<>(); // 儲存所有形狀
    private List<BaseShape> selectedShapes = new ArrayList<>(); // 儲存被選取的形狀
    private Point startPoint; // 拖曳起始點
    private Point endPoint;// 拖曳的目的點
    private BaseShape currentShape; // 當前操作的形狀
    private ShapeFactory shapeFactory = new ShapeFactory(); // 形狀工廠
    private int nextDepth = 0; // 下一個形狀的深度值
    private int DeltaX = 0;
    private int DeltaY = 0;

    // 使用策略模式處理不同模式的操作
    private ModeHandler modeHandler;
    private SelectionManager selectionManager;
    private ShapeManager shapeManager;

    /**
     * 建構函數
     */
    public CanvasPanel() {
        initializePanel();
        initializeComponents();
        setupEventListeners();
    }

    /**
     * 初始化面板
     */
    private void initializePanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * 初始化組件
     */
    private void initializeComponents() {
        shapeFactory = new ShapeFactory();
        modeHandler = new ConcreteModeHandler();
        selectionManager = new ConcreteSelectionManager();
        shapeManager = new ConcreteShapeManager();
    }

    /**
     * 設置事件監聽器
     */
    private void setupEventListeners() {
        // 滑鼠事件監聽
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        // 滑鼠拖曳
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    /**
     * 處理滑鼠拖曳事件
     */
    private void handleMouseDragged(MouseEvent e) {
        Point currentPoint = e.getPoint();
        modeHandler.handleDrag(currentMode, currentPoint, this);
        repaint();
    }

    /**
     * 處理滑鼠按下事件
     */
    private void handleMousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        modeHandler.handlePress(currentMode, startPoint, this);
        repaint();
    }

    /**
     * 處理滑鼠釋放事件
     */
    private void handleMouseReleased(MouseEvent e) {
        Point endPoint = e.getPoint();
        modeHandler.handleRelease(currentMode, endPoint, this);
        currentShape = null;
        repaint();
    }

    /**
     * 處理形狀移動時，更新所有相關連結的位置
     */
    public void updateLinksForShape(BaseShape shape) {
        LinkUpdater updater = new LinkUpdater();
        updater.updateLinksForShape(shape, shapes, DeltaX, DeltaY);
    }

    /**
     * 設定位移量
     */
    public void settingDeltaX_DeltaY(Point currentPoint){
        DeltaX = currentPoint.x - startPoint.x;
        DeltaY = currentPoint.y - startPoint.y;
    }

    /**
     * 設定當前模式
     */
    public void setMode(Mode mode) {
        this.currentMode = mode;
        clearSelection();
    }

    /**
     * 清除所有選取狀態
     */
    public void clearSelection() {
        selectionManager.clearSelection(selectedShapes);
        repaint();
    }

    /**
     * 清除畫布上的所有物件
     */
    public void clearAll() {
        shapes.clear();
        selectedShapes.clear();
        nextDepth = 0;
        repaint();
    }

    /**
     * 群組選取的物件 - 修正版本
     * 支援將現有群組與其他物件重新組成新群組
     */
    public void groupSelectedShapes() {
        if (selectedShapes.size() > 1) {
            CompositeShape group = shapeManager.createGroup(selectedShapes, shapes, nextDepth++);
            if (group != null) {
                shapes.add(group);
                selectedShapes.clear();
                selectedShapes.add(group);
                group.setSelected(true);
                repaint();
            }
        }
    }

    /**
     * 解除群組 - 修正版本
     * 提供選擇是否深度解除群組
     */
    public void ungroupSelectedShape() {
        ungroupSelectedShape(false); // 預設為淺層解除群組
    }

    /**
     * 解除群組
     * @param deepUngroup 是否深度解除群組（遞迴展開所有巢狀群組）
     */
    public void ungroupSelectedShape(boolean deepUngroup) {
        if (selectedShapes.size() == 1 && selectedShapes.get(0) instanceof CompositeShape) {
            CompositeShape group = (CompositeShape) selectedShapes.get(0);

            if (deepUngroup && shapeManager instanceof ConcreteShapeManager) {
                ((ConcreteShapeManager) shapeManager).deepUngroupShape(group, shapes, selectedShapes);
            } else {
                shapeManager.ungroupShape(group, shapes, selectedShapes);
            }

            repaint();
        }
    }

    /**
     * 檢查選取的形狀是否可以組成群組
     */
    public boolean canCreateGroup() {
        return selectedShapes.size() > 1;
    }

    /**
     * 檢查選取的形狀是否可以解除群組
     */
    public boolean canUngroup() {
        return selectedShapes.size() == 1 && selectedShapes.get(0) instanceof CompositeShape;
    }

    /**
     * 強制重新計算所有群組的邊界
     * 在群組操作後調用以確保邊界正確
     */
    public void recalculateGroupBounds() {
        for (BaseShape shape : shapes) {
            if (shape instanceof CompositeShape) {
                CompositeShape group = (CompositeShape) shape;
                // 觸發邊界重新計算
                group.move(0, 0); // 移動0距離會觸發updateBounds
            }
        }
        repaint();
    }

    /**
     * 取得目前選取形狀的類型資訊
     * 用於除錯和狀態顯示
     */
    public String getSelectionInfo() {
        if (selectedShapes.isEmpty()) {
            return "沒有選取任何物件";
        }

        StringBuilder info = new StringBuilder();
        info.append("選取了 ").append(selectedShapes.size()).append(" 個物件: ");

        for (int i = 0; i < selectedShapes.size(); i++) {
            BaseShape shape = selectedShapes.get(i);
            if (i > 0) info.append(", ");

            if (shape instanceof CompositeShape) {
                CompositeShape group = (CompositeShape) shape;
                info.append("群組(").append(group.getShapeCount()).append("個子物件)");
            } else if (shape instanceof BasicShape) {
                BasicShape basic = (BasicShape) shape;
                String name = basic.getName();
                if (name.isEmpty()) {
                    info.append(shape.getClass().getSimpleName());
                } else {
                    info.append(name);
                }
            } else {
                info.append(shape.getClass().getSimpleName());
            }
        }

        return info.toString();
    }

    /**
     * 刪除選取的形狀
     */
    public void deleteSelectedShapes() {
        ShapeDeleter deleter = new ShapeDeleter();
        deleter.deleteShapes(selectedShapes, shapes);
        selectedShapes.clear();
        repaint();
    }

    /**
     * 重命名選取的形狀
     */
    public void renameSelectedShape(String name) {
        if (!selectedShapes.isEmpty()) {
            BaseShape shape = selectedShapes.get(0);
            if (shape instanceof BasicShape) {
                ((BasicShape) shape).setName(name);
                repaint();
            }
        }
    }

    /**
     * 自定義標籤樣式
     */
    public void customizeLabelStyle(String name, String shape, Color color, int fontSize) {
        if (selectedShapes.size() == 1) {
            BaseShape baseShape = selectedShapes.get(0);
            if (baseShape instanceof BasicShape) {
                BasicShape shape1 = (BasicShape) baseShape;
                shape1.setName(name);
                shape1.setLabelShape(shape);
                shape1.setLabelColor(color);
                shape1.setFontSize(fontSize);
                repaint();
            }
        }
    }

    /**
     * 檢查是否有選取的形狀
     */
    public boolean hasSelectedShapes() {
        return !selectedShapes.isEmpty();
    }

    /**
     * 取得選取形狀的名稱
     */
    public String getSelectedShapeName() {
        if (!selectedShapes.isEmpty()) {
            BaseShape shape = selectedShapes.get(0);
            if (shape instanceof BasicShape) {
                return ((BasicShape) shape).getName();
            }
        }
        return "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 繪製所有形狀
        for (BaseShape shape : shapes) {
            shape.draw(g2d);
        }

        // 繪製當前正在操作的形狀（如選取框）
        if (currentShape != null) {
            currentShape.draw(g2d);
        }
    }

    // Getter 方法供內部類使用
    public List<BaseShape> getShapes() { return shapes; }
    public List<BaseShape> getSelectedShapes() { return selectedShapes; }
    public Point getStartPoint() { return startPoint; }
    public void setStartPoint(Point point) { this.startPoint = point; }
    public BaseShape getCurrentShape() { return currentShape; }
    public void setCurrentShape(BaseShape shape) { this.currentShape = shape; }
    public ShapeFactory getShapeFactory() { return shapeFactory; }
    public int getNextDepth() { return nextDepth++; }
    public int getDeltaX() { return DeltaX; }
    public int getDeltaY() { return DeltaY; }
    /**
     * 模式處理器介面
     * 使用策略模式處理不同模式的操作
     */
    private interface ModeHandler {
        void handlePress(Mode mode, Point point, CanvasPanel canvas);
        void handleDrag(Mode mode, Point point, CanvasPanel canvas);
        void handleRelease(Mode mode, Point point, CanvasPanel canvas);
    }

    /**
     * 具體模式處理器
     */
    private static class ConcreteModeHandler implements ModeHandler {
        private SelectModeHandler selectHandler = new SelectModeHandler();
        private ShapeModeHandler shapeHandler = new ShapeModeHandler();
        private LinkModeHandler linkHandler = new LinkModeHandler();

        @Override
        public void handlePress(Mode mode, Point point, CanvasPanel canvas) {
            switch (mode) {
                case SELECT:
                    selectHandler.handlePress(point, canvas);
                    break;
                case RECT:
                case OVAL:
                    shapeHandler.handlePress(mode, point, canvas);
                    break;
                case ASSOCIATION:
                case GENERALIZATION:
                case COMPOSITION:
                    linkHandler.handlePress(mode, point, canvas);
                    break;
            }
        }

        @Override
        public void handleDrag(Mode mode, Point point, CanvasPanel canvas) {
            switch (mode) {
                case SELECT:
                    selectHandler.handleDrag(point, canvas);
                    break;
                case RECT:
                case OVAL:
                    shapeHandler.handleDrag(point, canvas);
                    break;
                case ASSOCIATION:
                case GENERALIZATION:
                case COMPOSITION:
                    linkHandler.handleDrag(point, canvas);
                    break;
            }
        }

        @Override
        public void handleRelease(Mode mode, Point point, CanvasPanel canvas) {
            switch (mode) {
                case SELECT:
                    selectHandler.handleRelease(point, canvas);
                    break;
                case ASSOCIATION:
                case GENERALIZATION:
                case COMPOSITION:
                    linkHandler.handleRelease(mode, point, canvas);
                    break;
            }
        }
    }

    /**
     * 選取模式處理器
     * 支援群組物件的框選和重新群組
     */
    private static class SelectModeHandler {
        public void handlePress(Point point, CanvasPanel canvas) {
            boolean found = false;
            canvas.selectionManager.clearSelection(canvas.selectedShapes);

            // 根據深度從上到下找到點擊的物件
            for (int i = canvas.shapes.size() - 1; i >= 0; i--) {
                BaseShape shape = canvas.shapes.get(i);
                if (shape.contains(point)) {
                    canvas.selectedShapes.add(shape);
                    shape.setSelected(true);
                    found = true;
                    break;
                }
            }
        }

        public void handleDrag(Point point, CanvasPanel canvas) {
            if (!canvas.selectedShapes.isEmpty()) {
                canvas.settingDeltaX_DeltaY(point);

                // 移動選取的物件
                for (BaseShape shape : canvas.selectedShapes) {
                    shape.move(canvas.DeltaX, canvas.DeltaY);
                }

                // 更新所有相關連結
                for (BaseShape shape : canvas.selectedShapes) {
                    canvas.updateLinksForShape(shape);
                }

                canvas.startPoint = point;
            } else {
                // 產生選取框
                if (canvas.currentShape == null || !(canvas.currentShape instanceof SelectionRectangle)) {
                    canvas.currentShape = new SelectionRectangle(canvas.startPoint, point);
                } else {
                    ((SelectionRectangle) canvas.currentShape).resize(canvas.startPoint, point);
                }
            }
        }

        public void handleRelease(Point point, CanvasPanel canvas) {
            if (canvas.currentShape instanceof SelectionRectangle) {
                Rectangle selectionRect = ((SelectionRectangle) canvas.currentShape).getRectangle();

                // 修正：使用改進的框選邏輯
                selectShapesInRectangle(selectionRect, canvas);

                canvas.currentShape = null;
            }
        }

        /**
         * 框選矩形內的所有形狀
         * 修正版本：支援群組物件的選取
         */
        private void selectShapesInRectangle(Rectangle selectionRect, CanvasPanel canvas) {
            for (BaseShape shape : canvas.shapes) {
                if (isShapeInSelectionArea(shape, selectionRect)) {
                    shape.setSelected(true);
                    canvas.selectedShapes.add(shape);
                }
            }
        }

        /**
         * 判斷形狀是否在選取區域內
         * 支援不同類型的形狀
         */
        private boolean isShapeInSelectionArea(BaseShape shape, Rectangle selectionRect) {
            if (shape instanceof BasicShape) {
                BasicShape basicShape = (BasicShape) shape;
                Rectangle shapeBounds = basicShape.getBounds();

                // 檢查形狀的邊界是否與選取矩形相交或包含
                return selectionRect.intersects(shapeBounds) || selectionRect.contains(shapeBounds);

            } else if (shape instanceof CompositeShape) {
                CompositeShape compositeShape = (CompositeShape) shape;
                Rectangle groupBounds = compositeShape.getBounds();

                // 對於群組，檢查群組的邊界
                return selectionRect.intersects(groupBounds) || selectionRect.contains(groupBounds);

            } else if (shape instanceof Link) {
                Link link = (Link) shape;
                Point startPoint = link.getStartPoint();
                Point endPoint = link.getEndPoint();

                // 對於連結，檢查起點和終點是否在選取區域內
                return selectionRect.contains(startPoint) || selectionRect.contains(endPoint) ||
                        selectionRect.intersectsLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            }

            return false;
        }
    }

    /**
     * 形狀模式處理器
     */
    private static class ShapeModeHandler {
        public void handlePress(Mode mode, Point point, CanvasPanel canvas) {
            canvas.currentShape = canvas.shapeFactory.createShape(mode, point);
            if (canvas.currentShape != null) {
                canvas.currentShape.setDepth(canvas.nextDepth++);
                canvas.shapes.add(canvas.currentShape);
            }
        }

        public void handleDrag(Point point, CanvasPanel canvas) {
            if (canvas.currentShape != null) {
                canvas.settingDeltaX_DeltaY(point);
                canvas.currentShape.move(canvas.DeltaX, canvas.DeltaY);
                canvas.startPoint = point;
            }
        }
    }

    /**
     * 連結模式處理器
     */
    private static class LinkModeHandler {
        public void handlePress(Mode mode, Point point, CanvasPanel canvas) {
            for (BaseShape shape : canvas.shapes) {
                if (shape instanceof BasicShape && shape.contains(point)) {
                    BasicShape basicShape = (BasicShape) shape;
                    Point port = basicShape.getNearestPort(point);
                    if (port != null) {
                        canvas.currentShape = canvas.shapeFactory.createLink(mode, port);
                        if (canvas.currentShape instanceof Link) {
                            Link link = (Link) canvas.currentShape;
                            link.setStartShape(basicShape);
                        }
                        canvas.currentShape.setDepth(canvas.nextDepth++);
                        canvas.shapes.add(canvas.currentShape);
                        break;
                    }
                }
            }
        }

        public void handleDrag(Point point, CanvasPanel canvas) {
            if (canvas.currentShape instanceof Link) {
                ((Link) canvas.currentShape).setEndPoint(point);
            }
        }

        public void handleRelease(Mode mode, Point point, CanvasPanel canvas) {
            if (canvas.currentShape instanceof Link) {
                Link link = (Link) canvas.currentShape;
                boolean validEnd = false;

                for (BaseShape shape : canvas.shapes) {
                    if (shape instanceof BasicShape && shape.contains(point)) {
                        BasicShape basicShape = (BasicShape) shape;
                        Point port = basicShape.getNearestPort(point);

                        if (port != null) {
                            link.setEndPoint(port);
                            link.setEndShape(basicShape);
                            validEnd = true;
                            break;
                        }
                    }
                }

                if (!validEnd) {
                    canvas.shapes.remove(canvas.currentShape);
                }
            }
        }
    }

    /**
     * 選取管理器介面
     */
    private interface SelectionManager {
        void clearSelection(List<BaseShape> selectedShapes);
    }

    /**
     * 具體選取管理器
     */
    private static class ConcreteSelectionManager implements SelectionManager {
        @Override
        public void clearSelection(List<BaseShape> selectedShapes) {
            for (BaseShape shape : selectedShapes) {
                shape.setSelected(false);
            }
            selectedShapes.clear();
        }
    }

    /**
     * 形狀管理器介面
     */
    private interface ShapeManager {
        CompositeShape createGroup(List<BaseShape> selectedShapes, List<BaseShape> allShapes, int depth);
        void ungroupShape(CompositeShape group, List<BaseShape> allShapes, List<BaseShape> selectedShapes);
    }

    // 修正 ConcreteShapeManager 中的群組邏輯

    /**
     * 具體形狀管理器 - 修正版本
     * 支援將現有群組與其他物件重新組成新群組
     */
    private static class ConcreteShapeManager implements ShapeManager {
        @Override
        public CompositeShape createGroup(List<BaseShape> selectedShapes, List<BaseShape> allShapes, int depth) {
            if (selectedShapes.size() < 2) {
                return null; // 至少需要兩個物件才能組成群組
            }

            CompositeShape group = new CompositeShape();
            group.setDepth(depth);

            // 處理所有選取的形狀，包括現有的群組
            for (BaseShape shape : selectedShapes) {
                if (allShapes.contains(shape)) {
                    allShapes.remove(shape);

                    // 如果是群組，可以選擇是否要展開
                    // 這裡我們保持群組結構，直接加入新群組中
                    group.addShape(shape);
                }
            }

            return group;
        }

        @Override
        public void ungroupShape(CompositeShape group, List<BaseShape> allShapes, List<BaseShape> selectedShapes) {
            allShapes.remove(group);

            List<BaseShape> childShapes = group.getShapes();
            for (BaseShape shape : childShapes) {
                allShapes.add(shape);
                shape.setSelected(true);
                selectedShapes.add(shape);
            }

            selectedShapes.remove(group);
        }

        /**
         * 深度解除群組 - 新增方法
         * 遞迴地將所有巢狀群組展開為個別形狀
         */
        public void deepUngroupShape(CompositeShape group, List<BaseShape> allShapes, List<BaseShape> selectedShapes) {
            allShapes.remove(group);

            List<BaseShape> childShapes = group.getShapes();
            for (BaseShape shape : childShapes) {
                if (shape instanceof CompositeShape) {
                    // 遞迴展開子群組
                    deepUngroupShape((CompositeShape) shape, allShapes, selectedShapes);
                } else {
                    allShapes.add(shape);
                    shape.setSelected(true);
                    selectedShapes.add(shape);
                }
            }

            selectedShapes.remove(group);
        }
    }

    /**
     * 連結更新器
     * 負責更新形狀移動時相關連結的位置
     */
    private static class LinkUpdater {
        public void updateLinksForShape(BaseShape shape, List<BaseShape> allShapes, int deltaX, int deltaY) {
            // 收集所有關聯到此形狀的連結
            List<Link> relatedLinks = new ArrayList<>();

            for (BaseShape s : allShapes) {
                if (s instanceof Link) {
                    Link link = (Link) s;

                    // 檢查此連結是否關聯到此形狀或其子形狀
                    boolean isRelated = false;

                    if (shape instanceof BasicShape) {
                        if (link.isRelatedTo(shape)) {
                            isRelated = true;
                        }
                    } else if (shape instanceof CompositeShape) {
                        CompositeShape composite = (CompositeShape) shape;
                        if (composite.isRelatedToLink(link)) {
                            isRelated = true;
                        }
                    }

                    if (isRelated) {
                        relatedLinks.add(link);
                    }
                }
            }

            // 更新所有相關連結的位置
            for (Link link : relatedLinks) {
                if (shape instanceof BasicShape) {
                    // 使用修正後的方法更新連接點
                    link.updateEndpointForShape(shape, deltaX, deltaY);
                } else if (shape instanceof CompositeShape) {
                    // 如果是群組形狀，檢查每個子形狀
                    CompositeShape composite = (CompositeShape) shape;
                    List<BaseShape> childShapes = composite.getShapes();

                    for (BaseShape childShape : childShapes) {
                        if (childShape instanceof BasicShape) {
                            // 對群組中的每個基本形狀，使用修正後的方法更新連接點
                            link.updateEndpointForShape(childShape, deltaX, deltaY);
                        }
                    }
                }
            }

            // 移動完成後，確保所有連接點都是最新的
            for (Link link : relatedLinks) {
                link.updatePosition();
            }
        }
    }

    /**
     * 形狀刪除器
     * 負責刪除形狀及其相關連結
     */
    private static class ShapeDeleter {
        public void deleteShapes(List<BaseShape> shapesToDelete, List<BaseShape> allShapes) {
            // 創建一個臨時列表，避免並發修改異常
            List<BaseShape> shapesToRemove = new ArrayList<>(shapesToDelete);
            List<BaseShape> allToRemove = new ArrayList<>();

            // 對於每個要刪除的形狀
            for (BaseShape shape : shapesToRemove) {
                // 如果是群組，遞迴收集所有子形狀
                if (shape instanceof CompositeShape) {
                    collectAllShapesInGroup((CompositeShape) shape, allToRemove);
                }

                // 添加當前形狀到刪除列表
                allToRemove.add(shape);
            }

            // 收集所有需要刪除的連結線
            List<BaseShape> linksToRemove = new ArrayList<>();
            for (BaseShape s : allShapes) {
                if (s instanceof Link) {
                    Link link = (Link) s;
                    // 檢查連結是否與任何要刪除的形狀相關
                    for (BaseShape shapeToRemove : allToRemove) {
                        if (shapeToRemove instanceof BasicShape && link.isRelatedTo(shapeToRemove)) {
                            linksToRemove.add(link);
                            break;
                        } else if (shapeToRemove instanceof CompositeShape &&
                                ((CompositeShape) shapeToRemove).isRelatedToLink(link)) {
                            linksToRemove.add(link);
                            break;
                        }
                    }
                }
            }

            // 從畫布上移除所有標記的形狀和連結
            allShapes.removeAll(allToRemove);
            allShapes.removeAll(linksToRemove);
        }

        /**
         * 遞迴收集群組中的所有形狀
         */
        private void collectAllShapesInGroup(CompositeShape group, List<BaseShape> collector) {
            for (BaseShape shape : group.getShapes()) {
                if (shape instanceof CompositeShape) {
                    // 遞迴處理子群組
                    collectAllShapesInGroup((CompositeShape) shape, collector);
                }
                collector.add(shape);
            }
        }
    }
}