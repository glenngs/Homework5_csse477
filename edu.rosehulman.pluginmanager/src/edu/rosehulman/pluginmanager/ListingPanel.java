package edu.rosehulman.pluginmanager;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.rosehulman.plugin.counter.Counter;
import edu.rosehulman.plugin.counter.Plugin;
import edu.rosehulman.pluginmanager.protocol.IExecutionPane;

public class ListingPanel extends JPanel implements Runnable {

	final JList<IExecutionPane> dataList = new JList<IExecutionPane>();
	private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final OutputStream statusArea;
    private final ExecutionPanel executionArea;
	
	public ListingPanel(final OutputStream statusArea, final ExecutionPanel executionArea) throws IOException {
		this.statusArea = statusArea;
		this.executionArea = executionArea;
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		final JLabel label = new JLabel("Update");

		grabPlugins();
		dataList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					// TODO Gabe, this is the method you should use to call your
					// code
					if (dataList.getSelectedValue() != null) {
						label.setText(dataList.getSelectedValue().toString());
						ListingPanel.this.executionArea.renderPlugin(dataList.getSelectedValue());
					} else {
						// This means that the selected plugin was deleted. I'm
						// not sure what to do in this case but we should handle
						// it.
						label.setText("None");
					}
				}
			}
		});

		add(dataList);
		add(label);

		System.out.println(dataList.getModel().getSize());
	}

	public void grabPlugins() throws IOException {
		File dir = new File("plugins");
		System.out.println(dir.getAbsolutePath());
		// If directory doesn't exist, create it.
		if (!dir.exists()) {
			dir.mkdir();
		}
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		DefaultListModel<IExecutionPane> model = new DefaultListModel<IExecutionPane>();
		dataList.setModel(model);
		if (files != null) {
			for (File jarfile : files) {
				addPlugin(jarfile);
			}
		}
		registerPath(Paths.get(dir.getAbsolutePath()));
	}

	public void addPlugin(File jarfile) {
		String pluginName = stripExtension(jarfile.getName());
		System.out.println("Adding plugin: " + pluginName);
		// TODO Gabe: Right now, I am importing Counter and just assuming that
		// all added jars are of that type for testing my part
		// You need to make it so it uses reflection to get the actual class
		// instead from the plugin
		
		try {
			URL fileURL = new URL("file:" + jarfile.getAbsolutePath());
			System.out.println("Addition: " + fileURL.getPath());
			URLClassLoader loader = new URLClassLoader(new URL[] { fileURL });
			JarFile jf = new JarFile(jarfile.getAbsoluteFile());
			jf.getManifest();
			String className = jf.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
			Class<?> c = loader.loadClass(className);
			final Plugin p = (Plugin) c.getConstructor(java.lang.String.class).newInstance(pluginName);
			p.setStatusStream(statusArea);
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					((DefaultListModel<IExecutionPane>) dataList.getModel()).addElement(p);
				}
			});
			loader.close();
			jf.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removePlugin(File jarfile) {
		final DefaultListModel<IExecutionPane> model = (DefaultListModel<IExecutionPane>) dataList
				.getModel();
		final String pluginName = stripExtension(jarfile.getName());
		System.out.println("Removing plugin: " + pluginName);
		for (int i = 0; i < model.size(); i++) {
			if (pluginName.equals(model.get(i).toString())) {
				final int j = i;
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						System.out.println("Removal: "+pluginName);
						model.remove(j);
					}
				});
				break;
			}
		}
	}

	static String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void registerPath(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		keys.put(key, dir);
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	public void run() {
		for (;;) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path name = ev.context();
				
				// TODO: OH GOD THIS IS SO UGLY
				File jarfile = new File(dir.toString(),name.toString());

				if (kind == ENTRY_CREATE) {
					removePlugin(jarfile);
					addPlugin(jarfile);
				} else if (kind == ENTRY_DELETE) {
					removePlugin(jarfile);
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}
}
