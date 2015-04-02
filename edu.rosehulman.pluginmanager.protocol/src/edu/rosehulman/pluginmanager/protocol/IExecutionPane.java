package edu.rosehulman.pluginmanager.protocol;

import java.io.PrintStream;

import javax.swing.JPanel;

public interface IExecutionPane {
	//the process is required to add itself to the provided JPanel for rendering
	public void start(JPanel j);
	//the process is required to remove itself from the provided JPanel
	public void pause();
	//TODO: Ben Whatever is needed to set up the stream for the output. I have no idea if this is the right type.
	public void setStatusStream(PrintStream s);
}
