package fr.ranksql.neutronstars.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.ranksql.neutronstars.commands.RankCommand;
import fr.ranksql.neutronstars.utils.RankUtils;

public class SQLManager {

	/**
	 * Connection à la base de donnée.
	 * @see SQLManager#getConnection()
	 */
	private Connection connexion;
	
	/**
	 * Statement de la base de donnée.
	 * @see SQLManager#getStatement()
	 */
	private Statement statement;
	
	/**
	 * List des tables et colonnes de la base de donnée.
	 */
	private String table_rank, table_rank_name,table_rank_prefix, table_rank_suffix, table_rank_power, table_rank_default, table_player, table_player_uuid, table_player_name, table_player_rank;
	
	/**
	 * Fichier RankSQL.yml
	 */
	private File file = new File("plugins/RankSQL/RankSQL.yml");
	
	
	/**
	 * Constructeur.
	 */
	public SQLManager(){
		if(!file.exists()){
			new File("plugins/RankSQL").mkdirs();
			createFile();
			return;
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connexion = DriverManager.getConnection(config.getString("URL"), config.getString("USER"), config.getString("PASSWORD"));
			statement = connexion.createStatement();
			table_rank = config.getString("TABLE_RANK");
			table_rank_name = config.getString("TABLE_RANK_NAME");
			table_rank_prefix = config.getString("TABLE_RANK_PREFIX");
			table_rank_suffix = config.getString("TABLE_RANK_SUFFIX");
			table_rank_power = config.getString("TABLE_RANK_POWER");
			table_rank_default = config.getString("TABLE_RANK_DEFAULT");
			table_player = config.getString("TABLE_PLAYER");
			table_player_uuid = config.getString("TABLE_PLAYER_UUID");
			table_player_name = config.getString("TABLE_PLAYER_NAME");
			table_player_rank = config.getString("TABLE_PLAYER_RANK");
			RankUtils.setPowerRankCommand(config.getInt("POWER_RANK_COMMAND"));
			
			System.out.println("[MySQL] -- Connected");
			
			RankUtils.updateTeams(this);
		}catch(Exception e){
			System.out.println("[MySQL] -- Impossible de ce connecter sur votre base de donnee.");
		}
	}
	/**
	 * Creation du fichier RankSQL.yml si celui n'exite pas.
	 * @since 1.0
	 */
	private void createFile(){
		try{file.createNewFile();}catch(IOException ioe){System.out.println("Erreur sur la création du fichier \"RankSQL.yml\".");}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set("URL", "");
		config.set("USER", "root");
		config.set("PASSWORD", "");
		config.set("TABLE_RANK", "");
		config.set("TABLE_RANK_NAME", "");
		config.set("TABLE_RANK_PREFIX", "");
		config.set("TABLE_RANK_SUFFIX", "");
		config.set("TABLE_RANK_POWER", "");
		config.set("TABLE_RANK_DEFAULT", "");
		config.set("TABLE_PLAYER", "");
		config.set("TABLE_PLAYER_UUID", "");
		config.set("TABLE_PLAYER_NAME", "");
		config.set("TABLE_PLAYER_RANK", "");
		config.set("POWER_RANK_COMMAND", 100);
		
		try{config.save(file);}catch(IOException ioe){System.out.println("Impossible de sauvegarder le fichier \"RankSQL.yml\".");}
		System.out.println("Veuillez arreter votre serveur afin de remplir le dossier RankSQL.yml pour pouvoir vous connecter sur votre base de donnee.");
	}
	
	/**
	 * Getter de la connection.
	 * @return la connection de la base de donnée.
	 * @see SQLManager#connexion
	 */
	public Connection getConnection() {
		return connexion;
	}
	
	/**
	 * Getter du Statement.
	 * @return le statement.
	 * @see SQLManager#statement
	 */
	public Statement getStatement() {
		return statement;
	}
	/**
	 * Recupère tout les grades sauvegardé dans la base de donnée.
	 * @return La list de grades.
	 * @since 1.0
	 * @throws SQLException
	 */
	public Collection<String> getTeams(){
		List<String> teams = Lists.newArrayList();
		try{
			ResultSet result = selectAll(table_rank_name, table_rank);
			while(result.next()){
				teams.add(result.getString(table_rank_name));
			}
		}catch(SQLException e){System.out.println(e.getMessage());}
		return teams;
	}
	
