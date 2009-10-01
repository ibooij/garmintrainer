package nl.iljabooij.garmintrainer.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import nl.iljabooij.garmintrainer.model.Activity;

import com.google.inject.Inject;

/**
 * Panel that shows an overview of all information in the {@link Activity} file.
 * @author ilja
 *
 */
public class OverviewPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, normally called by Guice.
	 * @param summaryPanel panel holding the summary
	 * @param mapViewer panel holding the map
	 * @param chartComponent panel holding the chart.
	 */
	@Inject
	public OverviewPanel(final SummaryPanel summaryPanel,
			final MapViewer mapViewer,
			final ChartComponent chartComponent) {
			
		super();
		setLayout(new GridLayout(2, 2, 5, 5));
		setName("Overview");

		// use a holder for the map to easily add a border. Otherwise, the
		// map would be drawn over the border.
		JPanel mapHolder = new JPanel(new BorderLayout());
		mapHolder.setBorder(new TitledBorder("Map"));
		mapHolder.add(mapViewer, BorderLayout.CENTER);
		
		summaryPanel.setBorder(new TitledBorder("Summary"));
		chartComponent.setBorder(new TitledBorder("Chart"));
		add(summaryPanel);
		add(mapHolder);
		add(new JPanel()); // empty block
		add(chartComponent);
	}
}
