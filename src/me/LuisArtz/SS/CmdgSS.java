package me.LuisArtz.SS;

import com.connorlinfoot.titleapi.TitleAPI;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static me.LuisArtz.SS.Main.effect;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.PlayerInventory;
import static me.LuisArtz.SS.TBanConsole.getBanned;
import org.bukkit.entity.Player;
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
                    p.sendMessage("§3§l§k||§b§lGood§f§lSS §b§lv6.0§3§l§k||");
                    p.sendMessage(" ");
                    p.sendMessage("§b/gss §3reload");
                    p.sendMessage("§b/gss §3start <player> <AnyDesk/Skype/Ts>");
                    p.sendMessage("§b/gss §3leave");
                    p.sendMessage("§b/gss §3ban <reason>");
                    p.sendMessage("§b/gss §3inv");
                    p.sendMessage("§b/gss §3tp");
                    p.sendMessage("§b/gss §3unban <player>");
                    p.sendMessage("§b/gss §3banlist");
                    p.sendMessage("§b/gss §3checkupdate");
                    p.sendMessage("§b/gss §3author");
                    p.sendMessage("§b/admit §3(Only for ss players)");
                    p.sendMessage(" ");
                    p.sendMessage("§3§l§k||§3§lBy §b§lLuis§f§lArtz§3§l§k||");
                    p.sendMessage(" ");
                }else{
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        plugin.reloadBans();
                        plugin.sfFile = new File(plugin.getDataFolder(), "tempsfdata.yml");
                        plugin.ssFile = new File(plugin.getDataFolder(), "tempssdata.yml");
                        if (plugin.sfFile.exists()){
                            plugin.sfFile.delete();
                            plugin.getSF().options().copyDefaults();
                            plugin.saveSF();
                        }
                        if (plugin.ssFile.exists()){
                            plugin.ssFile.delete();
                            plugin.getSS().options().copyDefaults();
                            plugin.saveSS();
                        }
                        TitleAPI.sendTitle(p, 20, 40, 20, plugin.getConfig().getString("Titles.reload.ti"), plugin.getConfig().getString("Titles.reload.subt"));
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
                                        String time = bans.getString("BanManager."+banned+".time");
                                        p.sendMessage(plugin.getConfig().getString("BanListFormat").replaceAll("&", "§").replace("%reason%", reason).replace("%staff%", staff).replace("%date%", date).replace("%time%", time).replace("%player%", banned));
                                        return true;
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
                            FileConfiguration ss = plugin.getSS();
                            FileConfiguration sf = plugin.getSF();
                            if (args.length > 2) {
                                if (p2 == null){
                                    p.sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                                }else{
                                    if ((ss.contains("ActualSS")) && (ss.contains("ActualSS."+p2.getName()))){
                                        p.sendMessage(plugin.getConfig().getString("NowInSS").replaceAll("&", "§"));
                                    }else{
                                        if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))) {
                                            p.sendMessage(plugin.getConfig().getString("StaffInSS").replaceAll("&", "§"));
                                        }else{
                                            String date = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
                                            String user = "";
                                            for (int i = 2; i < args.length; i++) {
                                                user = user + args[i] + ' ';
                                            }
                                            TitleAPI.sendTitle(p2, 20, 40, 20, plugin.getConfig().getString("Titles.start.ti"), plugin.getConfig().getString("Titles.start.subt").replace("%contact%", user));
                                            TitleAPI.sendTitle(p, 20, 40, 20, plugin.getConfig().getString("Titles.start.staff"), plugin.getConfig().getString("Titles.start.stsubt").replace("%player%", p2.getName()));
                                            ss.set("ActualSS."+p2.getName()+".staff", p.getName());
                                            ss.set("ActualSS."+p2.getName()+".started", date);
                                            ss.set("ActualSS."+p2.getName()+".uuid", p2.getUniqueId().toString());
                                            ss.set("ActualSS."+p2.getName()+".contact", user);
                                            sf.set("StaffSS."+p.getName()+".user", p2.getName());
                                            sf.set("StaffSS."+p.getName()+".started", date);
                                            sf.set("StaffSS."+p.getName()+".uuid", p.getUniqueId().toString());
                                            sf.set("StaffSS."+p.getName()+".contact", user);
                                            p2.addPotionEffect(effect);
                                            Bukkit.broadcastMessage(plugin.getConfig().getString("StartSSGlobal").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", p2.getName()).replace("%user%", user));
                                            p2.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", p2.getName()).replace("%user%", user).replace("%started%", date));
                                            p.teleport(p2.getLocation());
                                            PlayerInventory pynv = p2.getInventory();
                                            p.openInventory(pynv);
                                            return true;
                                        }
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
                            if (args.length > 1) {
                                String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(System.currentTimeMillis()));
                                FileConfiguration ss = plugin.getSS();
                                FileConfiguration sf = plugin.getSF();
                                if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))){
                                    for (Player pz : Bukkit.getServer().getOnlinePlayers()) {
                                        Player pb = pz;
                                        if (pb.getName().equals(sf.getString("StaffSS."+p.getName()+".user"))) {
                                            pb.removePotionEffect(PotionEffectType.BLINDNESS);
                                            String razon = "";
                                            for (int i = 1; i < args.length; i++) {
                                                razon = razon + args[i] + ' ';
                                            }
                                                    ss.set("ActualSS."+pb.getName(), null);
                                                    sf.set("StaffSS."+p.getName(), null);
                                                    plugin.saveSS();
                                                    plugin.saveSF();
                                            TitleAPI.sendTitle(p, 20, 40, 20, plugin.getConfig().getString("Titles.ban.ti").replace("%player%", pb.getName()), plugin.getConfig().getString("Titles.ban.subt").replace("%reason%", razon));
                                            Bukkit.broadcastMessage(plugin.getConfig().getString("BanFormat").replaceAll("&", "§").replace("%player%", p.getName()).replace("%reason%", razon).replace("%bannedpl%", pb.getName()).replace("%time%", "Permanently"));
                                            pb.kickPlayer(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", p.getName()).replace("%reason%", razon).replace("%date%", date).replace("%time%", "Permanently"));
                                            pb.removePotionEffect(PotionEffectType.BLINDNESS);
                                            FileConfiguration bans = plugin.getBans();
                                            if (bans.contains("BanManager")){
                                                if (!bans.contains("BanManager."+pb.getName())){
                                                    bans.set("BanManager."+pb.getName()+".reason", razon);
                                                    bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                                                    bans.set("BanManager."+pb.getName()+".staff", p.getName());
                                                    bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                                                    bans.set("BanManager."+pb.getName()+".date", date);
                                                    bans.set("BanManager."+pb.getName()+".time", "Permanently");
                                                    plugin.saveBans();
                                                    return true;
                                                }
                                            }else{
                                                bans.set("BanManager."+pb.getName()+".reason", razon);
                                                bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                                                bans.set("BanManager."+pb.getName()+".staff", p.getName());
                                                bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                                                bans.set("BanManager."+pb.getName()+".date", date);
                                                bans.set("BanManager."+pb.getName()+".time", "Permanently");
                                                ss.set("ActualSS."+pb.getName(), null);
                                                sf.set("StaffSS."+p.getName(), null);
                                                plugin.saveBans();
                                                plugin.saveSS();
                                                plugin.saveSF();
                                                return true;
                                            }
                                        }
                                    }
                                }else{
                                    p.sendMessage(plugin.getConfig().getString("StaffNotInSS").replaceAll("&", "§"));
                                }
                            }else{
                                p.sendMessage(" ");
                                p.sendMessage("§3Use: §b/gss ban (reason)");
                                p.sendMessage(" ");
                            }
                        }catch(Exception e){
                            p.sendMessage(" ");
                            p.sendMessage("§3Use: §b/gss ban (reason)");
                            p.sendMessage(" ");
                        }
                    }else if (args[0].equalsIgnoreCase("leave")) {
                        try{
                            FileConfiguration ss = plugin.getSS();
                            FileConfiguration sf = plugin.getSF();
                            if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))){
                                for (Player pd : Bukkit.getServer().getOnlinePlayers()) {
                                    if (pd.getName().equals(sf.getString("StaffSS."+p.getName()+".user"))) {
                                        pd.sendMessage(plugin.getConfig().getString("LeaveSS_Player").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", pd.getName()));
                                        pd.setCanPickupItems(true);
                                        Bukkit.broadcastMessage(plugin.getConfig().getString("LeaveSSGlobal").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", pd.getName()));
                                        pd.removePotionEffect(PotionEffectType.BLINDNESS);
                                        TitleAPI.sendTitle(pd, 20, 40, 20, plugin.getConfig().getString("Titles.stop.ti"), plugin.getConfig().getString("Titles.stop.subt"));
                                        TitleAPI.sendTitle(p, 20, 40, 20, plugin.getConfig().getString("Titles.stop.staff").replace("%player%", pd.getName()), plugin.getConfig().getString("Titles.stop.stsubt").replace("%player%", pd.getName()));
                                        ss.set("ActualSS."+pd.getName(), null);
                                        sf.set("StaffSS."+p.getName(), null);
                                        plugin.saveSS();
                                        plugin.saveSF();
                                        return true;
                                    }
                                }
                            }else{
                                p.sendMessage(plugin.getConfig().getString("StaffNotInSS").replaceAll("&", "§"));
                                return true;
                            }
                        }catch(Exception ax){
                            p.sendMessage("§cHere is an error!");
                            p.sendMessage("§cCheck the console!");
                            ax.printStackTrace();
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
                                            if(getBanned().containsKey(args[1].toLowerCase())){
                                                getBanned().remove(args[1].toLowerCase());
                                                plugin.getServer().getPlayerExact(args[1]).setBanned(false);
                                                return true;
                                            }
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
                        FileConfiguration sf = plugin.getSF();
                        if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))){
                            for (Player pd : Bukkit.getServer().getOnlinePlayers()) {
                                if (pd.getName().equals(sf.getString("StaffSS."+p.getName()+".user"))) {
                                    PlayerInventory pinv = pd.getInventory();
                                    p.openInventory(pinv);
                                    p.sendMessage(plugin.getConfig().getString("OpeningInventory").replaceAll("&", "§").replace("%player%", p.getName()).replace("%plss%", pd.getName()));
                                    return true;
                                }
                            }
                        }else{
                            p.sendMessage(plugin.getConfig().getString("StaffNotInSS").replaceAll("&", "§"));
                        }
                    }else if (args[0].equalsIgnoreCase("tp")) {
                        FileConfiguration sf = plugin.getSF();
                        if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))){
                            for (Player pd : Bukkit.getServer().getOnlinePlayers()) {
                                if (pd.getName().equals(sf.getString("StaffSS."+p.getName()+".user"))) {
                                    p.teleport(pd.getLocation());
                                    p.sendMessage(plugin.getConfig().getString("Teleport").replaceAll("&", "§").replace("%plss%", pd.getName()));
                                    return true;
                                }
                            }
                        }else{
                            p.sendMessage(plugin.getConfig().getString("StaffNotInSS").replaceAll("&", "§"));
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
                                p.sendMessage("§3By §b§lLuis§f§lArtz");
                                p.sendMessage("§3======================");
                               p.sendMessage(" ");
                            }else{
                               p.sendMessage(" ");
                               p.sendMessage("§3======================");
                               p.sendMessage("§bGood§fSS");
                               p.sendMessage("§a§l§k||§2Now updated!§a§l§k||");
                               p.sendMessage("§3Last version: §b"+plugin.getVersion());
                               p.sendMessage("§2Thanks for use §bGood§fSS");
                               p.sendMessage("§3By §b§lLuis§f§lArtz");
                               p.sendMessage("§3======================");
                               p.sendMessage(" ");
                               return true;
                            }
                        }else{
                            p.sendMessage(plugin.getConfig().getString("PermissionMsg").replaceAll("&", "§"));
                        }
                    }else if(args[0].equalsIgnoreCase("author")){
                        p.sendMessage(" ");
                        p.sendMessage("§3=========================");
                        p.sendMessage("§3§lAuthor:");
                        p.sendMessage("§b§lLuis§f§lArtz");
                        p.sendMessage("§6§lSpigot:");
                        p.sendMessage("§ehttps://www.spigotmc.org/members/306157/");
                        p.sendMessage("§3§lTwitter:");
                        p.sendMessage("§bhttps://twitter.com/xLuisArtz/");
                        p.sendMessage("§1§lFaceBook:");
                        p.sendMessage("§9https://www.facebook.com/messages/t/zZLuisArtzZz/");
                        p.sendMessage("§3=========================");
                        p.sendMessage(" ");
                    }else{
                        p.sendMessage(" ");
                        p.sendMessage("§3§l§k||§b§lGood§f§lSS §b§lv6.0§3§l§k||");
                        p.sendMessage(" ");
                        p.sendMessage("§b/gss §3reload");
                        p.sendMessage("§b/gss §3start <player> <AnyDesk/Skype/Ts>");
                        p.sendMessage("§b/gss §3leave");
                        p.sendMessage("§b/gss §3ban <reason>");
                        p.sendMessage("§b/gss §3inv");
                        p.sendMessage("§b/gss §3tp");
                        p.sendMessage("§b/gss §3unban <player>");
                        p.sendMessage("§b/gss §3checkupdate");
                        p.sendMessage("§b/gss §3banlist");
                        p.sendMessage("§b/gss §3author");
                        p.sendMessage("§b/admit §3(Only for ss players)");
                        p.sendMessage(" ");
                        p.sendMessage("§3§l§k||§3§lBy §b§lLuis§f§lArtz§3§l§k||");
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
