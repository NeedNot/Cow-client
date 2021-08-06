package net.neednot.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.mixin.networking.accessor.ConnectScreenAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.neednot.Gui;
import net.neednot.GuiScreen;
import net.minecraft.client.gui.screen.ConnectScreen;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.neednot.ExampleMod;

import java.util.Iterator;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {

    @Shadow private ButtonWidget buttonResetDemo;

    @Shadow @Final private static Identifier EDITION_TITLE_TEXTURE;

    @Shadow @Nullable private String splashText;

    @Shadow @Final private boolean doBackgroundFade;

    @Shadow private long backgroundFadeStart;

    @Shadow private int copyrightTextX;

    @Shadow private int copyrightTextWidth;

    @Shadow protected abstract boolean areRealmsNotificationsEnabled();

    @Shadow private Screen realmsNotificationGui;

    @Shadow @Final private RotatingCubeMapRenderer backgroundRenderer;

    @Shadow @Final private static Identifier PANORAMA_OVERLAY;

    @Shadow @Final private static Identifier MINECRAFT_TITLE_TEXTURE;

    @Shadow @Final private boolean isMinceraft;

    protected MainMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal")
    public void addCustomButton(int y, int spacingY, CallbackInfo info) {

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 20 + -16 +60, 98, 20, new LiteralText("Modern SMP"), (buttonWidgetx) -> {

            Screen screen = new TitleScreen();
            MinecraftClient client = MinecraftClient.getInstance();
            ServerAddress serverAddress = new ServerAddress("184.95.44.42", 25598);
            ServerInfo sinfo = new ServerInfo("Modern SMP", "184.95.44.42:25598", false);
            ConnectScreen.connect(screen, client, serverAddress, sinfo);
        }));
    }
    @Inject(method = "init", at = @At("RETURN"))
    private void modifySize(CallbackInfo ci) {
        for (ClickableWidget button : Screens.getButtons(this)) {
            if (button.getMessage().getString().equals(I18n.translate("menu.singleplayer"))) {
                button.setWidth(98);
                button.x = this.width / 2 - 100;
                button.y = this.height / 4 + 20 + -16 + 35;
            }
            if (button.getMessage().getString().equals(I18n.translate("menu.online"))) {
                button.setWidth(98);
                button.x = this.width / 2 + 2;
                button.y = this.height / 4 + 20 + -16 + 60;
            }
            if (button.getMessage().getString().equals(I18n.translate("menu.multiplayer"))) {
                button.setWidth(0);
                button.x = this.width / 2 +2;
                button.y = this.height / 4 + 20 + -16 + 4000;
            }
        }
    }

    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        this.backgroundRenderer.render(delta, MathHelper.clamp(f, 0.0F, 1.0F));

        int j = this.width / 2 - 137;

        Identifier CUSTOM_FARM_BACKGROUND = new Identifier("cowclient","textures/gui/farm_background.png");

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CUSTOM_FARM_BACKGROUND);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
        drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MINECRAFT_TITLE_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, g);
            if (this.isMinceraft) {
                this.drawWithOutline(j, 30, (x, y) -> {
                    this.drawTexture(matrices, x + 0, y, 0, 0, 99, 44);
                    this.drawTexture(matrices, x + 99, y, 129, 0, 27, 44);
                    this.drawTexture(matrices, x + 99 + 26, y, 126, 0, 3, 44);
                    this.drawTexture(matrices, x + 99 + 26 + 3, y, 99, 0, 26, 44);
                    this.drawTexture(matrices, x + 155, y, 0, 45, 155, 44);
                });
            } else {
                this.drawWithOutline(j, 30, (x, y) -> {
                    this.drawTexture(matrices, x + 0, y, 0, 0, 155, 44);
                    this.drawTexture(matrices, x + 155, y, 0, 45, 155, 44);
                });
            }

            RenderSystem.setShaderTexture(0, EDITION_TITLE_TEXTURE);
            drawTexture(matrices, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);

            String string = "Minecraft " + SharedConstants.getGameVersion().getName();
            if (this.client.isDemo()) {
                string = string + " Demo";
            }
            if (this.client.isModded()) {
                string = I18n.translate("Cow Client " + SharedConstants.getGameVersion().getName() + " running on Fabric");
            }

            drawStringWithShadow(matrices, this.textRenderer, string, 2, this.height - 10, 16777215 | l);
            drawStringWithShadow(matrices, this.textRenderer, "Copyright Mojang AB. Do not distribute!", this.copyrightTextX, this.height - 10, 16777215 | l);
            if (mouseX > this.copyrightTextX && mouseX < this.copyrightTextX + this.copyrightTextWidth && mouseY > this.height - 10 && mouseY < this.height) {
                fill(matrices, this.copyrightTextX, this.height - 1, this.copyrightTextX + this.copyrightTextWidth, this.height, 16777215 | l);
            }

            Iterator var12 = this.children().iterator();

            while(var12.hasNext()) {
                Element element = (Element)var12.next();
                if (element instanceof ClickableWidget) {
                    ((ClickableWidget)element).setAlpha(g);
                }
            }

            super.render(matrices, mouseX, mouseY, delta);
            if (this.areRealmsNotificationsEnabled() && g >= 1.0F) {
                this.realmsNotificationGui.render(matrices, mouseX, mouseY, delta);
            }

        }
    }
}
