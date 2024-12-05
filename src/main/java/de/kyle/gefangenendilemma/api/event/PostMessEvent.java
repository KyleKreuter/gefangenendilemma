package de.kyle.gefangenendilemma.api.event;

import de.kyle.gefangenendilemma.api.result.PrisonerMessResult;


/**
 * The PostMessEvent represents an event that is triggered after a round of the dilemma.
 * It contains information about the opponent, the outcome of the round, and the points awarded.
 *
 * @param opponent The opponent the round was played against.
 * @param result The result of the opponent's decision (either COOPERATE or BETRAY).
 * @param points The number of points earned in this round.
 */
public record PostMessEvent(
        String opponent,
        PrisonerMessResult result,
        int points
) {
}
