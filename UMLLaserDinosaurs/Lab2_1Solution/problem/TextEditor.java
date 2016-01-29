package problem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * <p>
 * The awesomest text editor ever developed in the awesomest language ever
 * created by humans!
 * </p>
 * 
 * <p>
 * It reads the text from the supplied {@link InputStream} and writes the text
 * to the supplied {@link OutputStream} when a user closes the application.
 * </p>
 * 
 * @author Chandan R. Rupakheti (chandan.rupakheti@rose-hulman.edu)
 */
public class TextEditor {
	private JFrame frame;

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel label;

	private InputStream in;
	private OutputStream out;
	private String text;

	/**
	 * Create the text editor object with the supplied parameters.
	 * 
	 * @param in
	 *            Take in an {@link InputStream} to read the input file from.
	 * @param out
	 *            Write the final text to the supplied {@link OutputStream} when
	 *            the user closes the application.
	 */
	public TextEditor(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;

		this.text = "";
		try {
			StringBuffer buffer = new StringBuffer();
			int input = 0;
			while ((input = in.read()) != -1) {
				buffer.append((char) input);
			}
			in.close();
			text = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createAndShowGUI() {
		frame = new JFrame("Awesomest Text Viewer");

		textArea = new JTextArea(text, 20, 60);
		textArea.setPreferredSize(new Dimension(800, 600));
		textArea.setLineWrap(true);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Add the scroll pane to the center of the window
		frame.add(scrollPane, BorderLayout.CENTER);

		// Add the label as status
		label = new JLabel("Ready");
		frame.add(label, BorderLayout.SOUTH);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				try {
					for (char c : textArea.getText().toCharArray()) {
						out.write(c);
					}
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void execute() throws Exception {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Basically, shows up the GUI.
				createAndShowGUI();
			}
		});
	}

}
