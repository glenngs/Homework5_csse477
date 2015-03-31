package edu.rosehulman.pluginmanager;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;

public class Main {
    public static void addComponentsToPane(Container pane) throws IOException {
         
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
                  
        JLabel center = new JLabel("Main section (Gabe)");
        center.setPreferredSize(new Dimension(400, 400));
        pane.add(center, BorderLayout.CENTER);
         
        ListingPanel listingPanel = new ListingPanel();
        listingPanel.setPreferredSize(new Dimension(200,200));
        (new Thread(listingPanel)).start();
        pane.add(listingPanel, BorderLayout.LINE_START);
         
        JLabel text = new JLabel("Text output (Ben)");
        text.setPreferredSize(new Dimension(200, 200));
        pane.add(text, BorderLayout.PAGE_END);
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     * @throws IOException 
     */
    private static void createAndShowGUI() throws IOException {
         
        //Create and set up the window.
        JFrame frame = new JFrame("Plugin Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        //Use the content pane's default BorderLayout. No need for
        //setLayout(new BorderLayout());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
