package me.LuisArtz.SS;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
    public static PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 10000 * 200, 100, false, true);
    public static ArrayList<String> frozen = new ArrayList();
    public static ArrayList<String> waiting = new ArrayList();
    
    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        Bukkit.getServer().getConsoleSender().sendMessage("GoodSS By LuisArtz v2.1");
        Bukkit.getServer().getConsoleSender().sendMessage("Running in " + Bukkit.getServerName());
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        saveConfig();
        registerBans();
        reloadConfig();
        reloadBans();
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
        }
    }
    public void saveBans(){
        try{
            bans.save(bansFile);			
       }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void registerBans(){
        bansFile = new File(this.getDataFolder(),"bans.yml");
        if(!bansFile.exists()){
            this.getBans().options().copyDefaults(true);
            saveBans();
        }
    }
}