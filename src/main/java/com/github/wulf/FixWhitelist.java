package com.github.wulf;

import org.bukkit.plugin.java.JavaPlugin;

public class FixWhitelist extends JavaPlugin{
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	this.getCommand("whitelistol").setExecutor(new CommandWhitelistAdd());
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
}
