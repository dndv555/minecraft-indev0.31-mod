package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiLoadLevel extends GuiScreen {
	private GuiScreen parent;
	public String status = "";
	protected String title = "Load/Save Level";
	public boolean frozen = false;
	private File selectedFile;
	private boolean isSaving = false;
        private float updateCounter = 0.0F;
        
	public GuiLoadLevel(GuiScreen var1) {
		this.parent = var1;
	}
	public final void updateScreen() {
        this.updateCounter += 0.01F;
		if (this.selectedFile != null) {
			if (isSaving) {
				if (!this.selectedFile.getName().endsWith(".mclevel")) {
					this.selectedFile = new File(this.selectedFile.getAbsolutePath() + ".mclevel");
				}
				this.saveLevel(this.selectedFile);
			} else {
				if (!this.selectedFile.getName().endsWith(".mclevel")) {
					this.selectedFile = new File(this.selectedFile.getAbsolutePath() + ".mclevel");
				}
				this.openLevel(this.selectedFile);
			}
			this.selectedFile = null;
		}
    }	
	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 48, "Load from file..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 24, "Save to file..."));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = true;
		((GuiButton)this.controlList.get(1)).enabled = true;
		((GuiButton)this.controlList.get(2)).enabled = true;
	}	
	protected final void actionPerformed(GuiButton var1) {
		if(!this.frozen) {
			if(var1.enabled) {
				if(var1.id == 1) {
					this.isSaving = false;
					this.frozen = true;
					GuiLevelDialog var2 = new GuiLevelDialog(this, false);
					var2.setDaemon(true);
					var2.start();
				} else if(var1.id == 2) {
					this.isSaving = true;
					this.frozen = true;
					GuiLevelDialog var2 = new GuiLevelDialog(this, true);
					var2.setDaemon(true);
					var2.start();
				} else if(var1.id == 3) {
					this.mc.displayGuiScreen(this.parent);
				}
			}
		}
	}	
	protected FileDialog d() {
		return new FileDialog((Dialog)null, "Select file", 0);
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
		drawCenteredString(this.fontRenderer, "Load or Save Level", 
			this.width / 2, this.height / 2 - 80, 16777215);
		if (this.status != null && !this.status.isEmpty()) {
			drawCenteredString(this.fontRenderer, this.status, 
				this.width / 2, this.height / 2 + 50, 0xFFCC00);
		}
		super.drawScreen(var1, var2);
	}	
	protected void openLevel(File var1) {
		try {
			this.status = "Loading world...";
			FileInputStream var4 = new FileInputStream(var1);
			World var2 = (new PlayerLoader(this.mc, this.mc.loadingScreen)).load(var4);
			var4.close();
			if (this.mc != null) {
				this.mc.setLevel(var2);
				this.mc.displayGuiScreen(null);
			}	
		} catch (IOException var3) {
			var3.printStackTrace();
			this.status = "Failed to load world from file";
			this.frozen = false;
		}
	}	
	protected void saveLevel(File var1) {
		try {
			this.status = "Saving world...";
			if (this.mc == null) {
				this.status = "Game not started!";
				this.frozen = false;
				return;
			}
			World currentWorld = this.mc.theWorld;
			
			if (currentWorld == null) {
				this.status = "No active world to save! Start a game first.";
				this.frozen = false;
				return;
			}			
			if (!var1.getName().endsWith(".mclevel")) {
				var1 = new File(var1.getAbsolutePath() + ".mclevel");
			}	
			try {
				FileOutputStream fos = new FileOutputStream(var1);
				PlayerLoader playerLoader = new PlayerLoader(this.mc, this.mc.loadingScreen);
				playerLoader.save(currentWorld, fos);
				fos.close();
				
				long fileSize = var1.length();
				this.status = "World saved successfully!";
				
			} catch (Exception e) {
				e.printStackTrace();
				this.status = "Failed to save world: " + e.getMessage();
				this.frozen = false;
				return;
			}
			GuiLoadLevelStatus resetTask = new GuiLoadLevelStatus(this);
			java.util.Timer timer = new java.util.Timer();
			timer.schedule(resetTask, 3000);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.status = "Failed to save world: " + e.getMessage();
			this.frozen = false;
		}
	}
	static File a(GuiLoadLevel var0, File var1) {
		return var0.selectedFile = var1;
	}
	static boolean a(GuiLoadLevel var0, boolean var1) {
		return var0.frozen = false;
	}
}