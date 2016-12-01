package fr.ranksql.neutronstars.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.ranksql.neutronstars.utils.RankSQL;
import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Gestion de la commande rank.
 * 
 * @author Neutron_Stars
 * @version 1.0
 */

public class RankCommand implements CommandExecutor, TabCompleter{

	/**
	 * CommandExecutor de la commande rank.
	 * @param s 
	 * 			Celui qui envoi la commande.
	 * @param c
	 * 			La commande.
	 * @param l
	 * 			Le label de la commande.
	 * @param a
	 * 			Les arguments qui suit le label.
	 * @return boolean true (Meme si la command à mal été saisi. 
	 * @since 1.0
	 */
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if(s instanceof Player){if(!RankSQL.hasPermissionPowerSE((Player)s, RankUtils.getRankPowerCommand())) return notPermission(s);}
		
		if(a.length < 1) return helpCommand(s);
		
		if(a[0].equalsIgnoreCase("add")) return add(s, a);
		if(a[0].equalsIgnoreCase("remove")) return remove(s, a);
		if(a[0].equalsIgnoreCase("list")) return list(s);
		if(a[0].equalsIgnoreCase("option")) return option(s, a);
		if(a[0].equalsIgnoreCase("default")) return defaultRank(s, a);
		if(a[0].equalsIgnoreCase("player")) return player(s, a);
		
