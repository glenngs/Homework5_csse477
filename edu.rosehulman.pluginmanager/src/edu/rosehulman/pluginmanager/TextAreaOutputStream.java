package edu.rosehulman.pluginmanager;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {

	private final JTextArea textArea;
	private final StringBuilder sb = new StringBuilder();

	public TextAreaOutputStream(final JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void write(int b) throws IOException {
		if (b == '\r') {
			return;
		} else if (b == '\n') {
			final String text = sb.toString() + "\n";
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append(text);
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
			});
			sb.setLength(0);
			return;
		} else {
			// TODO: This isn't very i18n friendly...
			sb.append((char) b);
		}

	}

}
