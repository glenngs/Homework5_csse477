package edu.rosehulman.plugin.counter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.rosehulman.pluginmanager.protocol.IExecutionPane;

public class Counter extends Plugin {
	
	//The constructor should take 1 argument, the name of the file
	public Counter(String name) {
		super(name);
	}

}
