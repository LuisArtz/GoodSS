package me.LuisArtz.SS;

import com.connorlinfoot.titleapi.TitleAPI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class TBanConsole implements CommandExecutor {
    public Main plugin;
  
    public TBanConsole(Main plugin) {
       this.plugin = plugin;
    }
    String staff = "";
    String user = "";
    String razon = "";
    String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(System.currentTimeMillis()));
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gstban")){
            FileConfiguration sf = plugin.getSF();
            FileConfiguration bans = plugin.getBans();
            FileConfiguration ss = plugin.getSS();
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (p.hasPermission("gss.tempban")) {
                    if ((sf.contains("StaffSS")) && (sf.contains("StaffSS."+p.getName()))){
                        for (Player pz : Bukkit.getServer().getOnlinePlayers()) {
                            Player pb = pz;
                            if (pb.getName().equals(sf.getString("StaffSS."+p.getName()+".user"))) {
                                staff = p.getName();
                                user = pb.getName();
                                pb.removePotionEffect(PotionEffectType.BLINDNESS);
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gstban " + user + " " + staff + " " + plugin.getConfig().getString("AdmitBan.int") + " " + plugin.getConfig().getString("AdmitBan.type") + " " + plugin.getConfig().getString("AdmitBan.reason"));
                                bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                                bans.set("BanManager."+pb.getName()+".staff", p.getName());
                                bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                                bans.set("BanManager."+pb.getName()+".date", date);
                                TitleAPI.sendTitle(p, 20, 40, 20, plugin.getConfig().getString("Titles.admit.ban").replace("%player%", pb.getName()), plugin.getConfig().getString("Titles.admit.bansubt").replace("%time%", plugin.getConfig().getString("AdmitBan.int")+plugin.getConfig().getString("AdmitBan.type")));
                                plugin.saveBans();
                                return true;
                            }
                        }
                    }else{
                        p.sendMessage(plugin.getConfig().getString("StaffNotInSS").replaceAll("&", "§"));
                    }
                }
            }else{
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if(target==null || !target.isOnline()){
                    Bukkit.getConsoleSender().sendMessage(plugin.getConfig().getString("PlayerDoesNotExist").replaceAll("&", "§"));
                    return true;
                }
                long endOfBan = System.currentTimeMillis() + AdmitBanUnit.getTicks(args[3], Integer.parseInt(args[2]));
                long now = System.currentTimeMillis();
                long diff = endOfBan - now;
                if(diff > 0){
                    setBanned(target.getName().toLowerCase(), endOfBan);
                    for (int i = 4; i < args.length; i++) {
                        razon = razon + args[i] + ' ';
                    }
                    bans.set("BanManager."+target.getName()+".reason", razon);
                    bans.set("BanManager."+target.getName()+".time", plugin.getConfig().getString("AdmitBan.int")+" "+ plugin.getConfig().getString("AdmitBan.type"));
                    ss.set("ActualSS."+user, null);
                    sf.set("StaffSS."+staff, null);plugin.saveSS();
                    plugin.saveSF();
                    Main.save();
                    Bukkit.broadcastMessage(plugin.getConfig().getString("BanFormat").replaceAll("&", "§").replace("%player%", staff).replace("%reason%", razon).replace("%bannedpl%", user).replace("%time%", bans.getString("BanManager."+user+".time")));
                    target.kickPlayer(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", staff).replace("%reason%", razon).replace("%date%", date).replace("%time%", bans.getString("BanManager."+user+".time")));
                    return true;
                }else{
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: Unit or time not valid.");
                    return true;
                }
            }
        }
        return false;
    }
    public static HashMap<String, Long> getBanned(){
        return Main.banned;
    }
    public void setBanned(String name, long end){
        getBanned().put(name, end);
    }
    public static String getMSG(long endOfBan){
        String message = "";
        long now = System.currentTimeMillis();
        long diff = endOfBan - now;
        int seconds = (int) (diff / 1000);
        if(seconds >= 60*60*24){
            int days = seconds / (60*60*24);
            seconds = seconds % (60*60*24);
            message += days + " Day(s) ";
        }
        if(seconds >= 60*60){
            int hours = seconds / (60*60);
            seconds = seconds % (60*60);
            message += hours + " Hour(s)";
        }
        if(seconds >= 60){
            int min = seconds / 60;
            seconds = seconds % 60;
            message += min + " Minute(s) ";
        }
        if(seconds >= 0){
            message += seconds + " Second(s) ";
        }
        return message;
    }
}
