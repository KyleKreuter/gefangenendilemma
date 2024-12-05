package de.kyle.gefangenendilemma.competition;

import de.kyle.gefangenendilemma.api.Prisoner;
import de.kyle.gefangenendilemma.api.event.PostMessEvent;
import de.kyle.gefangenendilemma.api.result.PrisonerMessResult;
import de.kyle.gefangenendilemma.competition.config.CompetitionSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class CompetitionTest {

    private Prisoner mockPrisoner1;
    private Prisoner mockPrisoner2;
    private CompetitionSpecification competitionSpecification;

    @BeforeEach
    public void setUp() throws IOException {
        mockPrisoner1 = Mockito.mock(Prisoner.class);
        mockPrisoner2 = Mockito.mock(Prisoner.class);

        when(mockPrisoner1.getName()).thenReturn("Prisoner 1");
        when(mockPrisoner2.getName()).thenReturn("Prisoner 2");

        when(mockPrisoner1.messAround(mockPrisoner2.getName())).thenReturn(PrisonerMessResult.COOPERATE);
        when(mockPrisoner2.messAround(mockPrisoner1.getName())).thenReturn(PrisonerMessResult.BETRAY);

        competitionSpecification = new CompetitionSpecification();
    }

    @Test
    public void testCompetitionStart() {
        Set<Prisoner> prisoners = new HashSet<>();
        prisoners.add(mockPrisoner1);
        prisoners.add(mockPrisoner2);

        Competition competition = new Competition(prisoners, competitionSpecification);

        verify(mockPrisoner1, atLeast(200)).messAround(mockPrisoner2.getName());
        verify(mockPrisoner2, atLeast(200)).messAround(mockPrisoner1.getName());

        verify(mockPrisoner1, atLeast(200)).onPostMessEvent(any(PostMessEvent.class));
        verify(mockPrisoner2, atLeast(200)).onPostMessEvent(any(PostMessEvent.class));
    }

}
