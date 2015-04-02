package edu.rosehulman.pluginmanager;

import javax.swing.JPanel;

import edu.rosehulman.pluginmanager.protocol.IExecutionPane;


public class ExecutionPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4464217608767640741L;
	private IExecutionPane currentPlugin = null;

	public ExecutionPanel() {
		
	}
	
	public void renderPlugin(IExecutionPane p){
		if (this.currentPlugin != null) {
			this.currentPlugin.pause();
		}
		
		if(p != null)
		{
			p.start(this);
			this.currentPlugin = p;
			this.repaint();
		}
	}

}
