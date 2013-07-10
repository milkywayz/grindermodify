package net.milkycraft.grindermodify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class GrinderModify extends JavaPlugin implements Listener {

	private final Random rand = new Random();
	private final List<PotionEffectType> pots = new ArrayList<PotionEffectType>();
	private int spread = 0;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.doFig();
		this.getLogger().info("GrinderModify loaded with a spread of " + spread);
	}

	@Override
	public void onDisable() {
		pots.clear();
	}

	private void doFig() {
		this.saveDefaultConfig();
		this.spread = this.getConfig().getInt("General.Spread", 4);
		for (String s : this.getConfig().getStringList("General.Effects")) {
			pots.add(PotionEffectType.getByName(s.toUpperCase()));
		}
	}

	private int a(int b) {
		if (rand.nextBoolean())
			return -b;
		return b;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onSpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.SPAWNER) {
			LivingEntity en = e.getEntity();
			double health = en.getHealth() + a(rand.nextInt(spread) + 1);
			en.setMaxHealth(health > 0 ? health : 2);
			en.setHealth(health);
			if (pots.size() > 0) {
				int ran = rand.nextInt(pots.size());
				en.addPotionEffect(pots.get(ran).createEffect(rand.nextInt(512*20), 1));
			}
		}
	}
}
