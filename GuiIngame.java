package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;

public final class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	private int updateCounter = 0;

	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	public final void renderGameOverlay(float var1) {
		ScaledResolution var2 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var3 = var2.getScaledWidth();
		int var12 = var2.getScaledHeight();
		FontRenderer var4 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		InventoryPlayer var5 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(var3 / 2 - 91, var12 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(var3 / 2 - 91 - 1 + var5.currentItem * 20, var12 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		this.drawTexturedModalRect(var3 / 2 - 7, var12 / 2 - 7, 0, 0, 16, 16);
		boolean var13 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			var13 = false;
		}

		int var6 = this.mc.thePlayer.health;
		int var7 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int var8;
		int var10;
		int var15;
		if(this.mc.playerController.shouldDrawHUD()) {
			for(var8 = 0; var8 < 10; ++var8) {
				byte var9 = 0;
				if(var13) {
					var9 = 1;
				}

				var10 = var3 / 2 - 91 + (var8 << 3);
				int var11 = var12 - 32;
				if(var6 <= 4) {
					var11 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(var10, var11, 16 + var9 * 9, 0, 9, 9);
				if(var13) {
					if((var8 << 1) + 1 < var7) {
						this.drawTexturedModalRect(var10, var11, 70, 0, 9, 9);
					}

					if((var8 << 1) + 1 == var7) {
						this.drawTexturedModalRect(var10, var11, 79, 0, 9, 9);
					}
				}

				if((var8 << 1) + 1 < var6) {
					this.drawTexturedModalRect(var10, var11, 52, 0, 9, 9);
				}

				if((var8 << 1) + 1 == var6) {
					this.drawTexturedModalRect(var10, var11, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial()) {
				var8 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				var15 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var8;

				for(var10 = 0; var10 < var8 + var15; ++var10) {
					if(var10 < var8) {
						this.drawTexturedModalRect(var3 / 2 - 91 + (var10 << 3), var12 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(var3 / 2 - 91 + (var10 << 3), var12 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		for(var8 = 0; var8 < 9; ++var8) {
			var15 = var3 / 2 - 90 + var8 * 20 + 2;
			var10 = var12 - 16 - 3;
			ItemStack var14 = this.mc.thePlayer.inventory.mainInventory[var8];
			if(var14 != null) {
				float var16 = (float)var14.animationsToGo - var1;
				if(var16 > 0.0F) {
					GL11.glPushMatrix();
					float var17 = 1.0F + var16 / 5.0F;
					GL11.glTranslatef((float)(var15 + 8), (float)(var10 + 12), 0.0F);
					GL11.glScalef(1.0F / var17, (var17 + 1.0F) / 2.0F, 1.0F);
					GL11.glTranslatef((float)(-(var15 + 8)), (float)(-(var10 + 12)), 0.0F);
				}

				itemRenderer.a(this.mc.renderEngine, var14, var15, var10);
				if(var16 > 0.0F) {
					GL11.glPopMatrix();
				}

				itemRenderer.a(this.mc.fontRenderer, var14, var15, var10);
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);
		if(this.mc.options.showFPS) {
                        var4.drawStringWithShadow("Minecraft 0.31 (modded)", 2, 2, 16777215);
			var4.drawStringWithShadow(this.mc.debug, 2, 12, 16777215);
                        var4.drawStringWithShadow("Java: " + System.getProperty("java.version"), 2, 22, 16777215);
                        var4.drawStringWithShadow("VM: " + System.getProperty("java.vm.name") + ", " + System.getProperty("java.vm.info"), 2, 32, 16777215);
                        var4.drawStringWithShadow("LWJGL: " + Sys.getVersion(), 2, 42, 16777215);
                        var4.drawStringWithShadow("OpenGL: " + GL11.glGetString(7937) + ", " + GL11.glGetString(7938), 2, 52, 16777215);
                        var4.drawStringWithShadow("X: " + this.mc.thePlayer.posX + " Y: " + this.mc.thePlayer.posY + " Z: " + this.mc.thePlayer.posZ, 2, 62, 16777215);
		}

		for(var10 = 0; var10 < this.chatMessageList.size() && var10 < 10; ++var10) {
			if(((ChatLine)this.chatMessageList.get(var10)).updateCounter < 200) {
				this.chatMessageList.get(var10);
				var4.drawStringWithShadow((String)null, 2, var12 - 8 - var10 * 9 - 20, 16777215);
			}
		}

	}

	public final void addChatMessage() {
		++this.updateCounter;

		for(int var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
			++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
		}

	}
}
