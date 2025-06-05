import javax.swing.*;
import java.awt.*;
import MenuBar.MenuBar;
import ToolBar.ToolBar;
import Canvas.CanvasPanel;

/**
 * UML編輯器
 * 使用外觀模式整合各個子系統
 * 使用單例模式確保只有一個編輯器實例
 * 整合工具欄、選單欄和畫布，實現UML圖形編輯功能
 */
public class UMLEditor {
    private static UMLEditor instance;
    private JPanel mainPanel;
    private CanvasPanel canvasPanel; // 畫布面板
    private ToolBar toolbar; // 工具欄
    private MenuBar menuBar; // 選單欄
    private UIThemeManager uiThemeManager; // UI主題管理器（重新命名避免衝突）

    /**
     * 建構函數（單例模式）
     */
    private UMLEditor() {
        initialize();
    }

    /**
     * 取得單例實例
     * @return UMLEditor實例
     */
    public static UMLEditor getInstance() {
        if (instance == null) {
            instance = new UMLEditor();
        }
        return instance;
    }

    /**
     * 初始化編輯器
     */
    private void initialize() {
        uiThemeManager = new ConcreteUIThemeManager();
        setupComponents();
        layoutComponents();
    }

    /**
     * 設置組件
     */
    private void setupComponents() {
        mainPanel = new JPanel(new BorderLayout());
        canvasPanel = new CanvasPanel();
        toolbar = new ToolBar();
        toolbar.setCanvasPanel(canvasPanel);
    }

    /**
     * 布局組件
     */
    private void layoutComponents() {
        mainPanel.add(toolbar, BorderLayout.WEST);
        mainPanel.add(canvasPanel, BorderLayout.CENTER);
    }

    /**
     * 創建應用程式窗口
     * @return 配置好的JFrame
     */
    public JFrame createApplicationWindow() {
        JFrame frame = new JFrame("UML Editor");

        // 設置UI外觀
        uiThemeManager.setupLookAndFeel();

        // 設置內容面板
        frame.setContentPane(mainPanel);

        // 設置選單欄
        menuBar = new MenuBar(frame);
        menuBar.setCanvasPanel(canvasPanel);
        frame.setJMenuBar(menuBar);

        // 配置窗口屬性
        WindowConfigurator configurator = new WindowConfigurator();
        configurator.configureWindow(frame);

        return frame;
    }

    /**
     * 取得主面板
     * @return 主面板
     */
    public JPanel getPanel() {
        return mainPanel;
    }

    /**
     * 取得畫布面板
     * @return 畫布面板
     */
    public CanvasPanel getCanvasPanel() {
        return canvasPanel;
    }

    /**
     * 取得工具欄
     * @return 工具欄
     */
    public ToolBar getToolbar() {
        return toolbar;
    }

    /**
     * 取得選單欄
     * @return 選單欄
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * UI主題管理器介面
     * 使用策略模式處理不同的UI配置
     * 重新命名避免與 javax.swing.UIManager 衝突
     */
    private interface UIThemeManager {
        void setupLookAndFeel();
    }

    /**
     * 具體UI主題管理器
     */
    private static class ConcreteUIThemeManager implements UIThemeManager {
        @Override
        public void setupLookAndFeel() {
            try {
                // 使用完整類別名稱明確指定 Swing 的 UIManager
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (ClassNotFoundException | InstantiationException |
                     IllegalAccessException | UnsupportedLookAndFeelException e) {
                // 如果設置失敗，使用默認外觀
                System.err.println("無法設置系統外觀: " + e.getMessage());
                // 可以選擇設置為跨平台外觀作為備選
                try {
                    javax.swing.UIManager.setLookAndFeel(
                            javax.swing.UIManager.getCrossPlatformLookAndFeelClassName()
                    );
                } catch (Exception fallbackException) {
                    System.err.println("無法設置備選外觀: " + fallbackException.getMessage());
                }
            }
        }
    }

    /**
     * 窗口配置器
     * 負責配置應用程式窗口的屬性
     */
    private static class WindowConfigurator {
        private static final int DEFAULT_WIDTH = 800;
        private static final int DEFAULT_HEIGHT = 600;

        /**
         * 配置窗口
         * @param frame 要配置的窗口
         */
        public void configureWindow(JFrame frame) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            frame.setLocationRelativeTo(null); // 視窗置中

            // 設置窗口圖標（如果有的話）
            setWindowIcon(frame);

            // 設置最小尺寸
            frame.setMinimumSize(new Dimension(600, 400));
        }

        /**
         * 設置窗口圖標
         * @param frame 窗口
         */
        private void setWindowIcon(JFrame frame) {
            // 這裡可以設置應用程式圖標
            // frame.setIconImage(iconImage);
        }
    }

    /**
     * 應用程式啟動器
     * 使用建造者模式配置應用程式
     */
    public static class ApplicationLauncher {
        private boolean visible = true;
        private String title = "UML Editor";

        /**
         * 設置是否可見
         * @param visible 可見性
         * @return ApplicationLauncher
         */
        public ApplicationLauncher setVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        /**
         * 設置標題
         * @param title 標題
         * @return ApplicationLauncher
         */
        public ApplicationLauncher setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 啟動應用程式
         * @return 創建的窗口
         */
        public JFrame launch() {
            // 確保在EDT中運行
            final JFrame[] frameHolder = new JFrame[1];

            SwingUtilities.invokeLater(() -> {
                UMLEditor editor = UMLEditor.getInstance();
                JFrame frame = editor.createApplicationWindow();
                frame.setTitle(title);
                frame.setVisible(visible);
                frameHolder[0] = frame;
            });

            return frameHolder[0];
        }
    }

    /**
     * 主程式入口點
     * @param args 命令行參數
     */
    public static void main(String[] args) {
        // 使用建造者模式啟動應用程式
        new ApplicationLauncher()
                .setTitle("UML Editor")
                .setVisible(true)
                .launch();
    }
}