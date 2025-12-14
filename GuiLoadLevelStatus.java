package net.minecraft.client.gui;
import java.util.TimerTask;
public class GuiLoadLevelStatus extends TimerTask {
	private final GuiLoadLevel guiLoadLevel;
	public GuiLoadLevelStatus(GuiLoadLevel guiLoadLevel) {
		this.guiLoadLevel = guiLoadLevel;
	}
	public void run() {
		guiLoadLevel.status = "";
		guiLoadLevel.frozen = false;
	}
}