package problem.graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import problem.sprites.ISprite;
import problem.sprites.SpriteFactory;

public class MainWindow {
	private JFrame frame;
	private JPanel actionPanel;
	private AnimationPanel animationPanel;
	private JButton moveButton;
	private JButton createButton;
	private JButton resetButton;
	
	public MainWindow(String name, long sleepTime) {

		createWidgets(name, sleepTime);
		assignActions();
	}
	
	private void createWidgets(String name, long sleepTime) {
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		JPanel contentPane = (JPanel)frame.getContentPane();

		actionPanel = new JPanel();
		actionPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

		createButton = new JButton("Create New Shape");
		moveButton = new JButton("Animate");
		resetButton = new JButton("Reset");
		
		actionPanel.add(createButton);
		actionPanel.add(moveButton);
		actionPanel.add(resetButton);
		
		contentPane.add(actionPanel, BorderLayout.NORTH);
		
		animationPanel = new AnimationPanel(sleepTime);
		animationPanel.setBorder(BorderFactory.createTitledBorder("Animation"));

		contentPane.add(animationPanel, BorderLayout.CENTER);
	}
	
	private void assignActions() {
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ISprite s = SpriteFactory.createRandomSprite(animationPanel.getSize());
					animationPanel.add(s);
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		});

		moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animationPanel.animate();
				moveButton.setEnabled(false);
				resetButton.setEnabled(true);
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				animationPanel.reset();
				moveButton.setEnabled(true);
				resetButton.setEnabled(false);
			}
		});
	}
	
	public void show() {
		frame.setVisible(true);
	}
}
