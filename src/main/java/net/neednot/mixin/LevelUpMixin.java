package net.neednot.mixin;


import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayerEntity.class)
public abstract class LevelUpMixin {
    public boolean ran = false  ;

    @Shadow @Final protected MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "setExperience")
    public void setExperience(float progress, int total, int level, CallbackInfo info) {
        int past = client.player.experienceLevel;
        int now = level;

        if (ran == false) {
            ran = true;
        }
        else {
            if (now > past) {
                client.player.sendChatMessage("I leveled up to level " + now);
                past = now;
            }
        }
    }
}