	/**
	 * Recupère tout les joueurs sauvegardé dans la base de donnée.
	 * @return La list des joueurs.
	 * @since 1.0
	 * @throws SQLException
	 */
	public Collection<String> getPlayer(){
		List<String> players = Lists.newArrayList();
		try{
			ResultSet result = selectAll(table_player_name, table_player);
			while(result.next()){
				players.add(result.getString(table_player_name));
			}
		}catch(SQLException e){System.out.println(e.getMessage());}
		return players;	
	}
	
	/**
	 * Regarde dans la base de donnée si le grade existe.
	 * @param name
	 * 		Nom du grade
	 * @return S'il existe 'true' sinon 'false'
	 * @since 1.0 
	 * @throws SQLException
	 */
	public boolean hasRank(String name){
		try{
			ResultSet result = select("*", table_rank, table_rank_name, "'"+name+"'");
			result.last();
			if(result.getRow() == 1) return true;
		}catch(SQLException e){System.out.println(e.getMessage());}
		return false;	
	}
	
	/**
	 * Recupère le prefix d'un grade.
	 * @param name
	 *      Nom du grade.
	 * @return le prefix
	 * @since 1.0
	 * @throws SQLException
	 */
	public String getRankPrefix(String name){
		try{
			ResultSet result = select(table_rank_prefix, table_rank, table_rank_name, "'"+name+"'");
			result.next();
			return result.getString(table_rank_prefix).toLowerCase().equalsIgnoreCase("null") ? null : result.getString(table_rank_prefix);
		}catch(SQLException e){System.out.println(e.getMessage());}
		
		return null;
	}
	
	/**
	 * Recupère le suffix d'un grade.
	 * @param name
	 *      Nom du grade.
	 * @return le suffix
	 * @since 1.0
	 * @throws SQLException
	 */
	public String getRankSuffix(String name){
		try{
			ResultSet result = select(table_rank_suffix, table_rank, table_rank_name, "'"+name+"'");
			result.next();
			return result.getString(table_rank_suffix).toLowerCase().equalsIgnoreCase("null") ? null : result.getString(table_rank_suffix);
		}catch(SQLException e){System.out.println(e.getMessage());}
		return null;
	}

	/**
	 * Recupère le grade par défaut.
	 * @return le grade ou null si il y en a pas.
	 * @since 1.0
	 * @throws SQLException
	 */
	public String getRankDefault(){
		try{
			ResultSet result = select(table_rank_name, table_rank, table_rank_default, "1");
			result.last();
			if(result.getRow() == 0) return null;
			return result.getString(table_rank_name);
		}catch(SQLException e){System.out.println(e.getMessage());}
		return null;
	}
	
	/**
	 * Recupère le power d'un grade.
	 * @param name
	 *      Nom du grade.
	 * @return le power
	 * @since 1.0
	 * @throws SQLException
	 */
	public int getPower(String name){
		try{
			ResultSet result = select(table_rank_power, table_rank, table_rank_name, "'"+name+"'");
			result.next();
			return result.getInt(table_rank_power);
		}catch(SQLException e){System.out.println(e.getMessage());}
		return 0;
	}

	/**
	 * Permet de créer un nouveau grade dans la base de donnée.
	 * @param name
	 * 			Nom du nouveau grade.
	 * @param prefix
	 * 			Prefix du nouveau grade.
	 * @param suffix
	 * 			Suffix du nouveau grade.
	 * @param power
	 * 			Power du nouveau grade.
	 * @return 'true' si celui a pu être créé sinon 'false'.
	 * @since 1.0
	 * @throws SQLException
	 */
	public boolean newRank(String name, String prefix, String suffix, int power){
		try{
			statement.executeUpdate("INSERT INTO "+table_rank+" VALUES ('"+name+"','"+prefix+"','"+suffix+"',"+power+",0)");
			return true;
		}catch(SQLException e){System.out.println(e.getMessage());}
		return false;
	}
	
	/**
	 * Supprime un grade dans la base de donnée.
	 * @param name
	 * 		Nom du grade.
	 * @see SQLManager#delete(String, String, String)
	 * @since 1.0
	 */
	public void removeRank(String name){
		delete(table_rank, table_rank_name, "'"+name+"'");
	}

