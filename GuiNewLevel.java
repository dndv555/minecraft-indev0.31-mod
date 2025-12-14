package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public final class GuiNewLevel extends GuiScreen {
	private GuiScreen prevGui;
	private String[] worldType = new String[]{"Inland", "Island", "Floating", "Flat"};
	private String[] worldShape = new String[]{"Square", "Long", "Deep"};
	private String[] worldSize = new String[]{"Small", "Normal", "Huge"};
	private String[] worldTheme = new String[]{"Normal", "Hell"};
	private int selectedWorldType = 1;
	private int selectedWorldShape = 0;
	private int selectedWorldSize = 1;
	private int selectedWorldTheme = 0;
        private float updateCounter = 0.0F;

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	public GuiNewLevel(GuiScreen var1) {
		this.prevGui = var1;
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Type: "));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Shape:"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48, "Size: "));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 72, "Theme: "));
		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 96 + 12, "Create"));
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
		this.worldOptions();
	}

	private void worldOptions() {
		((GuiButton)this.controlList.get(0)).displayString = "Type: " + this.worldType[this.selectedWorldType];
		((GuiButton)this.controlList.get(1)).displayString = "Shape: " + this.worldShape[this.selectedWorldShape];
		((GuiButton)this.controlList.get(2)).displayString = "Size: " + this.worldSize[this.selectedWorldSize];
		((GuiButton)this.controlList.get(3)).displayString = "Theme: " + this.worldTheme[this.selectedWorldTheme];
	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 5) {
			this.mc.displayGuiScreen(this.prevGui);
		} else if(var1.id == 4) {
			this.mc.generateNewLevel(this.selectedWorldSize, this.selectedWorldShape, this.selectedWorldType, this.selectedWorldTheme);
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else if(var1.id == 0) {
			this.selectedWorldType = (this.selectedWorldType + 1) % this.worldType.length;
		} else if(var1.id == 1) {
			this.selectedWorldShape = (this.selectedWorldShape + 1) % this.worldShape.length;
		} else if(var1.id == 2) {
			this.selectedWorldSize = (this.selectedWorldSize + 1) % this.worldSize.length;
		} else if(var1.id == 3) {
			this.selectedWorldTheme = (this.selectedWorldTheme + 1) % this.worldTheme.length;
		}

		this.worldOptions();
	}

	public final void drawScreen(int var1, int var2) {
                Tessellator var3 = Tessellator.instance;
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.startDrawingQuads();
		var3.setColorOpaque_I(4210752);
		var3.addVertexWithUV(0.0F, (float)this.height, 0.0F, 0.0F, (float)this.height / 32.0F + this.updateCounter);
		var3.addVertexWithUV((float)this.width, (float)this.height, 0.0F, (float)this.width / 32.0F, (float)this.height / 32.0F + this.updateCounter);
		var3.addVertexWithUV((float)this.width, 0.0F, 0.0F, (float)this.width / 32.0F, 0.0F + this.updateCounter);
		var3.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F + this.updateCounter);
		var3.draw();  
		drawCenteredString(this.fontRenderer, "Generate new level", this.width / 2, 40, 16777215);
		super.drawScreen(var1, var2);
	}
}
