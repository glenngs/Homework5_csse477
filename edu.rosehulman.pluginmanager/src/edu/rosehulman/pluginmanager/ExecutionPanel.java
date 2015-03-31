package edu.rosehulman.pluginmanager;

import javax.swing.JPanel;

import edu.rosehulman.pluginmanager.ListingPanel;
import edu.rosehulman.pluginmanager.protocol.IExecutionPane;


public class ExecutionPanel extends JPanel implements Runnable {

	public ExecutionPanel() {
	}
	
	public void getPlugin(ListingPanel list){
		IExecutionPane selectedPlugin = list.dataList.getSelectedValue();
		
		if(selectedPlugin != null)
		{
			
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

}
