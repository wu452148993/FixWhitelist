package com.github.wulf;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

public class CommandWhitelistAdd implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (args.length != 1) {
			sender.sendMessage("指令格式不正确！");
		} else {
			UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0]).getBytes(Charsets.UTF_8));
			GameProfile gameprofile = new GameProfile(offlineUUID, args[0]);
			//Player player = (Player)sender;
	//		Server Server = ((Player)sender).getServer();
			Server Server = sender.getServer();
			//net.minecraft.server.v1_13_R1.MinecraftServer server =  ((org.bukkit.craftbukkit.v1_13_R1.CraftServer)s).getHandle();
			//net.minecraft.server.v1_13_R1.EntityPlayer EntityPlaye = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
			// EntityPlaye.getWorldServer().getServer();
			Object PlayerList= NMSUtil.getNMSPlayerList(Server);
			//Object PlayerList = MethodUtil.invokeMethod(NMSUtil.method_CraftServer_getHandle,NMSServer);
			Object WhiteListEntr = NMSUtil.newInstance(NMSUtil.clazz_WhiteListEntry,GameProfile.class,gameprofile);
			Object Whitelist = MethodUtil.invokeMethod(NMSUtil.method_PlayerList_getWhitelist,PlayerList);
			MethodUtil.invokeMethod(NMSUtil.method_JsonList_add,Whitelist,WhiteListEntr);
			sender.sendMessage("设置成功");
			//Object WhiteListEntry = MethodUtil.invokeMethod()
			//PlayerList PlayerList =  ((org.bukkit.craftbukkit.v1_13_R1.CraftServer)s).getHandle();
			//PlayerList.getWhitelist().add(new WhiteListEntry(gameprofile));
		}
        // If the player (or console) uses our command correct, we can return true
        return true;
    }

}
