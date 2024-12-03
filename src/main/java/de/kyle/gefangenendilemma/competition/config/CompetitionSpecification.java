package de.kyle.gefangenendilemma.competition.config;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class CompetitionSpecification {
    private final int rounds;
    private final int cooperatePoints;
    private final int betrayPoints;

    public CompetitionSpecification() throws IOException {
        Properties properties = new Properties();
        File specificationFolder = new File("specification");
        if (specificationFolder.mkdir()) {
            properties.load(getClass().getResourceAsStream("/DefaultCompetitionSpecification.properties"));
        } else {
            File[] configs = specificationFolder.listFiles((dir, name) -> name.endsWith(".properties"));
            if (configs == null || configs.length == 0) {
                properties.load(getClass().getResourceAsStream("/DefaultCompetitionSpecification.properties"));
            } else {
                properties.load(new FileInputStream(configs[0]));
            }
        }
        rounds = Integer.parseInt(properties.getProperty("competition.rounds"));
        cooperatePoints = Integer.parseInt(properties.getProperty("competition.points.cooperate"));
        betrayPoints = Integer.parseInt(properties.getProperty("competition.points.betray"));
    }
}
