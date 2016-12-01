package fr.ranksql.neutronstars;

import org.bukkit.plugin.java.JavaPlugin;

import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Class de départ du plugin.
 * @author Neutron_Stars
 * @version 1.0
 */

public class Rank extends JavaPlugin {

	@Override
	public void onEnable() {
		RankUtils.register(this);
	}
}
