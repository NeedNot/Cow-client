package net.neednot.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatScannerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("TAIL"))
    public void onChatMessage(Text message , int messageId , CallbackInfo ci) {
        if (message.getString().contains("Is Mordor city good?")) {
            MinecraftClient.getInstance().player.sendChatMessage("no");
        }
        if (!message.getString().contains("<") && !message.getString().contains("*")) {
            if (message.getString().contains("tried to swim in lava")) {
                say("You should swim in water not lava");
            }

            if (message.getString().contains("hit the ground too hard") || message.getString().contains("fell from a high place")) {
                say("Looks like someone forgot their wings");
            }

            if (message.getString().contains("fell off a ladder") || message.getString().contains("fell off some vines") || message.getString().contains("fell off some weeping vines") || message.getString().contains("fell off some twisting vines") || message.getString().contains("fell off scaffolding") || message.getString().contains("fell while climbing")) {
                say("Sweaty hands make you slip");
            }

            if (message.getString().contains("fell out of the world") || message.getString().contains("didn't want to live in the same world as")) {
                say("You must be on land too live");
            }

            if (message.getString().contains("experienced kinetic energy")) {
                say("Look where ya going");
            }

            if (message.getString().contains("was stung to death")) {
                say("Mud helps bee stings");
            }

            if (message.getString().contains("was poked to death by a sweet berry bush")) {
                say("I know you like berries but don't get too close");
            }

            if (message.getString().contains("was shot by")) {
                say("Aim bot");
            }

            if (message.getString().contains("was squashed by a falling anvil")) {
                say("The sky is falling! The sky is falling!");
            }

            if (message.getString().contains("went off with a bang")) {
                say("This isn't the 4th of July is it?");
            }

            if (message.getString().contains("was blown up by a creeper")) {
                say("Aww man!");
            }

            if (message.getString().contains("was impaled by")) {
                say("King Neptune is that you?!?");
            }

            if (message.getString().contains("withered away")) {
                say("Spooky");
            }

            if (message.getString().contains("was pricked to death")) {
                say("Prick!");
            }

            if (message.getString().contains("was squished too much") || message.getString().contains("was squished by")) {
                say("Man they pack those airline seats in tight");
            }

            if (message.getString().contains("drowned")) {
                say("Sorry you can't live underwater");
            }

            if (message.getString().contains("was squashed by a falling block")) {
                say("Heads up!");
            }

            if (message.getString().contains("discovered the floor was lava") || message.getString().contains("walked into danger zone due to")) {
                say("Floor is Lava!");
            }
            if (message.getString().contains("went up in flames") || message.getString().contains("walked into fire whilst fighting") || message.getString().contains("burned to death") || message.getString().contains("was burnt to a crisp whilst fighting")) {
                say("Hot! Hot! Hot! Dead...");
            }

            if (message.getString().contains("suffocated in a wall")) {
                say("You can't hide in the wall");
            }

            if (message.getString().contains("was struck by lightning")) {
                say("I mean... that's so rare that's cool");
            }

            if (message.getString().contains("was killed by [Intentional Game Design]")) {
                say("L");
            }
            if (message.getString().contains("froze to death")) {
                say("You could have asked for some hot coco");
            }

            if (message.getString().contains("was skewered by a falling stalactite")) {
                say("The sky is falling");
            }
            if (message.getString().contains("was impaled on a stalagmite")) {
                say("Those are spiky");
            }
            if (message.getString().contains("died from dehydration")) {
                say("Wait that's illegal");
            }

            if (message.getString().contains("whilst fighting")) {
                String key = "fighting";
                checkMob(message.getString(), key);

            }
            if (message.getString().contains(" by ")) {
                String key = "by";
                checkMob(message.getString(), key);

            }
            if (message.getString().contains("walked into danger zone due to")) {
                String key = "to";
                checkMob(message.getString(), key);

            }
            if (message.getString().contains("to escape")) {
                String key = "escape";
                checkMob(message.getString(), key);
            }
            if (message.getString().contains("didn't want to live in the same world as")) {
                String key = "as";
                checkMob(message.getString(), key);
            }
        }
    }
    public void say(String text) {
        MinecraftClient.getInstance().player.sendChatMessage(text);
    }
    public void checkMob(String msg, String key) {
        String[] list = msg.split(" ");
        boolean go = false;
        String mob = "";


        for (String s : list) {

            if (go) {
                mob = s;
                go = false;
            }
            if (s.equalsIgnoreCase(key)) {
                go = true;
            }
        }

        boolean found = false;

        for (EntityType e : Registry.ENTITY_TYPE) {
            if (e.getName().getString().equalsIgnoreCase(mob)) {
                found = true;
                break;
            }
        }

        if (!found) {
            say("CALL THE COPS! " + msg.substring(0, msg.indexOf(' '))  +" WAS MURDERED!");
        }
        else {
            say("Don't trust the mobs");
        }

    }
}