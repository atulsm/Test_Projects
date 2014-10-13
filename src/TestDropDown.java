import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class TestDropDown extends JFrame {

	static final String dbs[] = new String[] { "Oracle", "Db2", "MySql" };

	
	public TestDropDown() {
		super();
		setTitle("Testing dropDown with ");
		setSize(100, 50);
		setLocationRelativeTo(null);
		this.add(new TestPanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TestDropDown ex = new TestDropDown();
				ex.setVisible(true);
			}
		});
	}

	private static class TestPanel extends JPanel {

		public TestPanel() {
			super(new BorderLayout());
			// ScrollCombo combo = new ScrollCombo(dbs);
			JComboBox combo = new JComboBox(dbs);
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			renderer.setPreferredSize(new Dimension(200, 130));
			combo.setRenderer(renderer);

			this.add(combo);
		}
	}

	private static class ScrollCombo extends JComboBox {
		public ScrollCombo(Object items[]) {
			super(items);
			setUI(new ScrollComboUI());
		}

		private class ScrollComboUI extends BasicComboBoxUI {
			protected ComboPopup createPopup() {
				BasicComboPopup popup = new BasicComboPopup(comboBox) {
					protected JScrollPane createScroller() {
						return new JScrollPane(
								list,
								ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					}
				};
				return popup;
			}
		}
	}

	private static class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		public ComboBoxRenderer() {
		//	setHorizontalAlignment(CENTER);
		//	setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			String db = (String)value;
			
			//if(db.equals("Db2")){
				ImageIcon icon = new ImageIcon("good1.png");
				setIcon(icon);
			//}
			
			setText(db);
			setToolTipText(db);
			return this;
		}

	}

}
