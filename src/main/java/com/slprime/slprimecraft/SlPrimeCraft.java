package com.slprime.slprimecraft;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.passive.EntityAnimal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "slprimecraft", version = Tags.VERSION, name = "SlPrimeCraft", acceptedMinecraftVersions = "[1.7.10]")
public class SlPrimeCraft {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderLiving(RenderLivingEvent.Specials.Pre event) {
        if (event.entity instanceof EntityAnimal && ((EntityAnimal) event.entity).hasCustomNameTag()) {
            ((EntityAnimal) event.entity).setAlwaysRenderNameTag(true);
            passSpecialRender((EntityAnimal) event.entity, event.x, event.y, event.z);
            event.setCanceled(true);
        }
    }

    protected void passSpecialRender(EntityAnimal entity, double x, double y, double z) {
        float scale = 1.6F;
        float scaleFactor = 0.016666668F * scale;
        double distanceSq = entity.getDistanceSqToEntity(RenderManager.instance.livingPlayer);
        float nameTagRange = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK
                : RendererLivingEntity.NAME_TAG_RANGE;

        if (distanceSq < (double) (nameTagRange * nameTagRange)) {
            String name = entity.func_145748_c_().getUnformattedText();
            FontRenderer fontRenderer = RenderManager.instance.getFontRenderer();

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x, (float) y + entity.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-scaleFactor, -scaleFactor, scaleFactor);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontRenderer.drawString(name, -fontRenderer.getStringWidth(name) / 2, 0, 16777215);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }
}
