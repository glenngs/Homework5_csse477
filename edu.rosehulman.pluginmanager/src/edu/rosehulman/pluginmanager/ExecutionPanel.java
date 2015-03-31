package edu.rosehulman.pluginmanager;

import javax.swing.JPanel;

import edu.rosehulman.plugin.counter.Plugin;
import edu.rosehulman.pluginmanager.protocol.IExecutionPane;


public class ExecutionPanel extends JPanel{

	public ExecutionPanel() {
		
	}
	
	public void renderPlugin(IExecutionPane p){
		IExecutionPane selectedPlugin = p;
		
		if(selectedPlugin != null)
		{
			selectedPlugin.start(this);
		}
	}

}
