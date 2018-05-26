package me.LuisArtz.SS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import static me.LuisArtz.SS.TBanConsole.getMSG;
import static me.LuisArtz.SS.TBanConsole.getBanned;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Events implements Listener {

    public static ArrayList<String> g = new ArrayList<String>();
    public Main plugin;

    public Events(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void updateAlert(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("gss.updates")) {
            if (!plugin.getVersion().equals(plugin.getLastV())) {
                p.sendMessage(" ");
                p.sendMessage("§3======================");
                p.sendMessage("§bGood§fSS");
                p.sendMessage("§a§l§k||§2New update!§a§l§k||");
                p.sendMessage("§3Your version: §b" + plugin.getVersion());
                p.sendMessage("§3New version: §b" + plugin.getLastV());
                p.sendMessage("§2Download the new version here!: §ahttps://www.spigotmc.org/resources/53515/");
                p.sendMessage("§3By §b§lLuis§f§lArtz");
                p.sendMessage("§3======================");
                p.sendMessage(" ");
            }
        }
        if ((p.getName().equals("LuisArtz") || (p.getName().equals("LuisPintoGamerYT")) || (p.getName().equals("xLuisArtz")))) {
            p.sendMessage("§aEste servidor está usando tu plugin §bGood§fSS");
            p.sendMessage("§aVersion: §b" + plugin.getVersion());
        }
    }

    @EventHandler
    public void playerIsBanned(PlayerLoginEvent e) {
        FileConfiguration bans = plugin.getBans();
        if (bans.contains("BanManager")) {
            if (bans.contains("BanManager." + e.getPlayer().getName())) {
                e.setKickMessage(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", bans.getString("BanManager." + e.getPlayer().getName() + ".staff")).replace("%reason%", bans.getString("BanManager." + e.getPlayer().getName() + ".reason")).replace("%date%", bans.getString("BanManager." + e.getPlayer().getName() + ".date")).replace("%time%", bans.getString("BanManager." + e.getPlayer().getName() + ".time")));
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, e.getKickMessage());
            }
        }
        Player player = e.getPlayer();
        if (getBanned().containsKey(player.getName().toLowerCase())) {
            if (getBanned().get(player.getName().toLowerCase()) != null) {
                long endOfBan = getBanned().get(player.getName().toLowerCase());
                long now = System.currentTimeMillis();
                long diff = endOfBan - now;
                if (diff <= 0) {
                    getBanned().remove(player.getName().toLowerCase());
                    bans.set("BanManager." + e.getPlayer().getName(), null);
                    plugin.saveBans();
                    e.allow();
                } else {
                    try {
                        e.setKickMessage(plugin.getConfig().getString("BanFormatDisconnect").replaceAll("&", "§").replace("%player%", bans.getString("BanManager." + e.getPlayer().getName() + ".staff")).replace("%reason%", bans.getString("BanManager." + e.getPlayer().getName() + ".reason")).replace("%date%", bans.getString("BanManager." + e.getPlayer().getName() + ".date")).replace("%time%", getMSG(endOfBan)));
                        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, e.getKickMessage());
                    } catch (Exception es) {
                        es.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        String c = String.valueOf((boolean) plugin.getConfig().getBoolean("MovementDisable"));
        if (!c.equalsIgnoreCase("false")) {
            Player p = e.getPlayer();
            FileConfiguration ss = plugin.getSS();
            if (ss.contains("ActualSS." + p.getName())) {
                String staff = ss.getString("ActualSS." + p.getName() + ".staff");
                String contact = ss.getString("ActualSS." + p.getName() + ".contact");
                String pl = p.getName();
                String started = ss.getString("ActualSS." + p.getName() + ".started");
                e.setTo(e.getFrom());
                p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%displayer%", p.getDisplayName()).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
                p.setCanPickupItems(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        FileConfiguration ss = plugin.getSS();
        Player p = e.getPlayer();
        if (ss.contains("ActualSS." + p.getName())) {
            e.setCancelled(true);
            String staff = ss.getString("ActualSS." + p.getName() + ".staff");
            String contact = ss.getString("ActualSS." + p.getName() + ".contact");
            String pl = p.getName();
            String started = ss.getString("ActualSS." + p.getName() + ".started");
            p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%displayer%", p.getDisplayName()).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
        }
    }

    @EventHandler
    public void inSS(AsyncPlayerChatEvent e) {
        String pn = e.getPlayer().getName();
        Player p = e.getPlayer();
        FileConfiguration ss = plugin.getSS();
        FileConfiguration sf = plugin.getSF();
        if (ss.contains("ActualSS." + pn)) {
            for (Player sz : Bukkit.getServer().getOnlinePlayers()) {
                if (sz.getName().equals(ss.getString("ActualSS." + pn + ".staff"))) {
                    if (!e.isCancelled()) {
                        e.setCancelled(true);
                    }
                    sz.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                }
            }
            if (!sf.contains("StaffSS." + pn)) {
                p.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                if (!e.isCancelled()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onZZ(PlayerQuitEvent e) {
        String pn = e.getPlayer().getName();
        Player p = e.getPlayer();
        FileConfiguration ss = plugin.getSS();
        FileConfiguration sf = plugin.getSF();
        String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(System.currentTimeMillis()));
        if (ss.contains("ActualSS." + pn)) {
            FileConfiguration bans = plugin.getBans();
            Player pb = e.getPlayer();
            Bukkit.broadcastMessage(plugin.getConfig().getString("BanFormat").replaceAll("&", "§").replace("%bannedpl%", p.getName()).replace("%displayer%", p.getDisplayName()).replace("%player%", ss.getString("ActualSS." + pn + ".staff")).replace("%reason%", plugin.getConfig().getString("AutoBanReason")).replace("%time%", "Permanently"));
            if (bans.contains("BanManager")) {
                if (!bans.contains("BanManager." + pb.getName())) {
                    p.removePotionEffect(PotionEffectType.BLINDNESS);
                    p.setCanPickupItems(true);
                    bans.set("BanManager." + pb.getName() + ".reason", plugin.getConfig().getString("AutoBanReason"));
                    bans.set("BanManager." + pb.getName() + ".UUID", pb.getUniqueId().toString());
                    bans.set("BanManager." + pb.getName() + ".staff", ss.getString("ActualSS." + p.getName() + ".staff"));
                    bans.set("BanManager." + pb.getName() + ".ip", pb.getAddress().toString());
                    bans.set("BanManager." + pb.getName() + ".date", date);
                    bans.set("BanManager." + pb.getName() + ".time", "Permanent");
                    Bukkit.getConsoleSender().sendMessage("Player added correctly :D thanks for use GoodSS.");
                    sf.set("StaffSS." + ss.getString("ActualSS." + p.getName() + ".staff"), null);
                    ss.set("ActualSS." + p.getName(), null);
                    plugin.saveSF();
                    plugin.saveSS();
                    plugin.saveBans();
                } else {
                    Bukkit.getConsoleSender().sendMessage("Player is now added.");
                }
            } else {
                bans.set("BanManager." + pb.getName() + ".reason", plugin.getConfig().getString("AutoBanReason"));
                bans.set("BanManager." + pb.getName() + ".UUID", pb.getUniqueId().toString());
                bans.set("BanManager." + pb.getName() + ".staff", ss.getString("ActualSS." + p.getName() + ".staff"));
                bans.set("BanManager." + pb.getName() + ".ip", pb.getAddress().toString());
                bans.set("BanManager." + pb.getName() + ".date", date);
                bans.set("BanManager." + pb.getName() + ".time", "Permanent");
                Bukkit.getConsoleSender().sendMessage("Player added correctly :D thanks for use GoodSS.");
                sf.set("StaffSS." + ss.getString("ActualSS." + p.getName() + ".staff"), null);
                ss.set("ActualSS." + p.getName(), null);
                plugin.saveSF();
                plugin.saveSS();
                plugin.saveBans();
            }
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    @EventHandler
    public void giveDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            FileConfiguration ss = plugin.getSS();
            if (ss.contains("ActualSS." + e.getEntity().getName())) {
                e.setCancelled(true);
                String staff = ss.getString("ActualSS." + e.getEntity().getName() + ".staff");
                String contact = ss.getString("ActualSS." + e.getEntity().getName() + ".contact");
                String pl = e.getEntity().getName();
                String started = ss.getString("ActualSS." + e.getEntity().getName() + ".started");
                e.getEntity().sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
            }
        }
    }

    @EventHandler
    public void getDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                FileConfiguration ss = plugin.getSS();
                if (ss.contains("ActualSS." + e.getEntity().getName())) {
                    e.setCancelled(true);
                    e.getDamager().sendMessage(plugin.getConfig().getString("PlayerInSS").replaceAll("&", "§").replace("%plss%", e.getEntity().getName()));
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        FileConfiguration ss = plugin.getSS();
        Player p = e.getPlayer();
        if (ss.contains("ActualSS." + p.getName())) {
            e.setCancelled(true);
            String staff = ss.getString("ActualSS." + p.getName() + ".staff");
            String contact = ss.getString("ActualSS." + p.getName() + ".contact");
            String pl = p.getName();
            String started = ss.getString("ActualSS." + p.getName() + ".started");
            p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%displayer%", p.getDisplayName()).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
        }
    }

    @EventHandler
    public void onPutBlock(BlockPlaceEvent e) {
        FileConfiguration ss = plugin.getSS();
        Player p = e.getPlayer();
        if (ss.contains("ActualSS." + p.getName())) {
            e.setCancelled(true);
            String staff = ss.getString("ActualSS." + p.getName() + ".staff");
            String contact = ss.getString("ActualSS." + p.getName() + ".contact");
            String pl = p.getName();
            String started = ss.getString("ActualSS." + p.getName() + ".started");
            p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%displayer%", p.getDisplayName()).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        FileConfiguration ss = plugin.getSS();
        Player p = e.getPlayer();
        if (ss.contains("ActualSS." + p.getName())) {
            e.setCancelled(true);
            String staff = ss.getString("ActualSS." + p.getName() + ".staff");
            String contact = ss.getString("ActualSS." + p.getName() + ".contact");
            String pl = p.getName();
            String started = ss.getString("ActualSS." + p.getName() + ".started");
            p.sendMessage(plugin.getConfig().getString("SSMessageEvent").replaceAll("&", "§").replace("%plss%", pl).replace("%displayer%", p.getDisplayName()).replace("%player%", staff).replace("%user%", contact).replace("%started%", started));
        }
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("CommandsBlockeds")) {
            String pn = e.getPlayer().getName();
            Player p = e.getPlayer();
            FileConfiguration ss = plugin.getSS();
            FileConfiguration sf = plugin.getSF();
            if (ss.contains("ActualSS." + pn)) {
                List<String> d = config.getStringList("CommandsBlockeds");
                if (d.contains("*")) {
                    if (!e.getMessage().startsWith("/admit")) {
                        if (!e.isCancelled()) {
                            e.setCancelled(true);
                            for (Player sz : Bukkit.getServer().getOnlinePlayers()) {
                                if (sz.getName().equals(ss.getString("ActualSS." + pn + ".staff"))) {
                                    if (!e.isCancelled()) {
                                        e.setCancelled(true);
                                    }
                                    sz.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                                }
                            }
                            if (!sf.contains("StaffSS." + pn)) {
                                p.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                                if (!e.isCancelled()) {
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                } else {
                    if (e.getMessage().startsWith("/" + d)) {
                        if (!e.getMessage().startsWith("/admit")) {
                            if (!e.isCancelled()) {
                                for (Player sz : Bukkit.getServer().getOnlinePlayers()) {
                                    if (sz.getName().equals(ss.getString("ActualSS." + pn + ".staff"))) {
                                        if (!e.isCancelled()) {
                                            e.setCancelled(true);
                                        }
                                        sz.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                                    }
                                }
                                if (!sf.contains("StaffSS." + pn)) {
                                    p.sendMessage(plugin.getConfig().getString("PlayerSSChatFormat").replaceAll("&", "§").replace("%player%", pn).replace("%displayer", p.getDisplayName()).replace("%msg%", e.getMessage()));
                                    if (!e.isCancelled()) {
                                        e.setCancelled(true);
                                    }
                                }
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
