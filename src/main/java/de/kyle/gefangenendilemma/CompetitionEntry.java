package de.kyle.gefangenendilemma;

import de.kyle.gefangenendilemma.competition.Competition;

import java.io.IOException;

public class CompetitionEntry {
    public static void main(String[] args) throws IOException {
        Competition competition = new Competition();
        competition.start();
    }
}
