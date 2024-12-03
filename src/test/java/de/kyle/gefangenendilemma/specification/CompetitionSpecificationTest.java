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

    @Test
    public void example_specification_is_used() {
        File folder = new File("specification");
        folder.mkdir();
        File specificationFile = new File(folder, "custom_specification.properties");
        Assertions.assertDoesNotThrow(() -> {
            if (specificationFile.exists()) {
                specificationFile.delete();
            }
            specificationFile.createNewFile();
            try (FileWriter fileWriter = new FileWriter(specificationFile)) {
                fileWriter.write("competition.rounds=10\n" +
                        "competition.points.cooperate=5\n" +
                        "competition.points.betray=20");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CompetitionSpecification specification = new CompetitionSpecification();
            Assertions.assertEquals(5, specification.getCooperatePoints());
            specificationFile.delete();
            folder.delete();
        });
    }
}
