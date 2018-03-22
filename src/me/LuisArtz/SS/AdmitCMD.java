package me.LuisArtz.SS;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AdmitCMD implements CommandExecutor {
    public Main plugin;
  
    public AdmitCMD(Main plugin) {
       this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("admit")){
            if (sender instanceof Player) {
                Player p = (Player)sender;
                FileConfiguration ss = plugin.getSS();
                if (ss.contains("ActualSS."+p.getName())) {
                    for (Player sz : Bukkit.getServer().getOnlinePlayers()){
                        if (sz.getName().equals(ss.getString("ActualSS."+p.getName()+".staff"))){
                            TextComponent cmp = new TextComponent();
                            cmp.setText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("AdmitBan.staffmsg").replace("%player%", p.getName())));
                            cmp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("AdmitBan.staffhover").replace("%type%", plugin.getConfig().getString("AdmitBan.type")).replace("%int%", plugin.getConfig().getString("AdmitBan.int")))).create()));
                            cmp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND ,"/gstban"));
                            sz.spigot().sendMessage(cmp);
                            return true;
                        }
                    }
                }else{
                    p.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&cYou arent in SS"));
                    return true;
                }
            }else{
                Bukkit.getConsoleSender().sendMessage("You cant execute this command on the console");
            }
        }
        return false;
    }
}