	/**
	 * Regarde si le unique id du joueur existe bien dans la base de donnée.
	 * @param player
	 * 		Le joueur.
	 * @return 'true' s'il existe sinon 'false'
	 * @since 1.0
	 * @see SQLManager#select(String, String, String, String)
	 * @throws SQLException
	 */
	public boolean hasPlayer(Player player){
		try{
			ResultSet result = select("*", table_player, table_player_uuid, "'"+player.getUniqueId().toString()+"'");
			result.last();
			if(result.getRow() == 1){
				update(table_player, table_player_name, "'"+player.getName()+"'", table_player_uuid, "'"+player.getUniqueId().toString()+"'");
				return true;
			}
		}catch(SQLException e){System.out.println(e.getMessage());}
		return false;
	}
	
	/**
	 * Regarde si le nom du joueur existe bien dans la base de donnée.
	 * @param name
	 * 		Le nom du joueur.
	 * @return 'true' s'il existe sinon 'false'
	 * @since 1.0
	 * @see SQLManager#select(String, String, String, String)
	 * @throws SQLException
	 */
	public boolean hasPlayerName(String name){
		try{
			ResultSet result = select("*", table_player, table_player_name, "'"+name+"'");
			result.last();
			if(result.getRow() == 1) return true;
		}catch(SQLException e){System.out.println(e.getMessage());}
		return false;
	}

	/**
	 * Ajoute le joueur dans la base de donnée.
	 * @param player
	 * 		Le joueur.
	 * @since 1.0
	 * @throws SQLException
	 */
	public void addPlayer(Player player){
		try{
			String rank = getRankDefault();
			statement.executeUpdate("INSERT INTO "+table_player+" VALUES ('"+player.getUniqueId().toString()+"','"+player.getName()+"','"+rank+"')");
		}catch(SQLException e){System.out.println(e.getMessage());}
	}
	/**
	 * Recupere le grade d'un joueur.
	 * @param player
	 * 		Le joueur.
	 * @return Le nom du grade ou null s'il n'a pas de grade.
	 * @since 1.0
	 * @see SQLManager#select(String, String, String, String)
	 * @throws SQLException
	 */
	public String getPlayerTeam(Player player){
		try {
			ResultSet result = select(table_player_rank, table_player, table_player_uuid, "'"+player.getUniqueId().toString()+"'");
			result.next();
			return result.getString(table_player_rank).toLowerCase().equalsIgnoreCase("null") ? null : result.getString(table_player_rank);
		} catch (SQLException e) {System.out.println(e.getMessage());}
		return null;
	}
	
	/**
	 * Recupere le grade d'un joueur avec son nom.
	 * @param name
	 * 		Le nom du joueur.
	 * @return Le nom du grade ou null s'il n'a pas de grade.
	 * @since 1.0
	 * @see SQLManager#select(String, String, String, String)
	 * @throws SQLException
	 */
	public String getPlayerTeam(String name){
		try {
			ResultSet result = select(table_player_rank, table_player, table_player_name, "'"+name+"'");
			result.next();
			return result.getString(table_player_rank).toLowerCase().equalsIgnoreCase("null") ? null : result.getString(table_player_rank);
		} catch (SQLException e) {System.out.println(e.getMessage());}
		return null;
	}
	
	/**
	 * Ajoute un grade au joueur.
	 * @param name
	 * 		Le nom  du joueur.
	 * @param rank
	 * 		Le nom du grade
	 * @since 1.0
	 * @see SQLManager#update(String, String, String, String, String)
	 */
	public void setPlayerEditRank(String name, String rank){
		update(table_player, table_player_rank, "'"+rank+"'", table_player_name, "'"+name+"'");
	}

	/**
	 * Modifie le prefix d'un grade.
	 * @param name
	 * 		Le nom  du grade.
	 * @param prefix
	 * 		Le nouveau prefix.
	 * @since 1.0
	 * @see SQLManager#update(String, String, String, String, String)
	 */
	public void setPrefix(String name, String prefix){
		update(table_rank, table_rank_prefix, "'"+prefix+"'", table_rank_name, "'"+name+"'");
	}
	
