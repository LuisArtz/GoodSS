package me.LuisArtz.SS;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
    FileConfiguration bans = null;
    File bansFile = null;
    public String lastversion;
    public String version = getDescription().getVersion();
    public static PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 10000 * 200, 100, false, true);
    public static ArrayList<String> frozen = new ArrayList();
    public static ArrayList<String> waiting = new ArrayList();
    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        Bukkit.getServer().getConsoleSender().sendMessage("GoodSS By LuisArtz v3.0");
        Bukkit.getServer().getConsoleSender().sendMessage("Running in " + Bukkit.getServerName());
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        saveConfig();
        registerBans();
        reloadConfig();
        reloadBans();
        updateChecker();
    }
    public void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Events(this), this);
    }
    
    public void registerCommands() {
        getCommand("gss").setExecutor(new CmdgSS(this));
    }
    public FileConfiguration getBans(){
        if(bans == null){
            reloadBans();
        }
        return bans;
    }
    public void reloadBans(){
        if(bans == null){
            bansFile = new File(getDataFolder(),"bans.yml");
        }
        bans = YamlConfiguration.loadConfiguration(bansFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("bans.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                bans.setDefaults(defConfig);
            }			
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error reloading the bans.yml");
        }
    }
    public void saveBans(){
        try{
            bans.save(bansFile);			
       }catch (IOException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error saving the bans.yml");
        }
    }
    public void registerBans(){
        bansFile = new File(this.getDataFolder(),"bans.yml");
        if(!bansFile.exists()){
            this.getBans().options().copyDefaults(true);
            saveBans();
        }
    }
    public void updateChecker(){        
        try{
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=53515").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            lastversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (lastversion.length() <= 7) {
                if(!getDescription().getVersion().equals(lastversion)){
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage("§3======================");
                    Bukkit.getConsoleSender().sendMessage("§bGood§fSS");
                    Bukkit.getConsoleSender().sendMessage("§a§l§k||§2New update!§a§l§k||");
                    Bukkit.getConsoleSender().sendMessage("§3Your version: §b"+getDescription().getVersion());
                    Bukkit.getConsoleSender().sendMessage("§3New version: §b"+lastversion);
                    Bukkit.getConsoleSender().sendMessage("§2Download the new version here!: §ahttps://www.spigotmc.org/resources/53515/");
                    Bukkit.getConsoleSender().sendMessage("§3By §bLuis§lArtz");
                    Bukkit.getConsoleSender().sendMessage("§3======================");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                }else{
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage("§3======================");
                    Bukkit.getConsoleSender().sendMessage("§bGood§fSS");
                    Bukkit.getConsoleSender().sendMessage("§a§l§k||§2Now updated!§a§l§k||");
                    Bukkit.getConsoleSender().sendMessage("§3Last version: §b"+getDescription().getVersion());
                    Bukkit.getConsoleSender().sendMessage("§2Thanks for use §bGood§fSS");
                    Bukkit.getConsoleSender().sendMessage("§3By §bLuis§lArtz");
                    Bukkit.getConsoleSender().sendMessage("§3======================");
                    Bukkit.getConsoleSender().sendMessage(" ");
                    Bukkit.getConsoleSender().sendMessage(" ");
                }
            }
        } catch (Exception x) {
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("§3======================");
            Bukkit.getConsoleSender().sendMessage("§bGood§fSS");
            Bukkit.getConsoleSender().sendMessage("§cError while checking update.");
            Bukkit.getConsoleSender().sendMessage("§cSee: https://www.spigotmc.org/resources/53515/");
            Bukkit.getConsoleSender().sendMessage("§3By §bLuis§lArtz");
            Bukkit.getConsoleSender().sendMessage("§3======================");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
            Bukkit.getConsoleSender().sendMessage("(!)");
        }
    }
    public String getVersion(){
        return version;
    }
    public String getLastV() {
        return lastversion;
    }
}
