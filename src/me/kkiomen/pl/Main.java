package me.kkiomen.pl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public MYSQL database = new MYSQL();

    @Override
    public void onEnable() {
        super.onEnable();
        FilesManager.base(this);
        database.connect();
        getCommand("zglos").setExecutor(new Reports(this));
        Bukkit.getServer().getPluginManager().registerEvents(new Reports(this),  this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        database.close();
    }
}
