package fr.ranksql.neutronstars.error;

import fr.ranksql.neutronstars.utils.RankUtils;

/**
 * Exception si la Class SQLManager existe déjà dans la class RankUtils
 * 
 * @see RankUtils
 * @author Neutron_Stars
 * @version 1.0
 */

public class ClassAlreadyExistingException extends RuntimeException {
	private static final long serialVersionUID = 271120162041L;
	
	/**
	 * Constucteur de la class.
	 * @param error
	 * 			Message de l'exception.
	 */
	public ClassAlreadyExistingException(String error) {
		super(error);
	}

}
