package de.kyle.gefangenendilemma.specification;

import de.kyle.gefangenendilemma.competition.config.CompetitionSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompetitionSpecificationTest {
    @Test
    public void default_specification_is_used_on_startup() {
        Assertions.assertDoesNotThrow(() -> {
            CompetitionSpecification competitionSpecification = new CompetitionSpecification();
            Assertions.assertEquals(100, competitionSpecification.getRounds());
        });
    }

    @Test
    public void specification_folder_is_created_on_startup() {
        Assertions.assertDoesNotThrow(() -> {
            new CompetitionSpecification();
            Assertions.assertTrue(new File("specification").exists());
        });
    }

    @Test
    public void default_specification_is_used_if_specification_folder_is_empty() {
        Assertions.assertDoesNotThrow(() -> {
            new CompetitionSpecification();
            CompetitionSpecification competitionSpecification_ = new CompetitionSpecification();
            Assertions.assertEquals(100, competitionSpecification_.getRounds());
        });
    }
}
