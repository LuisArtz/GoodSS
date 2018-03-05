package me.LuisArtz.SS;

import java.text.SimpleDateFormat;
import java.util.Date;
import static me.LuisArtz.SS.Main.effect;
import static me.LuisArtz.SS.Main.frozen;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class CmdgSS implements CommandExecutor {
    public Main plugin;
  
    public CmdgSS(Main plugin) {
       this.plugin = plugin;
    }
      
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    if (cmd.getName().equalsIgnoreCase("gss")) {
        if (sender instanceof Player) {
                Player p = (Player)sender;
            if (p.hasPermission("gss.usage")) {
                if (args.length == 0) {
                    p.sendMessage(" ");
                    p.sendMessage("§b§l§k||§3§lGoodSS v3.0§b§l§k||");
                    p.sendMessage(" ");
                    p.sendMessage("§b/gss §3reload");
                    p.sendMessage("§b/gss §3start (player) (your username)");
                    p.sendMessage("§b/gss §3leave (player)");
                    p.sendMessage("§b/gss §3ban (player) (reason)");
                    p.sendMessage("§b/gss §3inv (player)");
                    p.sendMessage("§b/gss §3tp (player)");
                    p.sendMessage("§b/gss §3unban (player)");
                    p.sendMessage("§b/gss §3banlist");
                    p.sendMessage("§b/gss §3checkupdate");
                    p.sendMessage("§b/gss §3author");
                    p.sendMessage(" ");
                    p.sendMessage("§b§l§k||§3§lBy LuisArtz§b§l§k||");
                    p.sendMessage(" ");
                }else{
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        plugin.reloadBans();
                        p.sendMessage(plugin.getConfig().getString("Reload").replaceAll("&", "§"));
                    }else if (args[0].equalsIgnoreCase("banlist")){
                        if (p.hasPermission("gss.banlist")){
                            FileConfiguration bans = plugin.getBans();
                            if (bans.contains("BanManager")) {
                                for (String banned : bans.getConfigurationSection("BanManager").getKeys(false)){
                                    if (bans.getString("BanManager").equals("{}")) {
                                        p.sendMessage(plugin.getConfig().getString("NotBans").replaceAll("&", "§"));
                                    }else{
                                        String reason = bans.getString("BanManager."+banned+".reason");
                                        String staff = bans.getString("BanManager."+banned+".staff");
                                        String date = bans.getString("BanManager."+banned+".date");
                                        p.sendMessage(plugin.getConfig().getString("BanListFormat").replaceAll("&", "§").replace("%player%", banned).replace("%reason%", reason).replace("%staff%", staff).replace("%date%", date));
                                    }
                                }
                            }else{
                                p.sendMessage(plugin.getConfig().getString("NotBans").replaceAll("&", "§"));
                            }
                        }else{
                            p.sendMessage(plugin.getConfig().getString("PermissionMsg").replaceAll("&", "§"));
                        }
                    }else if (args[0].equalsIgnoreCase("start")){
                        try{
                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                            if (args.length > 2) {
                                if (p2 == null){
                                    p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                                }else{
                                    if (frozen.contains(p2.getName())) {
                                        p.sendMessage(plugin.getConfig().getString("NowInSS").replaceAll("&", "§"));
                                    }else{
                                        String user = "";
                                        for (int i = 2; i < args.length; i++) {
                                            user = user + args[i] + ' ';
                                        }
                                        frozen.add(p2.getName());
                                        p2.addPotionEffect(effect);
                                        Bukkit.broadcastMessage(plugin.getConfig().getString("StartSSGlobal").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", p2.getName()).replace("%user%", user));
                                        p2.sendMessage(plugin.getConfig().getString("StartSS_Player").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", p2.getName()).replace("%user%", user));
                                        p.teleport(p2.getLocation());
                                        PlayerInventory pynv = p2.getInventory();
                                        p.openInventory(pynv);
                                    }
                                }
                            }else{
                                p.sendMessage(" ");
                                p.sendMessage("§3Use: §b/gss start (player) (Skype/AnyDesk/Ts user)");
                                p.sendMessage(" ");
                            }
                        }catch(Exception x) {
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss start (player) (Skype/AnyDesk/Ts user)");
                            p.sendMessage(" ");
                        }
                    }else if (args[0].equalsIgnoreCase("ban")) {
                        try{
                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                            if (args.length > 2) {
                                if (p2 == null){
                                    p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                                }else{
                                    Player pb = Bukkit.getServer().getPlayer(args[1]);
                                    if (frozen.contains(pb.getName())) {
                                        String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(System.currentTimeMillis()));
                                        frozen.remove(pb.getName());
                                        pb.removePotionEffect(PotionEffectType.BLINDNESS);
                                        String razon = "";
                                        for (int i = 2; i < args.length; i++) {
                                            razon = razon + args[i] + ' ';
                                        }
                                            Bukkit.broadcastMessage(plugin.getConfig().getString("BanFormat").replaceAll("&", "§").replace("%player%", p.getName()).replace("%reason%", razon).replace("%bannedpl%", pb.getName()));
                                            pb.kickPlayer(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", p.getName()).replace("%reason%", razon).replace("%date%", date));
                                            pb.removePotionEffect(PotionEffectType.BLINDNESS);
                                        FileConfiguration bans = plugin.getBans();
                                        if (bans.contains("BanManager")){
                                            if (!bans.contains("BanManager."+p2.getName()) | !bans.getStringList("BanListHistory").contains(p2.getName())){
                                                bans.set("BanManager."+pb.getName()+".reason", razon);
                                                bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                                                bans.set("BanManager."+pb.getName()+".staff", p.getName());
                                                bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                                                bans.set("BanManager."+pb.getName()+".date", date);
                                                plugin.saveBans();
                                            }
                                        }else{
                                            bans.set("BanManager."+p2.getName()+".reason", razon);
                                            bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                                            bans.set("BanManager."+p2.getName()+".staff", p.getName());
                                            bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                                            bans.set("BanManager."+pb.getName()+".date", date);
                                            plugin.saveBans();
                                        }
                                    } else {
                                        p.sendMessage(plugin.getConfig().getString("NotInSS").replaceAll("&", "§"));
                                    }
                                }
                            }else{
                                p.sendMessage(" ");
                                p.sendMessage("§3Use: §b/gss ban (player) (reason)");
                                p.sendMessage(" ");
                            }
                        }catch(Exception e){
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss ban (player) (reason)");
                            p.sendMessage(" ");
                        }
                    }else if (args[0].equalsIgnoreCase("leave")) {
                        try{
                            Player pd = Bukkit.getPlayer(args[1]);
                            if (args.length > 1) {    
                                if (pd == null) {
                                    p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                                }else{
                                    if (frozen.contains(pd.getName())){
                                        frozen.remove(pd.getName());
                                        pd.sendMessage(plugin.getConfig().getString("LeaveSS_Player").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", pd.getName()));
                                        pd.setCanPickupItems(true);
                                        Bukkit.broadcastMessage(plugin.getConfig().getString("LeaveSSGlobal").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", pd.getName()));
                                        pd.removePotionEffect(PotionEffectType.BLINDNESS);
                                    }else{
                                        p.sendMessage(plugin.getConfig().getString("NotInSS").replaceAll("&", "§"));
                                    }   
                                }
                            }else{
                                p.sendMessage(" ");
                                p.sendMessage("§3Use: §b/gss leave (player)");
                                p.sendMessage(" ");
                            }
                        }catch(Exception ax){
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss leave (player)");
                            p.sendMessage(" ");
                            
                        }
                    }else if (args[0].equalsIgnoreCase("unban")) {
                        if (args.length > 1) {
                            if (p.hasPermission("gss.unban")){
                                FileConfiguration bans = plugin.getBans();
                                if (bans.contains("BanManager")){
                                    if (bans.contains("BanManager."+args[1])) {
                                        p.sendMessage(plugin.getConfig().getString("UnbanCorrect").replaceAll("&", "§"));
                                        bans.set("BanManager."+args[1]+"", null);
                                        plugin.saveBans();
                                        if (bans.getString("BanManager") == null) {
                                            bans.set("BanManager", null);
                                            plugin.saveBans();
                                        }
                                    }else{
                                        p.sendMessage(plugin.getConfig().getString("UnbanIncorrect").replaceAll("&", "§"));
                                    }
                                }else{
                                    p.sendMessage(plugin.getConfig().getString("NotBans").replaceAll("&", "§"));
                                }
                            }else{
                                p.sendMessage(plugin.getConfig().getString("PermissionMsg").replaceAll("&", "§"));
                            }
                        }else{
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss unban (player)");
                            p.sendMessage(" ");
                        }
                    }else if (args[0].equalsIgnoreCase("inv")) {
                        if (args.length > 1) {
                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                            if (p2 == null) {
                                p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                            }else{
                                if (frozen.contains(p2.getName())) {
                                    PlayerInventory pinv = p.getInventory();
                                    p.openInventory(pinv);
                                    p.sendMessage(plugin.getConfig().getString("OpeningInventory").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", p2.getName()));   
                                }else{
                                    p.sendMessage(plugin.getConfig().getString("NotInSS").replaceAll("&", "§"));
                                }
                            }
                        }else{
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss inv (player)");
                            p.sendMessage(" ");
                        }
                    }else if (args[0].equalsIgnoreCase("tp")) {
                        if (args.length > 1) {
                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                            if (p2 == null) {
                                p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                            }else{
                                if (frozen.contains(p2.getName())) {
                                    p.teleport(p2.getLocation());
                                    p.sendMessage(plugin.getConfig().getString("Teleport").replaceAll("&", "§").replace("%plss%", p2.getName()));
                                }else{
                                    p.sendMessage(plugin.getConfig().getString("NotInSS").replaceAll("&", "§"));
                                }
                            }
                        }else{
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss tp (player)");
                            p.sendMessage(" ");
                        }
                    }else if(args[0].equalsIgnoreCase("checkupdate")){
                        if (p.hasPermission("gss.updates")) {
                            if (!plugin.getVersion().equals(plugin.getLastV())) {
                               p.sendMessage(" ");
                                p.sendMessage("§3======================");
                                p.sendMessage("§bGood§fSS");
                                p.sendMessage("§a§l§k||§2New update!§a§l§k||");
                                p.sendMessage("§3Your version: §b"+plugin.getVersion());
                                p.sendMessage("§3New version: §b"+plugin.getLastV());
                                p.sendMessage("§2Download the new version here!: §ahttps://www.spigotmc.org/resources/53515/");
                                p.sendMessage("§3By §bLuis§lArtz");
                                p.sendMessage("§3======================");
                               p.sendMessage(" ");
                            }else{
                               p.sendMessage(" ");
                               p.sendMessage("§3======================");
                               p.sendMessage("§bGood§fSS");
                               p.sendMessage("§a§l§k||§2Now updated!§a§l§k||");
                               p.sendMessage("§3Last version: §b"+plugin.getVersion());
                               p.sendMessage("§2Thanks for use §bGood§fSS");
                               p.sendMessage("§3By §bLuis§lArtz");
                               p.sendMessage("§3======================");
                               p.sendMessage(" ");
                            }
                        }else{
                            p.sendMessage(plugin.getConfig().getString("PermissionMsg").replaceAll("&", "§"));
                        }
                    }else if(args[0].equalsIgnoreCase("author")){
                        p.sendMessage(" ");
                        p.sendMessage("§2=========================");
                        p.sendMessage("§3§lAuthor:");
                        p.sendMessage("§b§lLuis§f§lArtz");
                        p.sendMessage("§6§lSpigot:");
                        p.sendMessage("§ehttps://www.spigotmc.org/members/306157/");
                        p.sendMessage("§3§lTwitter:");
                        p.sendMessage("§bhttps://twitter.com/xLuisArtz/");
                        p.sendMessage("§1§lFaceBook:");
                        p.sendMessage("§9https://www.facebook.com/messages/t/zZLuisArtzZz/");
                        p.sendMessage("§2=========================");
                        p.sendMessage(" ");
                    }else{
                        p.sendMessage(" ");
                        p.sendMessage("§b§l§k||§3§lGoodSS v3.0§b§l§k||");
                        p.sendMessage(" ");
                        p.sendMessage("§b/gss §3reload");
                        p.sendMessage("§b/gss §3start (player) (your username)");
                        p.sendMessage("§b/gss §3leave (player)");
                        p.sendMessage("§b/gss §3ban (player) (reason)");
                        p.sendMessage("§b/gss §3inv (player)");
                        p.sendMessage("§b/gss §3tp (player)");
                        p.sendMessage("§b/gss §3unban (player)");
                        p.sendMessage("§b/gss §3checkupdate");
                        p.sendMessage("§b/gss §3banlist");
                        p.sendMessage("§b/gss §3author");
                        p.sendMessage(" ");
                        p.sendMessage("§b§l§k||§3§lBy LuisArtz§b§l§k||");
                        p.sendMessage(" ");
                    }
                }
            }else{
                p.sendMessage(plugin.getConfig().getString("PermissionMsg").replaceAll("&", "§"));
            }
        }else{
            Bukkit.getServer().getConsoleSender().sendMessage("You cant usage the command in the console");
        }
    }
    return false;
    }
}
