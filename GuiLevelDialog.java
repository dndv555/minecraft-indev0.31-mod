package net.minecraft.client.gui;

import java.awt.FileDialog;
import java.io.File;

class GuiLevelDialog extends Thread {
	private GuiLoadLevel parent;
	private boolean isSaveDialog;

	public GuiLevelDialog(GuiLoadLevel var1, boolean isSave) {
		this.parent = var1;
		this.isSaveDialog = isSave;
	}

	public final void run() {
		try {
			FileDialog dialog;
			
			if (isSaveDialog) {
				dialog = new FileDialog((java.awt.Dialog)null, "Save world", FileDialog.SAVE);
			} else {
				dialog = new FileDialog((java.awt.Dialog)null, "Load world", FileDialog.LOAD);
			}
			
			dialog.setVisible(true);
			String file = dialog.getFile();
			String dir = dialog.getDirectory();
			
			if (file != null && dir != null) {
				File selectedFile = new File(dir, file);
				GuiLoadLevel.a(parent, selectedFile);
			}
		} finally {
			GuiLoadLevel.a(parent, false);
		}
	}
}