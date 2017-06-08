package dev.aura.bungeechat.module;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.command.SocialSpyCommand;
import net.md_5.bungee.api.ProxyServer;

public class SpyModule extends Module {
    private SocialSpyCommand socialSpyCommand;

    @Override
    public String getName() {
        return "Spy";
    }

    @Override
    public void onEnable() {
        socialSpyCommand = new SocialSpyCommand(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(BungeeChat.getInstance(), socialSpyCommand);
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getPluginManager().unregisterCommand(socialSpyCommand);
    }
}