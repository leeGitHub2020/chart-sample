import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import quadbase.ChartAPI.ColInfo;
import quadbase.ChartAPI.DBInfo;
import quadbase.ChartAPI.QbChart;
import quadbase.util.IAxis;
import quadbase.util.ICanvas;
import quadbase.util.IDataPointSet;
import quadbase.util.IFormat;
import quadbase.util.ILabel;
import quadbase.util.ILegend;
import quadbase.util.IMouseEventSet;
import quadbase.util.IPlot;
import quadbase.util.NumericFormat;
import quadbase.util.Position;

public class ChartSample extends Applet {

    static final long serialVersionUID = 1;

    public static void main(String[] args) {
        try {
            ChartSample chart = new ChartSample();
            Frame frame = new Frame();
            frame.setLayout(new BorderLayout());
            frame.add("Center", chart.doApplyingChartTemplate(null));
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.setSize(800, 600);
            frame.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init() {
        setLayout(new BorderLayout());
        add("Center", doApplyingChartTemplate(this));
    }

    Component doApplyingChartTemplate(Applet parent) {

        // Do not use EspressManager
        QbChart.setEspressManagerUsed(false);

        // Data Source
        DBInfo databaseInfo =
                new DBInfo("jdbc:postgresql://localhost:5432/sample", "org.postgresql.Driver",
                        "postgres", "abc123", "select year, record from m_chart order by year");

        // Column Mapping
        ColInfo columnMapping = new ColInfo();
        columnMapping.category = 0;
        columnMapping.value = 1;

        // Apply the template
        QbChart chart = new QbChart(parent, // container
                QbChart.VIEW2D, // chart dimension
                QbChart.LINE, // chart type
                databaseInfo, // data
                columnMapping, // column mapping
                null); // template

        // キャンバス設定
        ICanvas canvas = chart.gethCanvas();
        // グラフサイズ指定
        canvas.setSize(new Dimension(800, 380));
        // キャンバスで切り落とされるグラフ要素調整
        canvas.setFitOnCanvas(true);
        // スクロールバーを無効にする
        canvas.setScrollBarOption(ScrollPane.SCROLLBARS_NEVER);
        // グラフ背景色設定
        canvas.setBackgroundColor(Color.WHITE);

        // グラフアンチエイリアス設定
        chart.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // ドラッグイベントを無効にする
        IMouseEventSet mouseEvent = chart.gethMouseEvents();
        mouseEvent.setDragEnabled(false);

        // プロットエリア設定
        IPlot chartPlot = chart.gethChartPlot();
        // プロットエリア境界を表示にする
        chartPlot.setBorderVisible(true);
        // log---start
        System.out.println(chartPlot.getPosition().getX());
        System.out.println(chartPlot.getPosition().getY());
        System.out.println(chartPlot.getRelativeHeight());
        System.out.println(chartPlot.getRelativeWidth());
        // log--end
        // プロットエリア境界囲み枠設定
        chartPlot.setPosition(new Position((float) 0.1, (float) 0.1));
        chartPlot.setRelativeHeight((float) 0.8);
        chartPlot.setRelativeWidth((float) 0.8);

        // 凡例ボックス
        ILegend hLegend = chart.gethLegend();
        // 凡例ボックスを非表示にする
        hLegend.setVisible(false);

        // X軸設定
        IAxis xAxis = chart.gethXAxis();
        // X軸グリッドを表示にする
        xAxis.setGridVisible(true);
        // X軸ティッカーを非表示にする
        xAxis.setTickersVisible(false);
        // X軸ティッカーを非表示にする
        xAxis.setTickerStep(2);
        xAxis.setLabelStep(2);

        // TODO 調査が必要
        xAxis.setMaxScale(2020);
        xAxis.setMinScale(2004);
        xAxis.setScaleStep(2);

        // xAxis.setMaxLabelAndTickerCount(2020);
        // xAxis.setTickerLabels(new String[] {new String("a"), new String("b"), new String("c")});

        // y軸設定
        IAxis yAxis = chart.gethYAxis();
        // y軸グリッドを表示にする
        yAxis.setGridVisible(true);
        // y軸ティッカーを非表示にする
        yAxis.setTickersVisible(false);
        // Y軸タイトル(単位)設定
        // log---start
        System.out.println(yAxis.gethTitle().getAngle());
        System.out.println(yAxis.gethTitle().getOffset());
        // log---end
        // y軸タイトル設定
        // 角度設定
        yAxis.gethTitle().setAngle(0);
        // オフセット設定
        yAxis.gethTitle().setOffset(new Dimension(-34, 260));
        // 単位設定
        yAxis.gethTitle().setValue("(cm)");
        // Y軸ラベル設定

        IFormat yIformat = yAxis.getLabelFormat();
        NumericFormat nFormat = new NumericFormat();
        nFormat.setFormat('N', false, 2, 1, 1, 0, '.', 'N', false);
        yIformat.format(nFormat);

        // データポイント設定
        IDataPointSet pointSet = chart.gethDataPoints();
        // ポイントを表示にする
        pointSet.setPointsVisible(true);
        // 色設定
        Color pointColor = new Color(51, 102, 255);
        Color[] colors = {pointColor};
        pointSet.setColors(colors);
        // ラインの幅設定
        int[] lineWh = {2};
        pointSet.setDataLineThickness(lineWh);
        // データビューグラフシンボル設定
        int[] shape = {QbChart.SQUARE};
        pointSet.setPointShapes(shape);
        int[] shapeSize = {5};
        pointSet.setPointSizes(shapeSize);
        // ポイントラベル設定
        ILabel label = pointSet.gethLabel();
        label.setVisible(true);
        label.setOffset(new Dimension(5, -20));

        // Show chart in Viewer
        return chart;
    }
}
