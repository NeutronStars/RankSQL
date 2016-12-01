package fr.ranksql.neutronstars.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Class du AsyncPlayerChatEvent pour modifier le format dans tchat en fonction du grade du joueur.
 * @author Neutron_Stars
 * @version 1.0
 */
public class PlayerTchatListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerTchat(AsyncPlayerChatEvent apce){
		String rank = RankUtils.getDataBase().getPlayerTeam(apce.getPlayer());
		if(rank == null) return;
		apce.setFormat(RankUtils.getScoreboard().getTeam(rank).getPrefix()+apce.getPlayer().getName()+RankUtils.getScoreboard().getTeam(rank).getSuffix()+" : "+apce.getMessage());
	}
}
