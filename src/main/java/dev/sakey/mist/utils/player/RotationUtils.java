package dev.sakey.mist.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.*;

public class RotationUtils {

    static Minecraft mc = Minecraft.getMinecraft();

    public static float getGCD()
    {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;

        return f1 * 0.15F;
    }

    public static float fixRotation(final float rotation)
    {
        return Math.round(rotation / getGCD()) * getGCD();
    }

    public static float[] getBlockRotations(int x, int y, int z, EnumFacing face) {
        EntityEgg entity = new EntityEgg(mc.theWorld);

        entity.posX = (double)x + 0.5;
        entity.posY = (double)y + 0.5;
        entity.posZ = (double)z + 0.5;

        entity.posX += (double)face.getDirectionVec().getX() * 0.25;
        entity.posY += (double)face.getDirectionVec().getY() * 0.25;
        entity.posZ += (double)face.getDirectionVec().getZ() * 0.25;

        return getRotations(entity);
    }

    public static float[] getRotations(Entity e) {
        return new float[] { getYaw(e) + mc.thePlayer.rotationYaw, getPitch(e) + mc.thePlayer.rotationPitch };
    }

    public static float getYaw(Entity entity) {
        double xDiff = entity.posX - mc.thePlayer.posX;
        double zDiff = entity.posZ - mc.thePlayer.posZ;
        double yaw;

        if(zDiff < 0 && xDiff < 0) {
            yaw = 90 + Math.toDegrees(Math.atan(zDiff / xDiff));
        }
        else if(zDiff < 0 && xDiff > 0) {
            yaw = -90 + Math.toDegrees(zDiff / xDiff);
        }
        else {
            yaw = Math.toDegrees(-Math.atan(xDiff / zDiff));
        }

        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yaw));
    }

    public static float getPitch(Entity entity) {
        double xDiff = entity.posX - mc.thePlayer.posX;
        double zDiff = entity.posZ - mc.thePlayer.posZ;
        double yDiff = entity.posY - 1.6 + (double) entity.getEyeHeight() - mc.thePlayer.posY;

        double xzDiff = (double) MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        double pitch = -Math.toDegrees(Math.atan(yDiff / xzDiff));

        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitch);
    }

    public static float[] getRotationsSmooth(Entity e, double smoothing) {
        float yaw = getRotations(e)[0],
                pitch = getRotations(e)[1];

        return new float[] {
                (float) (mc.thePlayer.rotationYaw + (yaw - mc.thePlayer.rotationYaw) / smoothing),
                (float) (mc.thePlayer.rotationPitch + (pitch - mc.thePlayer.rotationPitch) / smoothing)
        };
    }

    public static float clampRotation() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float n = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (!(Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0f)) return rotationYaw * ((float)Math.PI / 180);
        rotationYaw += 90.0f * n;
        return rotationYaw * ((float)Math.PI / 180);
    }

    public static void setRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    public static void setRotations(float[] rotations) {
        RotationUtils.setRotations(rotations[0], rotations[1]);
    }

    public static Vec3 getVecRotations(float yaw, float pitch) {
        double d = Math.cos(Math.toRadians(-yaw) - Math.PI);
        double d1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
        double d2 = -Math.cos(Math.toRadians(-pitch));
        double d3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3(d1 * d2, d3, d * d2);
    }

}
