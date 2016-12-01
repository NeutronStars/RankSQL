package fr.ranksql.neutronstars.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Class du PlayerJoinEvent pour permettre a joueur d'avoir son grade lors de sa connection au serveur.
 * @author Neutron_Stars
 * @version 1.0
 */
public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerJoin(PlayerJoinEvent pje){
		if(!RankUtils.getDataBase().hasPlayer(pje.getPlayer())) RankUtils.getDataBase().addPlayer(pje.getPlayer());
		String rank = RankUtils.getDataBase().getPlayerTeam(pje.getPlayer());
		if(rank == null){
			rankDefault(pje.getPlayer());
			return;
		}
		
		if(RankUtils.getDataBase().hasRank(rank)){
			RankUtils.setPlayerTeam(rank, pje.getPlayer());
			return;
		}
		
		rankDefault(pje.getPlayer());
	}
	
	private void rankDefault(Player player){
		String rank = RankUtils.getDataBase().getRankDefault();
		if(rank != null){
			RankUtils.getDataBase().setPlayerEditRank(player.getName(), rank);
			RankUtils.setPlayerTeam(rank, player);
		}
	}
}
