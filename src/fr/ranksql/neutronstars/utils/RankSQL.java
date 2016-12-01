package fr.ranksql.neutronstars.utils;

import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.entity.Player;

/**
 * Class que les developpeurs peuvent ce servir.
 * @author NeutronStars
 * @version 1.0
 */

public final class RankSQL {

	/**
	 * @return la connection à la base de donnée.
	 */
	public static Connection getConnection(){
		return RankUtils.getDataBase().getConnection();
	}
	
	/**
	 * @return le statement de la connection à la base de donnée.
	 */
	public static Statement getStatement(){
		return RankUtils.getDataBase().getStatement();
	}
	
	/**
	 * Savoir si le joueur a la permission ou pas en fonction de son grade.
	 * @param player
	 * 		Le joueur
	 * @param power
	 * 		Le power de permission.
	 * @return si [le power du joueur == power de permission] alors 'true' sinon 'false'.
	 */
	public static boolean hasPermissionPower(Player player, int power){
		String rank = RankUtils.getDataBase().getPlayerTeam(player);
		if(rank == null) return false;
		if(RankUtils.getDataBase().getPower(rank) == power) return true;
		return false;
	}
	
	
	/**
	 * Savoir si le joueur a la permission ou pas en fonction de son grade.
	 * @param player
	 * 		Le joueur
	 * @param power
	 * 		Le power de permission.
	 * @return si [le power du joueur <= power de permission] alors 'true' sinon 'false'.
	 */
	public static boolean hasPermissionPowerIE(Player player, int power){
		String rank = RankUtils.getDataBase().getPlayerTeam(player);
		if(rank == null) return false;
		if(RankUtils.getDataBase().getPower(rank) <= power) return true;
		return false;
	}
	
	/**
	 * Savoir si le joueur a la permission ou pas en fonction de son grade.
	 * @param player
	 * 		Le joueur
	 * @param power
	 * 		Le power de permission.
	 * @return si [le power du joueur >= power de permission] alors 'true' sinon 'false'.
	 */
	public static boolean hasPermissionPowerSE(Player player, int power){
		String rank = RankUtils.getDataBase().getPlayerTeam(player);
		if(rank == null) return false;
		if(RankUtils.getDataBase().getPower(rank) >= power) return true;
		return false;
	}
	
	/**
	 * Savoir si le joueur a la permission ou pas en fonction de son grade.
	 * @param player
	 * 		Le joueur
	 * @param power
	 * 		Le power de permission.
	 * @return si [le power du joueur < power de permission] alors 'true' sinon 'false'.
	 */
	public static boolean hasPermissionPowerI(Player player, int power){
		String rank = RankUtils.getDataBase().getPlayerTeam(player);
		if(rank == null) return false;
		if(RankUtils.getDataBase().getPower(rank) < power) return true;
		return false;
	}
	
	/**
	 * Savoir si le joueur a la permission ou pas en fonction de son grade.
	 * @param player
	 * 		Le joueur
	 * @param power
	 * 		Le power de permission.
	 * @return si [le power du joueur > power de permission] alors 'true' sinon 'false'.
	 */
	public static boolean hasPermissionPowerS(Player player, int power){
		String rank = RankUtils.getDataBase().getPlayerTeam(player);
		if(rank == null) return false;
		if(RankUtils.getDataBase().getPower(rank) > power) return true;
		return false;
	}
}
