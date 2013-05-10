package me.bbark9.nostackplus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_5_R3.Item;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoStack extends JavaPlugin
		implements Listener
{
	@Override
	public void onEnable()
	{
		try {
			saveDefaultConfig();
			
			final List<String> unstacks = getConfig().getStringList("UnStackable");
			final List<Integer> unstackable = new ArrayList<Integer>();
			for (final String string : unstacks) {
				unstackable.add(Integer.valueOf(Integer.parseInt(string)));
			}
			if (Bukkit.getServer().getVersion().toLowerCase().contains("1.5")) {
				getServer().getPluginManager().registerEvents(this, this);
			} else {
				System.out.print("[NoStackPlus] BUG THE FUCK OUT OF BARK TO UPDATE!");
			}
			try
			{
				final Field[] allFields = Item.class.getDeclaredFields();
				
				for (final Field f : allFields)
				{
					final Class<?> type = f.getType();
					
					if ((type.isPrimitive()) & (type.equals(Boolean.TYPE)))
					{
						f.setAccessible(true);
						
						for (final Item item : Item.byId)
						{
							if ((item != null) && (unstackable.contains(Integer.valueOf(item.id))))
							{
								f.setBoolean(item, true);
							}
						}
					}
				}
			} catch (final Exception e) {}
		} catch (final NumberFormatException e) {}
	}
	
	@EventHandler
	public void onInv(final InventoryClickEvent event)
	{
		Bukkit.getServer().getScheduler()
				.scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					@SuppressWarnings("deprecation")
					public void run() {
						if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
							((Player) event.getWhoClicked()).updateInventory();
					}
				}
						, 0L);
	};
}
