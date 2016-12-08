package fr.ranksql.neutronstars.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Class du AsyncPlayerChatEvent pour modifier le format dans tchat en fonction du grade du joueur.
 * @author Neutron_Stars
 * @version 1.1
 */
public class PlayerTchatListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerTchat(AsyncPlayerChatEvent apce){
		String rank = RankUtils.getDataBase().getPlayerTeam(apce.getPlayer());
		if(rank == null) return;
		String prefix = RankUtils.getDataBase().getChatPrefix(rank);
		String suffix = RankUtils.getDataBase().getChatSuffix(rank);
		String separator = RankUtils.getDataBase().getChatSeparator(rank);
		
		if(prefix.toLowerCase().equalsIgnoreCase("null")) prefix = "";
		if(suffix.toLowerCase().equalsIgnoreCase("null")) suffix = "";
		if(separator.toLowerCase().equalsIgnoreCase("null")) separator = "";
		
		apce.setFormat(prefix+apce.getPlayer().getName()+suffix+apce.getMessage());
	}
}