	/**
	 * Modifie le suffix d'un grade.
	 * @param name
	 * 		Le nom  du grade.
	 * @param suffix
	 * 		Le nouveau suffix.
	 * @since 1.0
	 * @see SQLManager#update(String, String, String, String, String)
	 */
	public void setSuffix(String name, String suffix){
		update(table_rank, table_rank_suffix, "'"+suffix+"'", table_rank_name, "'"+name+"'");
	}
	
	/**
	 * Modifie le power d'un grade.
	 * @param name
	 * 		Le nom  du grade.
	 * @param power
	 * 		Le nouveau power.
	 * @since 1.0
	 * @see SQLManager#update(String, String, String, String, String)
	 */
	public void setPower(String name, int power){
		update(table_rank, table_rank_power, ""+power, table_rank_name, "'"+name+"'");
	}
	
	/**
	 * Modifie le grade par défaut.
	 * Ne pas appelé cette méthode en dehors de la class RankCommand {@link RankCommand}
	 * @param name
	 * 		Le nom  du grade.
	 * @param defaultRank
	 * 		'0' pour 'false' - '1' pour 'true'.
	 * @since 1.0
	 * @see SQLManager#update(String, String, String, String, String)
	 */
	public void setDefaultRank(String name, int defaultRank){
		update(table_rank, table_rank_default, ""+defaultRank, table_rank_name, "'"+name+"'");
	}
	
	/**
	 * Permet de faire une valeur dans la base de donnée.
	 * @param colone
	 * 		La colone de la table à recuperer.
	 * @param table
	 * 		La table dans lequel ce trouve la colone
	 * @param coloneKey
	 * 		La colone qui permetra de récuperer la valeur de la colone ci dessus.
	 * @param key
	 * 		La valeur de la coloneKey.
	 * @return ResultSet (Le resultat de la selection.)
	 * @since 1.0
	 * @throws SQLException
	 */
	private ResultSet select(String colone, String table, String coloneKey, String key){
		try{
			return statement.executeQuery("SELECT "+colone+" FROM "+table+" WHERE "+coloneKey+"="+key);
		}catch(SQLException e){System.out.println(e.getMessage());}
		return null;
	}
	
	/**
	 * Permet de faire toutes les valeurs d'une colonne dans la base de donnée.
	 * @param colone
	 * 		La colone des valeurs à récupérer.
	 * @param table
	 * 		La table ou ce trouve la colonne.
	 * @return ResultSet (Tout les resultat selectionné)
	 * @since 1.0
	 * @throws SQLException
	 */
	private ResultSet selectAll(String colone, String table){
		try{
			return statement.executeQuery("SELECT "+colone+" FROM "+table);
		}catch(SQLException e){System.out.println(e.getMessage());}
		return null;
	}
	
	/**
	 * Mets à jour une valeur dans la base de donnée.
	 * @param table
	 * 		La table ou ce trouve la valeur à mettre à jour.
	 * @param colone
	 * 		La colonne ou ce trouve la valeur à mettre à jour.
	 * @param result
	 * 		La nouvelle valeur.
	 * @param colonekey
	 * 		La colone ou ce trouve la key.
	 * @param key
	 * 		La valeur a prendre comme identifiant pour mettre à jour sa ligne.
	 * @since 1.0
	 * @throws SQLException
	 */
	private void update(String table, String colone, String result, String colonekey, String key){
		try{
			statement.executeUpdate("UPDATE "+table+" SET "+colone+"="+result+" WHERE "+colonekey+"="+key);
		}catch(SQLException e){System.out.println(e.getMessage());}
	}
	/**
	 * Permet de supprimé une valeur et sa ligne dans la base de donnée.
	 * @param table
	 * 		La table ou ce trouve la colonneKey.
	 * @param coloneKey
	 * 		La colone ou ce trouve la key.
	 * @param key
	 * 		La valeur à supprimer.
	 * @since 1.0
	 * @throws SQLException
	 */
	private void delete(String table, String coloneKey, String key){
		try {
			statement.executeUpdate("DELETE FROM "+table+" WHERE "+coloneKey+"="+key);
		} catch (SQLException e) {System.out.println(e.getMessage());}
	}
}