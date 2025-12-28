package net.minecraft.client;

import java.time.LocalDate;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiVerySmallButton;
import net.minecraft.client.gui.GuiLoadLevel;
import net.minecraft.client.gui.GuiNewLevel;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class GuiMainTitle extends GuiScreen {
	private float updateCounter = 0.0F;
        public LocalDate date = LocalDate.now();
        int month = date.getMonthValue(); 
        int day = date.getDayOfMonth(); 
        public String splashString = "Pre-beta!";

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	protected final void keyTyped(char var1, int var2) {
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
		this.controlList.add(new GuiVerySmallButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
                this.controlList.add(new GuiVerySmallButton(4, this.width / 2 - -4, this.height / 4 + 120 + 12, "Quit Game"));
                ((GuiButton)this.controlList.get(2)).enabled = false;
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected final void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}
                
                if(var1.id == 4) {
			this.mc.exit();
		}
                
		if(this.mc.session != null && var1.id == 2) {
			this.mc.displayGuiScreen(new GuiLoadLevel(this));
		}

	}

	public final void drawScreen(int var1, int var2) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.setColorOpaque_I(16777215);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var4 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		GL11.glScalef(var4, var4, var4);
                if ((month == 12 && day == 31) || (month == 1 && day == 1)) {
                    this.splashString = "Happy new year!";
        }
                if ((month == 12 && day == 25)) {
                    this.splashString = "Merry X-mas!";
        }
		drawCenteredString(this.fontRenderer, splashString, 0, -8, 16776960);
		GL11.glPopMatrix();
		String var5 = "Copyright Mojang Specifications. Do not distribute.";
                String var6 = "Mod by dndv.";
		b(this.fontRenderer, var5, this.width - this.fontRenderer.getStringWidth(var5) - 2, this.height - 10, 16777215);
                b(this.fontRenderer, var6, this.width - this.fontRenderer.getStringWidth(var6) - 2, this.height - 20, 16777215);
		super.drawScreen(var1, var2);
	}
}
