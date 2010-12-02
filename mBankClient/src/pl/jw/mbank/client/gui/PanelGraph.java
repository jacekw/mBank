package pl.jw.mbank.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import pl.jw.mbank.client.RequestProxyFactory;
import pl.jw.mbank.common.dto.PresentationGraphData;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.StockQuotesData;
import pl.jw.mbank.common.request.IPresentation;

public class PanelGraph extends JPanel {

	interface IPanelGraphDataInterpreter {
		BigDecimal getDataColumn(StockQuotesData stockQuotesData);

		List<PresentationGraphData> getData(SfiData sfiData) throws SQLException, Exception;

		String getValueAxisLabel();
	}

	static class PanelGraphDataInterpreterDelta implements IPanelGraphDataInterpreter {
		public BigDecimal getDataColumn(StockQuotesData stockQuotesData) {
			return stockQuotesData.getDelta();
		}

		public List<PresentationGraphData> getData(SfiData sfiData) throws SQLException, Exception {
			List<PresentationGraphData> data = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getGraphData(sfiData);
			return data;
		}

		@Override
		public String getValueAxisLabel() {
			return "Delta [%]";
		}

	}

	static class PanelGraphDataInterpreterValue implements IPanelGraphDataInterpreter {
		public BigDecimal getDataColumn(StockQuotesData stockQuotesData) {
			return stockQuotesData.getValue();
		}

		public List<PresentationGraphData> getData(SfiData sfiData) throws SQLException, Exception {
			List<PresentationGraphData> data = RequestProxyFactory.getInstance().requestData(IPresentation.class)
					.getGraphData(sfiData);
			return data;
		}

		@Override
		public String getValueAxisLabel() {
			return "Value";
		}
	}

	private final IPanelGraphDataInterpreter dataInterpreter;

	private JFreeChart chart;
	private ChartPanel chartPanel;

	public PanelGraph(IPanelGraphDataInterpreter dataInterpreter) {
		this.dataInterpreter = dataInterpreter;

		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		chart = ChartFactory.createTimeSeriesChart("", "date", "trend", null, false, false, true);

		chartPanel = new ChartPanel(chart);

		add(chartPanel, BorderLayout.CENTER);

		setup();
	}

	private void setup() {

		chart.setAntiAlias(true);
		chart.setBorderVisible(true);
		chart.setTextAntiAlias(true);

		chartPanel.setAlignmentY(0);
		chartPanel.setMouseZoomable(true, false);
		chartPanel.setAutoscrolls(true);
		chartPanel.setDisplayToolTips(true);
		chartPanel.setOpaque(true);
		//		chartPanel.setMaximumDrawHeight(200);
		//		chartPanel.setMaximumDrawWidth(800);
		chartPanel.setPreferredSize(new Dimension(Short.MAX_VALUE, 200));

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);

		plot.setDomainGridlinePaint(Color.GREEN);
		plot.setDomainZeroBaselineVisible(true);
		plot.setDomainZeroBaselinePaint(Color.RED);

		plot.setRangeGridlinePaint(Color.GREEN);
		plot.setRangeZeroBaselineVisible(true);
		plot.setRangeZeroBaselinePaint(Color.RED);

		//		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		//		plot.setDomainCrosshairVisible(true);
		//		plot.setRangeCrosshairVisible(true);
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setPaint(Color.ORANGE);
			//			renderer.setBaseShapesVisible(true);
			//			renderer.setBaseShapesFilled(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setLabel("Course date");
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MM"));
		//		axis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));

		plot.getRangeAxis().setLabel(dataInterpreter.getValueAxisLabel());

		setVisible(false);
	}

	void refresh(SfiData sfiData) throws Exception {

		if (sfiData != null) {
			chart.setTitle(sfiData.getName());

			List<PresentationGraphData> data = dataInterpreter.getData(sfiData);

			TimeSeries ts = new TimeSeries("");
			for (StockQuotesData stockQuotesData : data) {
				ts.add(new Day(stockQuotesData.getDate()), dataInterpreter.getDataColumn(stockQuotesData));
			}

			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(ts);

			chart.getXYPlot().setDataset(dataset);
			//			chart.fireChartChanged();
		}

	}

}
