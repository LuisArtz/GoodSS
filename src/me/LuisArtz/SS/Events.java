package me.LuisArtz.SS;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {
    public Main plugin;
    public Events(Main plugin) {
       this.plugin = plugin;
    }
    @EventHandler
    public void playerIsBanned(PlayerPreLoginEvent e) {
        FileConfiguration bans = plugin.getBans();
        if (bans.contains("BanManager")) {
            if (bans.contains("BanManager."+e.getName())) {
                if (bans.getString("BanManager."+e.getName()+".UUID").equalsIgnoreCase(e.getUniqueId().toString())){
                    e.setKickMessage(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", bans.getString("BanManager."+e.getName()+".staff")).replace("%reason%", bans.getString("BanManager."+e.getName()+".reason")).replace("%date%", bans.getString("BanManager."+e.getName()+".date")));
                    e.disallow(PlayerPreLoginEvent.Result.KICK_BANNED, e.getKickMessage());
                }
            }
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Main.frozen.contains(p.getName())) {
            e.setTo(e.getFrom());
            p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", p.getName()).replace("%displayer%", p.getDisplayName()));
            p.setCanPickupItems(true);
        }
    }
    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        if (Main.frozen.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", e.getPlayer().getName()));
        }
    }
    @EventHandler
    public void inSS(AsyncPlayerChatEvent e) {
        String pn = e.getPlayer().getName();
        Player p = e.getPlayer();
        if (Main.frozen.contains(pn)) {
            e.setCancelled(true);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("gss.usage")) {
                    staff.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                }
            }
            if (!p.hasPermission("gss.usage")) {
                p.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
            }
        }
    }
    @EventHandler
    public void onZZ(PlayerQuitEvent e) {
        String pn = e.getPlayer().getName();
        Player p = e.getPlayer();
        String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(System.currentTimeMillis()));
        if (Main.frozen.contains(pn)) {
            Main.frozen.remove(pn);
            FileConfiguration bans = plugin.getBans();
            Player pb = e.getPlayer();
            Bukkit.broadcastMessage(plugin.getConfig().getString("BanFormat").replaceAll("&", "§").replace("%bannedpl%", p.getName()).replace("%displayer%", p.getDisplayName()).replace("%player%", plugin.getConfig().getString("AutoBanAuthor")).replace("%reason%", plugin.getConfig().getString("AutoBanReason")));
            if (bans.contains("BanManager")){
                if (!bans.contains("BanManager."+pb.getName())){
                    bans.set("BanManager."+pb.getName()+".reason", plugin.getConfig().getString("AutoBanReason"));
                    bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                    bans.set("BanManager."+pb.getName()+".staff", plugin.getConfig().getString("AutoBanAuthor"));
                    bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                    bans.set("BanManager."+pb.getName()+".date", date);
                    Bukkit.getConsoleSender().sendMessage("Player added correctly :D thanks for use GoodSS.");
                    plugin.saveBans();
                }else{
                    Bukkit.getConsoleSender().sendMessage("Player is now added.");
                }
            }else{
                bans.set("BanManager."+pb.getName()+".reason", plugin.getConfig().getString("AutoBanReason"));
                bans.set("BanManager."+pb.getName()+".UUID", pb.getUniqueId().toString());
                bans.set("BanManager."+pb.getName()+".staff", plugin.getConfig().getString("AutoBanAuthor"));
                bans.set("BanManager."+pb.getName()+".ip", pb.getAddress().toString());
                bans.set("BanManager."+pb.getName()+".date", date);
                plugin.saveBans();
                Bukkit.getConsoleSender().sendMessage("Player added correctly :D thanks for use GoodSS.");
            }
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
    @EventHandler
    public void giveDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Main.frozen.contains(e.getEntity().getName())) {
                e.setCancelled(true);
                e.getEntity().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", e.getEntity().getName()));
            }
        }
    }
    @EventHandler
    public void getDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                if (Main.frozen.contains(e.getEntity().getName())){
                e.setCancelled(true);
                e.getDamager().sendMessage(plugin.getConfig().getString("PlayerInSS").replaceAll("&", "§").replace("%plss%", e.getEntity().getName()));
                }
            }
        }
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (Main.frozen.contains(e.getPlayer().getName())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", e.getPlayer().getName()));
        }
    }
    @EventHandler
    public void onPutBlock(BlockPlaceEvent e) {
        if (Main.frozen.contains(e.getPlayer().getName())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", e.getPlayer().getName()));
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (Main.frozen.contains(e.getPlayer().getName())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", e.getPlayer().getName()));
        }
    }
}