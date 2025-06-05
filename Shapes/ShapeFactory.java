package Shapes;

import java.awt.*;
import Canvas.CanvasPanel.Mode;

/**
 * 建立各種形狀
 * 處理不同類型的形狀或連結的建立
 */
public class ShapeFactory {
    // 矩形和橢圓的固定大小常數
    private static final int RECT_WIDTH = 100;
    private static final int RECT_HEIGHT = 60;
    private static final int OVAL_WIDTH = 100;
    private static final int OVAL_HEIGHT = 80;

    // 形狀建立器
    private ShapeCreator shapeCreator; //形狀
    private LinkCreator linkCreator; //線條類別

    /**
     * 建構函數
     */
    public ShapeFactory() {
        this.shapeCreator = new ConcreteShapeCreator();
        this.linkCreator = new ConcreteLinkCreator();
    }

    /**
     * 公用建立基本形狀
     * @param mode 模式
     * @param start 起始點
     * @return 建立的形狀
     * 內部實際的建立靠 內部形狀建立的interface
     */
    public BaseShape createShape(Mode mode, Point start) {
        return shapeCreator.createShape(mode, start);
    }

    /**
     * 公用建立連結
     * @param mode 模式
     * @param start 起始點
     * @return 建立的連結
     */
    public BaseShape createLink(Mode mode, Point start) {
        return linkCreator.createLink(mode, start);
    }

    /**
     * 內部形狀建立的interface
     * 實際建立在 private static class ConcreteShapeCreator implements ShapeCreator
     */
    private interface ShapeCreator {
        BaseShape createShape(Mode mode, Point start);
    }

    /**
     * 實際具體形狀建立
     */
    private static class ConcreteShapeCreator implements ShapeCreator {
        @Override
        public BaseShape createShape(Mode mode, Point start) {
            switch (mode) { //根據左邊Toolbar選取不同的Mode，來建立不同的形狀
                case RECT:
                    return new CustomRectShape(start); //矩形
                case OVAL:
                    return new CustomOvalShape(start); //橢圓形
                case SELECT:
                    return null; // 選取模式不建立形狀
                default:
                    return null;
            }
        }
    }

    /**
     * 內部連結建立的interface
     */
    private interface LinkCreator {
        BaseShape createLink(Mode mode, Point start);
    }

    /**
     * 實際具體連結建立器
     */
    private static class ConcreteLinkCreator implements LinkCreator {
        @Override
        public BaseShape createLink(Mode mode, Point start) {
            switch (mode) {
                case ASSOCIATION:
                    return new AssociationLink(start);
                case GENERALIZATION:
                    return new GeneralizationLink(start);
                case COMPOSITION:
                    return new CompositionLink(start);
                default:
                    return null;
            }
        }
    }

    /**
     * 自定義矩形類別 - 固定大小並有兩條橫線
     * 使用模板方法模式實現特殊的矩形樣式
     */
    public static class CustomRectShape extends BasicShape {
        private CustomDrawingStrategy drawingStrategy;

        /**
         * 建構函數
         * @param start 起始點
         */
        public CustomRectShape(Point start) {
            super(start);
            // 設定固定大小
            bounds = new Rectangle(start.x, start.y, RECT_WIDTH, RECT_HEIGHT);
            drawingStrategy = new UMLClassDrawingStrategy();
            updatePorts();
        }

        /**
         * 繪製自定義矩形
         * @param g2d 圖形上下文
         */
        @Override
        public void draw(Graphics2D g2d) {
            drawingStrategy.draw(g2d, bounds, this);
        }

        /**
         * 調整大小（固定大小，不支援調整）
         * @param start 起始點
         * @param end 結束點
         */
        @Override
        public void resize(Point start, Point end) {
            // 設定固定大小，不支援調整大小
            bounds.x = start.x;
            bounds.y = start.y;
            bounds.width = RECT_WIDTH;
            bounds.height = RECT_HEIGHT;
            updatePorts();
        }

