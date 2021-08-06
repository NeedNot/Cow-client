package net.neednot.mixin;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.fabric.mixin.networking.accessor.ConnectScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.neednot.Gui;
import net.neednot.GuiScreen;
import net.minecraft.client.gui.screen.ConnectScreen;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {

    @Shadow private ButtonWidget buttonResetDemo;

    protected MainMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal")
    public void addCustomButton(int y, int spacingY, CallbackInfo info) {

        BooleanConsumer customer = new BooleanConsumer() {
            @Override
            public void accept(boolean t) {

            }
        };
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 20 + -16 +60, 98, 20, new LiteralText("Modern SMP"), (buttonWidgetx) -> {

            Screen screen = new TitleScreen();
            MinecraftClient client = MinecraftClient.getInstance();
            ServerAddress serverAddress = new ServerAddress("184.95.44.42", 25598);
            ServerInfo sinfo = new ServerInfo("Modern SMP", "184.95.44.42:25598", false);
            ConnectScreen.connect(screen, client, serverAddress, sinfo);
        }));
    }
}