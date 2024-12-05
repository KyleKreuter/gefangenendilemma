package de.kyle.gefangenendilemma.api;

import de.kyle.gefangenendilemma.api.event.PostMessEvent;
import de.kyle.gefangenendilemma.api.result.PrisonerMessResult;


/**
 * The Prisoner interface represents a participant in the Prisoner's Dilemma competition.
 * Each participant must implement the following methods to take part in the competition.
 */
public interface Prisoner {
    /**
     * Should return the name of the prisoner.
     * This method should provide a unique identification for each participant.
     *
     * @return The name of the given prisoner.
     */
    String getName();


    /**
     * Represents the action of the prisoner during a round of the dilemma.
     * This method is called each round to determine whether the prisoner will "COOPERATE" or "BETRAY".
     *
     * @param opponent The opponent you're about to mess with.
     * @return The decision result of the prisoner as a PrisonerMessResult.
     */
    PrisonerMessResult messAround(String opponent);

    /**
     * This method is called after each round of the competition to provide the result of that round.
     * The prisoner receives information about the opponent's decision and the points obtained during the round.
     * This information can be used in future rounds.
     *
     * @param postMessEvent An event containing information about the opponent's action and the points earned.
     */
    void onPostMessEvent(PostMessEvent postMessEvent);
}