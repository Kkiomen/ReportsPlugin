package me.kkiomen.pl;

import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Reports implements CommandExecutor, Listener {

    private Main plugin;

    public Reports(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("reports.create")){
            ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
            player.getInventory().addItem(book);
            ItemMeta book_meta = book.getItemMeta();
            book_meta.setDisplayName(ChatColor.DARK_GRAY + "Wpisz tutaj zgloszenie");
            book.setItemMeta(book_meta);

            player.sendMessage(ChatColor.GREEN + "Otrzymałeś książke do wpisania swojego zgłoszenia \n Gdy wypelnisz ją,  wpisz /zglos, wloz do pustego pola i zatwierdz");

            Inventory gui = Bukkit.createInventory(player, 27, "Zgloszenia");

            ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta item1_meta = item1.getItemMeta();
            item1_meta.setDisplayName("");
            ArrayList<String> itemLore = new ArrayList<>();
            item1_meta.setLore(itemLore);
            item1.setItemMeta(item1_meta);
            for(int i = 0; i<27 ; i++){
                if(i == 10){

                }else if(i == 17){
                    ItemStack item3 = new ItemStack(Material.RED_TERRACOTTA, 1);
                    ItemMeta item3_meta = item3.getItemMeta();
                    item3_meta.setDisplayName(ChatColor.DARK_GRAY + "Zamknij");
                    item3.setItemMeta(item3_meta);
                    gui.setItem(i,item3);
                }else if(i == 15){
                    ItemStack item4 = new ItemStack(Material.GREEN_TERRACOTTA, 1);
                    ItemMeta item4_meta = item4.getItemMeta();
                    item4_meta.setDisplayName(ChatColor.GREEN + "Wyślij zgłoszenie");
                    item4.setItemMeta(item4_meta);
                    gui.setItem(i,item4);
                }else{
                    gui.setItem(i,item1);
                }
            }
            player.openInventory(gui);

        }

        return false;
    }



    @EventHandler
    public void onClickInventory(InventoryClickEvent e) throws SQLException {
        HumanEntity player = e.getWhoClicked();
        try{
            if (e.getView().getTitle().equals("Zgloszenia")) {
                if(e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE ||
                        e.getCurrentItem().getType() == Material.RED_TERRACOTTA
                ){
                    e.setCancelled(true);
                }

                if (e.getCurrentItem().getType() == Material.GREEN_TERRACOTTA){
                    e.setCancelled(true);
                    ItemStack bookItem = e.getInventory().getItem(10);
                    BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                    Integer countPage = bookMeta.getPageCount();
                    String text = "";
                    for(int i = 1; i <= countPage; i++){
                        text += bookMeta.getPage(i) + " ";
                    }

                    Integer limitToSend = 8;
                    if(text.length() > limitToSend){
                        player.sendMessage(text);

                        String sql = "INSERT INTO reports (id, nick,text) VALUES (NULL, ?, ?);";
                        PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
                        stmt.setString(1, String.valueOf(player.getName()));
                        stmt.setString(2, String.valueOf(text));
                        stmt.executeUpdate();

                        player.sendMessage(ChatColor.GREEN + "Poprawnie wysłano zgłoszenie");

                    }else{
                        player.sendMessage(ChatColor.RED + "Twoja wiadomosc jest za krotka. Wymagamy, minimum X znakow");
                    }



                }

            }
        }catch (NullPointerException ek){}
    }
}