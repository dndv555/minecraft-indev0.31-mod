package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public final class GuiControls extends GuiScreen {
	private GuiScreen parentScreen;
	private String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;
        private float updateCounter = 0.0F;

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	public GuiControls(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public final void initGui() {
		for(int var1 = 0; var1 < this.options.keyBindings.length; ++var1) {
			this.controlList.add(new GuiSmallButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.options.setKeyBindingString(var1)));
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton var1) {
		for(int var2 = 0; var2 < this.options.keyBindings.length; ++var2) {
			((GuiButton)this.controlList.get(var2)).displayString = this.options.setKeyBindingString(var2);
		}

		if(var1.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = var1.id;
			var1.displayString = "> " + this.options.setKeyBindingString(var1.id) + " <";
		}
	}

	protected final void keyTyped(char var1, int var2) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, var2);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.setKeyBindingString(this.buttonId);
			this.buttonId = -1;
		} else {
			super.keyTyped(var1, var2);
		}
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
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2);
	}
}
