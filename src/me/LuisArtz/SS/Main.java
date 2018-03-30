package me.LuisArtz.SS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
    // All fundamental data
    public static HashMap<String, Long> banned = new HashMap<String, Long>();
    FileConfiguration bans = null;
    File bansFile = null;
    FileConfiguration ss = null;
    File ssFile = null;
    FileConfiguration sf = null;
    public Server server;
    public Logger log;
    File sfFile = null;
    public String lastversion;
    public String version = getDescription().getVersion();
	public static String Path = "plugins/GoodSS" + File.separator + "BanList.dat";
    public static PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 10000 * 200, 100, false, true);
    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        Bukkit.getServer().getConsoleSender().sendMessage("GoodSS By LuisArtz v5.1");
        Bukkit.getServer().getConsoleSender().sendMessage("Running in " + Bukkit.getServerName());
        Bukkit.getServer().getConsoleSender().sendMessage("==================");
        // Register all ymls
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        registerBans();
        registerSF();
        registerSS();
        updateChecker();
        
	log = this.getLogger();
	File file = new File(Path);
	new File("plugins/GoodSS").mkdir();
	    
        if(file.exists()){
            banned = load();
        }
        if(banned == null){
            banned = new HashMap<String, Long>();
	}

    }
    @Override
    public void onDisable(){
        ssFile = new File(getDataFolder(), "tempssdata.yml");
        if (ssFile.exists()){
            ssFile.delete();
            Bukkit.getServer().getConsoleSender().sendMessage("tempssdata Has deleted");
        }
        sfFile = new File(getDataFolder(), "tempsfdata.yml");
        if (sfFile.exists()){
            sfFile.delete();
            Bukkit.getServer().getConsoleSender().sendMessage("tempsfdata Has deleted");
        }
    }
    // Register events
    public void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Events(this), this);
    }
    // Command
    public void registerCommands() {
        getCommand("gss").setExecutor(new CmdgSS(this));
        getCommand("admit").setExecutor(new AdmitCMD(this));
        getCommand("gstban").setExecutor(new TBanConsole(this));
    }
    // Bans.yml load
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
    // Update check when start server
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
                    Bukkit.getConsoleSender().sendMessage("§3By §b§lLuis§f§lArtz");
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
                    Bukkit.getConsoleSender().sendMessage("§3By §b§lLuis§f§lArtz");
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
            Bukkit.getConsoleSender().sendMessage("§3By §b§lLuis§f§lArtz");
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
    // All TempSSData.yml (The file will deleted when stop plugins)
    public FileConfiguration getSS(){
        if(ss == null){
            reloadSS();
        }
        return ss;
    }
    public void reloadSS(){
        if(ss == null){
            ssFile = new File(getDataFolder(),"tempssdata.yml");
        }
        ss = YamlConfiguration.loadConfiguration(ssFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("tempssdata.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                ss.setDefaults(defConfig);
            }			
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error reloading the tempssdata.yml");
        }
    }
    public void saveSS(){
        try{
            ss.save(ssFile);			
       }catch (IOException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error saving the tempssdata.yml");
        }
    }
    public void registerSS(){
        ssFile = new File(this.getDataFolder(),"tempssdata.yml");
        if(!ssFile.exists()){
            this.getSS().options().copyDefaults(true);
            saveSS();
        }
    }
    public FileConfiguration getSF(){
        if(sf == null){
            reloadSF();
        }
        return sf;
    }
    public void reloadSF(){
        if(sf == null){
            sfFile = new File(getDataFolder(),"tempsfdata.yml");
        }
        sf = YamlConfiguration.loadConfiguration(sfFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("tempsfdata.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                sf.setDefaults(defConfig);
            }			
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error reloading the tempsfdata.yml");
        }
    }
    public void saveSF(){
        try{
            sf.save(sfFile);
       }catch (IOException e){
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Error saving the tempsfdata.yml");
        }
    }
    public void registerSF(){
        sfFile = new File(this.getDataFolder(),"tempsfdata.yml");
        if(!sfFile.exists()){
            this.getSF().options().copyDefaults(true);
            saveSF();
        }
    }
    public static void save(){
        File file = new File("plugins/GoodSS" + File.separator + "Tempbans.dat");
        new File("plugins/GoodSS").mkdir();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path))) {
                oos.writeObject(banned);
                oos.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public static HashMap<String, Long> load(){
        try{
            Object result;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path))) {
                result = ois.readObject();
            }
            return (HashMap<String,Long>)result;
        }catch(Exception e){
            return null;
        }
    }
}