		return helpCommand(s);
	}
	
	/**
	  * Envoi un message si le CommandSender n'a pas la permission de faire cette commande.
	  * 
	  * @param s
	  * 		Celui qui a executé la commande.
	  * @return Boolean true.
	  * @since 1.0
	  */
	private boolean notPermission(CommandSender s){
		s.sendMessage("§cVous n'avez pas l'autorisation de faire cette commande.");
		return true;
	}
	
	/**
	 * TabCompleter de la commande rank.
	 * @param s 
	 * 			Celui qui fait la commande.
	 * @param c
	 * 			La commande.
	 * @param l
	 * 			Le label de la commande.
	 * @param a
	 * 			Les arguments qui suit le label.
	 * @return La list des arguments possible. Null si aucun arguments n'est possible. 
	 * @since 1.0
	 */
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		if(s instanceof Player){if(!RankSQL.hasPermissionPowerSE((Player)s, RankUtils.getRankPowerCommand())) return null;}
		
		if(a.length == 1) return completer(a[0], Arrays.asList("add", "remove", "option", "list", "default", "player"));
		
		if(a.length == 2){
			if(a[0].equalsIgnoreCase("remove") || a[0].equalsIgnoreCase("default")) return completer(a[1], RankUtils.getDataBase().getTeams());
			if(a[0].equalsIgnoreCase("option")) return completer(a[1], Arrays.asList("prefix", "suffix", "power"));
			if(a[0].equalsIgnoreCase("player")) return completer(a[1], Arrays.asList("add", "remove"));
		}
		
		if(a.length == 3){
			if(a[0].equalsIgnoreCase("add")) return completer(a[2], Arrays.asList("null"));
			if(a[0].equalsIgnoreCase("option") && (a[1].equalsIgnoreCase("prefix") || a[1].equalsIgnoreCase("suffix") || a[1].equalsIgnoreCase("power"))) return completer(a[2], RankUtils.getDataBase().getTeams());
		}
		
		if(a.length == 4){
			if(a[0].equalsIgnoreCase("add")) return completer(a[3], Arrays.asList("null"));
			if(a[0].equalsIgnoreCase("player") && (a[1].equalsIgnoreCase("add") || a[1].equalsIgnoreCase("remove"))) return completer(a[3], RankUtils.getDataBase().getTeams());
		}
		
		return null;
	}
	
	/**
	 * Envoi la list des arguments possible lors du TabCompleter
	 * @param arg
	 *			  L'argument qui doit etre completé.
	 * @param string
	 *            List des arguments possible.
	 * @return La list des arguments qui correspond à arg.
	 * @since 1.0
	 */
	private List<String> completer(String arg, Collection<String> string){
		List<String> list = Lists.newArrayList();
		for(String args : string){
			if(args.toLowerCase().startsWith(arg.toLowerCase())) list.add(args);
		}
		return list;
	}
	/**
	 * Envoie la list des commandes dans le Tchat du CommandSender.
	 * @param s
	 *         Celui qui a fait la commande.
	 * @return Boolean true.
	 * @since 1.0
	 */
	private boolean helpCommand(CommandSender s){
		s.sendMessage("§c/rank add [name] [prefix] [suffix] [power]");
		s.sendMessage("§c/rank remove [name]");
		s.sendMessage("§c/rank list");
		s.sendMessage("§c/rank option <prefix, suffix, power> [rank] [param]");
		s.sendMessage("§c/rank player add [player] [rank]");
		s.sendMessage("§c/rank player remove [player]");
		s.sendMessage("§c/rank default [rank]");
		return true;
	}
	/**
	 * Envoie un message suivant la commande qui indique si oui ou non le grade existe.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param exist
	 * 			Boolean qui indique si oui ou non le grade existe.
	 * @return Boolean true.
	 * @since 1.0
	 */
	private boolean rankError(CommandSender s, boolean exist){
		if(exist) s.sendMessage("§cLe grade existe déjà.");
		else s.sendMessage("§cLe grade n'existe pas.");
		return true;
	}
	
	/**
	 * Envoi un message d'erreur au CommandSender si le power n'est pas un chiffre.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @return Boolean true.
	 * @since 1.0
	 */
	private boolean powerError(CommandSender s){
		s.sendMessage("§cLe power doit être un chiffre.");
		return true;
	}
	
	/**
	 * Methode pour ajouter un grade.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param a
	 * 			Les arguments qui suivent le label dans la methode onCommand
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean add(CommandSender s, String[] a){
		if(a.length < 5) return helpCommand(s);
		if(RankUtils.getDataBase().hasRank(a[1])) return rankError(s, true);
		int power = 0;
		try{power = Integer.parseInt(a[4]);}catch(NumberFormatException nfe){return powerError(s);}
		String prefix = a[2].replace('&', '§').replace('€', ' ');
		String suffix = a[3].replace('&', '§').replace('€', ' ');
		RankUtils.getDataBase().newRank(a[1], prefix, suffix, power);
		RankUtils.addTeamScoreboard(a[1], prefix, suffix);
		s.sendMessage("§2Le grade §8[§9"+a[1]+"§8] §2a bien été créé.");
		return true;
	}

	/**
	 * Methode pour supprimer un grade.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param a
	 * 			Les arguments qui suivent le label dans la methode onCommand
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean remove(CommandSender s, String[] a){
		if(a.length < 2) return helpCommand(s);
		if(!RankUtils.getDataBase().hasRank(a[1])) return rankError(s, false);
		RankUtils.getDataBase().removeRank(a[1]);
		RankUtils.getScoreboard().getTeam(a[1]).unregister();
		Collection<String> players = RankUtils.getDataBase().getPlayer();
		String rank = RankUtils.getDataBase().getRankDefault();
		if(rank == null) rank = "null";
		for(String player : players){
			if(RankUtils.getDataBase().getPlayerTeam(player).equalsIgnoreCase(a[1])) RankUtils.getDataBase().setPlayerEditRank(player, rank);
		}
		s.sendMessage("Le grade a bien été supprimé.");
		return true;
	}

	/**
	 * Methode pour voir la list des grades.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean list(CommandSender s){
		List<String> nameRanks = (ArrayList<String>) RankUtils.getDataBase().getTeams();
		if(nameRanks.size() == 0) s.sendMessage("§cIl n'y a aucun grade d'enregistré.");
		else{
			String nameRankList = nameRanks.toString().replace("[", "").replace("]", "");
			s.sendMessage("§3La Liste des grades :\n§2"+nameRankList);
		}
		return true;
	}

	/**
	 * Methode pour l'argument option de la commande rank.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param a
	 * 			Les arguments qui suivent le label dans la methode onCommand
	 * @return Boolean true
	 * @see RankCommand#optionPrefix(CommandSender, String, String)
	 * @see RankCommand#optionSuffix(CommandSender, String, String)
	 * @see RankCommand#optionPower(CommandSender, String, String)
	 * @since 1.0
	 */
	private boolean option(CommandSender s, String[] a){
		if(a.length < 4) return helpCommand(s);
		if(!RankUtils.getDataBase().hasRank(a[2])) return rankError(s, false);
		if(a[1].equalsIgnoreCase("prefix")) return optionPrefix(s, a[2], a[3].replace('&', '§').replace('€', ' '));
		if(a[1].equalsIgnoreCase("suffix")) return optionSuffix(s, a[2], a[3].replace('&', '§').replace('€', ' '));
		if(a[1].equalsIgnoreCase("power")) return optionPower(s, a[2], a[3]);
		return helpCommand(s);
	}
	
	/**
	 * Methode pour modifier le prefix d'un grade.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param rank
	 * 			Le grade qui doit être modifé.
	 * @param prefix
	 * 			Le nouveau prefix du grade.
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean optionPrefix(CommandSender s, String rank, String prefix){
		RankUtils.getDataBase().setPrefix(rank, prefix);
		if(prefix.toLowerCase().equalsIgnoreCase("null")) RankUtils.getScoreboard().getTeam(rank).setPrefix(null);
		else RankUtils.getScoreboard().getTeam(rank).setPrefix(prefix);
		s.sendMessage("§2Le prefix a bien été changé.");
		return true;
	}
	
	/**
	 * Methode pour modifier le suffix d'un grade.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param rank
	 * 			Le grade qui doit être modifé.
	 * @param suffix
	 * 			Le nouveau suffix du grade.
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean optionSuffix(CommandSender s, String rank, String suffix){
		RankUtils.getDataBase().setSuffix(rank, suffix);
		if(suffix.toLowerCase().equalsIgnoreCase("null")) RankUtils.getScoreboard().getTeam(rank).setSuffix(null);
		else RankUtils.getScoreboard().getTeam(rank).setSuffix(suffix);
		s.sendMessage("§2Le suffix a bien été changé.");
		return true;
	}
	
	/**
	 * Methode pour modifier le power d'un grade.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param rank
	 * 			Le grade qui doit être modifé.
	 * @param powerString
	 * 			Le nouveau power du grade.
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean optionPower(CommandSender s, String rank, String powerString){
		int power = 0;
		try{power = Integer.parseInt(powerString);}catch(NumberFormatException nfe){powerError(s);}
		RankUtils.getDataBase().setPower(rank, power);
		s.sendMessage("§2Le power a bien été changé.");
		return true;
	}
	
	/**
	 * Selectionne le grade par défaut.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param a
	 * 			Les arguments qui suivent le label dans la méthode onCommand
	 * @return Boolean true
	 * @since 1.0
	 */
	private boolean defaultRank(CommandSender s, String[] a){
		if(a.length < 2) return helpCommand(s);
		if(!RankUtils.getDataBase().hasRank(a[1])) return rankError(s, false);
		for(String rank : RankUtils.getDataBase().getTeams()) RankUtils.getDataBase().setDefaultRank(rank, 0);
		RankUtils.getDataBase().setDefaultRank(a[1], 1);
		s.sendMessage("§2Le grade §9"+a[1]+" §2est selectionné par défaut.");
		return true;
	}
	/**
	 * Methode pour l'argument player de la commande rank
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param a
	 * 			Les arguments qui suivent le label dans la methode onCommand.
	 * @return Boolean true.
	 * @see RankCommand#addPlayer(CommandSender, String, String)
	 * @see RankCommand#removePlayer(CommandSender, String)
	 * @since 1.0
	 */
	private boolean player(CommandSender s, String[] a){
		if(a.length < 3) return helpCommand(s);
		if(Bukkit.getPlayerExact(a[2]) == null){
			if(!RankUtils.getDataBase().hasPlayerName(a[2])) return playerError(s);
		}
		if(a[1].equalsIgnoreCase("remove")) return removePlayer(s, a[2]);
		if(a.length < 4) return helpCommand(s);
		if(!RankUtils.getDataBase().hasRank(a[3])) return rankError(s, false);
		if(a[1].equalsIgnoreCase("add")) return addPlayer(s, a[3], a[2]);
		return helpCommand(s);
	}
	
	/**
	 * Envoi un message au CommandSender si le joueur n'existe pas.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @return Boolean true.
	 * @since 1.0
	 */
	private boolean playerError(CommandSender s){
		s.sendMessage("§cLe joueur n'existe pas.");
		return true;
	}
	
	/**
	 * Permet d'ajouté un grade à un joueur.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param rank
	 * 			Le nom du grade que le joueur rejoindra.
	 * @param name
	 * 			Le nom du joueur.
	 * @return Boolean true.
	 * @since 1.0
	 */
	private boolean addPlayer(CommandSender s, String rank, String name){
		if(RankUtils.getDataBase().getPlayerTeam(name) != null){
			if(RankUtils.getDataBase().getPlayerTeam(name).equals(rank)){
				s.sendMessage("§cLe joueur a déjà ce grade.");
				return true;
			}
		}
		RankUtils.getDataBase().setPlayerEditRank(name, rank);
		if(Bukkit.getPlayerExact(name) != null) RankUtils.setPlayerTeam(rank, Bukkit.getPlayerExact(name));
		s.sendMessage("§2Le joueur a bien eu son grade.");
		return true;
	}
	
	/**
	 * Permet de retire le grade d'un joueur. Il rejoindra le grade par défaut automatiquement s'il existe.
	 * @param s
	 * 			Celui qui a envoyé la commande.
	 * @param name
	 * 			Le nom du joueur.
	 * @return Boolean true.
	 * @since 1.0
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	private boolean removePlayer(CommandSender s, String name){
		String rank = RankUtils.getDataBase().getRankDefault();
		if(rank == null) rank = "null";
		RankUtils.getDataBase().setPlayerEditRank(name, rank);
		if(Bukkit.getPlayerExact(name) != null){
			if(rank != null) RankUtils.setPlayerTeam(rank, Bukkit.getPlayerExact(name));
			else{
				RankUtils.getScoreboard().getPlayerTeam(Bukkit.getPlayerExact(name)).removePlayer(Bukkit.getPlayerExact(name));
				Bukkit.getPlayerExact(name).setScoreboard(RankUtils.getScoreboard());
			}
		}
		s.sendMessage("§2Le joueur ne possede plus ce grade.");
		return true;
	}
}
