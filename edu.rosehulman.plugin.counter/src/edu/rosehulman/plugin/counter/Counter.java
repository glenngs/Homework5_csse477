package edu.rosehulman.plugin.counter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.rosehulman.pluginmanager.protocol.IExecutionPane;

public class Counter extends JPanel implements IExecutionPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7195746257630036388L;
	//The constructor should take 1 argument, the name of the file
	private JPanel parent;
	private int count;
	private JLabel label;
	private String name;
	private PrintStream statusAreaStream;
	
	
	public Counter(String name) {
		this.name = name;
		this.count = 0;
		this.label = new JLabel(Integer.toString(count));
		this.add(label);
		
		JButton button = new JButton("Increment Counter");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				count += 1;
				label.setText(Integer.toString(count));
				statusAreaStream.print("Button Pressed! Counter at: " + count +"\n");
			}
		});
		this.add(button);
	}
	
	public void start (JPanel p) {
		this.parent = p;
		this.parent.add(this);
	}
	
	public void pause() {
		this.parent.remove(this);		
	}

	
	public void setStatusStream(PrintStream s) {
		this.statusAreaStream = s;	
	}
	
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("Plugin Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        JPanel testPanel = new JPanel();
        frame.add(testPanel);
        Counter c = new Counter("test name");
        c.start(testPanel);
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	@Override
	// This should always return the name given to the constructor
	public String toString() {
		return name;
	}
}

