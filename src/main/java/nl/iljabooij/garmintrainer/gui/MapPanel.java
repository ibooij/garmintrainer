package nl.iljabooij.garmintrainer.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.google.inject.Inject;

public class MapPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	@Inject
	public MapPanel(final MapViewer mapViewer) {
		super(new BorderLayout());
		setBorder(new TitledBorder("Map"));
		add(mapViewer, BorderLayout.CENTER);
	}

}
