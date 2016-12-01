package fr.ranksql.neutronstars.utils;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import fr.ranksql.neutronstars.commands.RankCommand;
import fr.ranksql.neutronstars.database.SQLManager;
import fr.ranksql.neutronstars.error.ClassAlreadyExistingException;
import fr.ranksql.neutronstars.events.PlayerJoinListener;
import fr.ranksql.neutronstars.events.PlayerTchatListener;

/**
 * Class final ou son rangé toutes les methodes qui servent dans le plugin.
 * @author Neutron_Stars
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public final class RankUtils {

	/**
	 * Variable du Scoreboard ou sont rangé les grades existant. Ne peux etre réinitialisé.
	 * @see RankUtils#getScoreboard()
	 */
	private final static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	
	/**
	 * Variable qui permet de savoir quel power minimum faut il avoir pour executer la commande rank.
	 * Peut etre modifer dans le fichier RankSQL.yml ou via la methode setPowerRankCommand(int)
	 * @see RankUtils#getRankPowerCommand()
	 * @see RankUtils#setPowerRankCommand(int)
	 */
	private static int rankPowerCommand = 100;
	
	/**
	 * Class ou est ce trouve la connection et les infos de la base de donnée.
	 * @see SQLManager
	 * @see RankUtils#getDataBase()
	 */
	private static SQLManager data_base;
	
	/**
	 * Class de démarrage.
	 * @see RankUtils#getPlugin()
	 */
	private static JavaPlugin plugin;

	/**
	 * Appelé lors du démarrage du serveur. Ne peut pas être rappelé.
	 * @param plugin
	 * 		Class de démarrage.
	 * @throws ClassAlreadyExistingException
	 * @see Rank
	 * @see ClassAlreadyExistingException
	 */
	public static void register(JavaPlugin plugin){
		if(RankUtils.plugin != null) throw new ClassAlreadyExistingException("The Class Plugin already existing");
		RankUtils.plugin = plugin;
		data_base = new SQLManager();
		
		registerEvent(new PlayerJoinListener());
		registerEvent(new PlayerTchatListener());
		
		registerCommand("rank", new RankCommand());
	}
	
	/**
	 * Getter du scoreboard.
	 * @return Le scoreboard ou son rangé les grades.
	 * @see RankUtils#scoreboard
	 */
	public static Scoreboard getScoreboard() {
		return scoreboard;
	}
	/**
	 * Getter du plugin
	 * @return le plugin
	 * @see RankUtils#plugin
	 */
	public static JavaPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Getter de data_base
	 * @return Le SQLManager {@link SQLManager}
	 * @see RankUtils#data_base
	 */
	public static SQLManager getDataBase() {
		return data_base;
	}
	
	/**
	 * Getter du rankPowerCommand
	 * @return rankPowerCommand
	 * @see RankUtils#rankPowerCommand
	 */
	public static int getRankPowerCommand() {
		return rankPowerCommand;
	}
	
	/**
	 * Setter du rankPowerCommand
	 * @param rankPowerCommand
	 * 		Nouveau power minimum requis pour executer la command rank
	 * @see RankUtils#rankPowerCommand
	 */
	public static void setPowerRankCommand(int rankPowerCommand) {
		RankUtils.rankPowerCommand = rankPowerCommand;
	}
	
	/**
	 * Permet de set tout les grades dans le scoreboard au demarrage.
	 * Cette methode ne doit pas etre rappelé.
	 * @param sqlManager
	 *       Objet ou ce trouve la base de donnée. {@link SQLManager}
	 * @see RankUtils#addTeamScoreboard(String, String, String)
	 */
	public static void updateTeams(SQLManager sqlManager){
		Collection<String> teams = sqlManager.getTeams();
		for(String team : teams){
			String prefix = sqlManager.getRankPrefix(team);
			String suffix = sqlManager.getRankSuffix(team);
			if(prefix == null) prefix = "null";
			if(suffix == null) suffix = "null";
			addTeamScoreboard(team, prefix, suffix);
		}
	}
	
	/**
	 * Set un joueur dans un grade enregistré dans le scoreboard.
	 * Ne doit pas être appelé en dehors de la Class RankCommand {@link RankCommand}
	 * @param team
	 * 			Grade que le joueur possede
	 * @param player
	 * 			Joueur qui est ajouté dans la team.
	 */
	public static void setPlayerTeam(String team, Player player){
		if(team == null) return;
		if(team.equalsIgnoreCase("null")) return;
		scoreboard.getTeam(team).addPlayer(player);
		player.setScoreboard(scoreboard);
	}

	/**
	 * Enregistrer un Event.
	 * @param listener
	 * 			L'event qui doit etre enregistrer.
	 */
	private static void registerEvent(Listener listener){
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}
	
	/**
	 * Ajoute un grade au scoreboard.
	 * Ne doit pas etre appelé en dehors de la Class RankCommand {@link RankCommand}
	 * @param name
	 * 		Le nom du grade.
	 * @param prefix
	 * 		Le prefix du grade.
	 * @param suffix
	 * 		Le Suffix du grade.
	 */
	public static void addTeamScoreboard(String name, String prefix, String suffix){
		scoreboard.registerNewTeam(name);
		if(!prefix.toLowerCase().equals("null")) scoreboard.getTeam(name).setPrefix(prefix);
		if(!suffix.toLowerCase().equals("null")) scoreboard.getTeam(name).setSuffix(suffix);
		scoreboard.getTeam(name).setNameTagVisibility(NameTagVisibility.ALWAYS);
	}
	
	/**
	 * Enregistre un commande.
	 * @param label
	 * 		Nom de la commande.
	 * @param command
	 * 		La Class CommandExecutor.
	 */
	public static void registerCommand(String label, CommandExecutor command){
		getPlugin().getCommand(label).setExecutor(command);
	}
}