        /**
         * 更新連接埠
         */
        @Override
        public void updatePorts() {
            ports.clear();
            // 8個連接埠
            PortGenerator generator = new RectanglePortGenerator();
            ports.addAll(generator.generatePorts(bounds));
        }

        /**
         * 自定義繪製interface
         */
        private interface CustomDrawingStrategy {
            void draw(Graphics2D g2d, Rectangle bounds, BasicShape shape);
        }

        /**
         * UML 矩形類別圖繪製
         */
        private static class UMLClassDrawingStrategy implements CustomDrawingStrategy {
            @Override
            public void draw(Graphics2D g2d, Rectangle bounds, BasicShape shape) {
                // 繪製矩形外框
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

                // 繪製兩條橫線（UML類別圖的分隔線）
                drawSeparatorLines(g2d, bounds);

                // 繪製標籤和連接埠
                shape.drawLabel(g2d);
                shape.drawPorts(g2d);
            }

            private void drawSeparatorLines(Graphics2D g2d, Rectangle bounds) {
                int lineSpacing = bounds.height / 3;
                g2d.drawLine(bounds.x, bounds.y + lineSpacing,
                        bounds.x + bounds.width, bounds.y + lineSpacing);
                g2d.drawLine(bounds.x, bounds.y + 2 * lineSpacing,
                        bounds.x + bounds.width, bounds.y + 2 * lineSpacing);
            }
        }
    }

    /**
     * 自定義橢圓類別 - 固定大小
     */
    public static class CustomOvalShape extends OvalShape {

        /**
         * 建構函數
         * @param start 起始點
         */
        public CustomOvalShape(Point start) {
            super(start);
            // 設定固定大小
            bounds = new Rectangle(start.x, start.y, OVAL_WIDTH, OVAL_HEIGHT);
            ellipse = new java.awt.geom.Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
            updatePorts();
        }

        /**
         * 調整大小（固定大小，不支援調整）
         * @param start 起始點
         * @param end 結束點
         */
        @Override
        public void resize(Point start, Point end) {
            // 設定固定大小，不支援調整大小
            bounds.x = start.x;
            bounds.y = start.y;
            bounds.width = OVAL_WIDTH;
            bounds.height = OVAL_HEIGHT;
            ellipse = new java.awt.geom.Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
            updatePorts();
        }

        /**
         * 更新連接埠
         */
        @Override
        public void updatePorts() {
            ports.clear();
            PortGenerator generator = new OvalPortGenerator();
            ports.addAll(generator.generatePorts(bounds));
        }
    }

    /**
     * 連接埠生成 interface
     * 處理不同形狀的連接埠生成
     */
    private interface PortGenerator {
        java.util.List<Point> generatePorts(Rectangle bounds);
    }

    /**
     * 實際矩形連接埠生成器
     */
    private static class RectanglePortGenerator implements PortGenerator {
        @Override
        public java.util.List<Point> generatePorts(Rectangle bounds) {
            java.util.List<Point> ports = new java.util.ArrayList<>();
            ports.add(new Point(bounds.x, bounds.y)); // 左上
            ports.add(new Point(bounds.x + bounds.width / 2, bounds.y)); // 上中
            ports.add(new Point(bounds.x + bounds.width, bounds.y)); // 右上
            ports.add(new Point(bounds.x, bounds.y + bounds.height / 2)); // 左中
            ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2)); // 右中
            ports.add(new Point(bounds.x, bounds.y + bounds.height)); // 左下
            ports.add(new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height)); // 下中
            ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height)); // 右下
            return ports;
        }
    }

    /**
     * 橢圓連接埠生成器
     */
    private static class OvalPortGenerator implements PortGenerator {
        @Override
        public java.util.List<Point> generatePorts(Rectangle bounds) {
            java.util.List<Point> ports = new java.util.ArrayList<>();
            ports.add(new Point(bounds.x + bounds.width / 2, bounds.y)); // 上
            ports.add(new Point(bounds.x, bounds.y + bounds.height / 2)); // 左
            ports.add(new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2)); // 右
            ports.add(new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height)); // 下
            return ports;
        }
    }
}