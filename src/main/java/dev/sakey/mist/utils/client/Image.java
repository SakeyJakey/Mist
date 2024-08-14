package dev.sakey.mist.utils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Image {

    ResourceLocation file;
    int w, h;

    public Image(ResourceLocation file, int width, int height){
    }

    public void drawImageTopLeft(int offsetX, int offsetY, float scaleX, float scaleY){
        Minecraft.getMinecraft().getTextureManager().bindTexture(file);
        Gui.drawModalRectWithCustomSizedTexture(offsetX, offsetY, 0, 0, (int)(w * scaleX), (int)(h * scaleY), w * scaleX, h * scaleY);
    }

    public void drawImageTopLeft(int offsetX, int offsetY){
        Minecraft.getMinecraft().getTextureManager().bindTexture(file);
        Gui.drawModalRectWithCustomSizedTexture(offsetX, offsetY, 0, 0, w, h, w, h);
    }

    public void drawImageTopLeft(){
        Minecraft.getMinecraft().getTextureManager().bindTexture(file);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, w, h, w, h);
    }

}
