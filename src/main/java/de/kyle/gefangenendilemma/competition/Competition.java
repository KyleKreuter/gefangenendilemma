package de.kyle.gefangenendilemma.competition;

import de.kyle.gefangenendilemma.api.Prisoner;
import de.kyle.gefangenendilemma.api.event.PostMessEvent;
import de.kyle.gefangenendilemma.api.result.PrisonerMessResult;
import de.kyle.gefangenendilemma.competition.config.CompetitionSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public final class Competition {

    private static final Logger log = LoggerFactory.getLogger(Competition.class);
    private final Set<Prisoner> competitors = new HashSet<>();
    private final Map<Prisoner, Integer> scores = new HashMap<>();
    private final CompetitionSpecification competitionSpecification;

    public Competition() throws IOException {
        competitionSpecification = new CompetitionSpecification();
    }

    public Competition(Set<Prisoner> prisoners, CompetitionSpecification specification) {
        log.warn("You've invoked the testing constructor; this constructor is for testing-purpose only");
        this.competitors.addAll(prisoners);
        this.competitionSpecification = specification;
        this.scores.putAll(prisoners.stream().collect(Collectors.toMap(prisoner -> prisoner, prisoner -> 0)));
        letTheMessBegin();
        evaluateTheMess();
    }

    public void start() {
        loadCompetitors();
        letTheMessBegin();
        evaluateTheMess();
    }

    private void loadCompetitors() {
        File competitorsFolder = new File("competitors");
        if (!competitorsFolder.exists()) {
            competitorsFolder.mkdir();
        }
        File[] competitorJars = competitorsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (competitorJars == null || competitorJars.length == 0) {
            log.warn("No competitors found. Shutting down.");
            System.exit(1);
            return;
        }

        try {
            URL[] jarUrls = new URL[competitorJars.length];
            for (int i = 0; i < competitorJars.length; i++) {
                jarUrls[i] = competitorJars[i].toURI().toURL();
            }

            try (URLClassLoader classLoader = new URLClassLoader(jarUrls, this.getClass().getClassLoader())) {
                for (File jarFile : competitorJars) {
                    try (JarFile jar = new JarFile(jarFile)) {
                        JarEntry entry = jar.getJarEntry("prisoner.properties");
                        if (entry == null) {
                            log.warn("Could not find prisoner.properties in {}", jarFile.getName());
                            continue;
                        }

                        try (InputStream inputStream = jar.getInputStream(entry)) {
                            Properties properties = new Properties();
                            properties.load(inputStream);

                            String prisonerEntry = properties.getProperty("prisoner.entry");
                            if (prisonerEntry == null) {
                                log.warn("Could not find prisoner.entry in prisoner.properties");
                                continue;
                            }
                            String prisonerName = properties.getProperty("prisoner.name");
                            if (prisonerName == null) {
                                log.warn("Could not find prisoner.name in prisoner.properties");
                                continue;
                            }

                            Class<?> clazz = classLoader.loadClass(prisonerEntry);
                            Object oi = clazz.getDeclaredConstructor().newInstance();
                            if (!(oi instanceof Prisoner prisoner)) {
                                log.warn("{} did not specify a valid entrypoint", prisonerName);
                                continue;
                            }
                            competitors.add(prisoner);
                            scores.put(prisoner, 0);
                        }
                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException e) {
                        log.error("Error loading competitor from {}: {}", jarFile.getName(), e.getMessage(), e);
                    }
                }
            }
        } catch (MalformedURLException e) {
            log.error("Malformed URL while trying to load competitors: {}", e.getMessage(), e);
        } catch (IOException e) {
            log.error("IO Exception while trying to create URLClassLoader: {}", e.getMessage(), e);
        }
    }

    private void letTheMessBegin() {
        if (competitors.isEmpty()) {
            log.warn("No competitors were registered. Shutting down.");
            System.exit(1);
            return;
        }
        for (Prisoner competitor : competitors) {
            for (Prisoner opponent : competitors) {
                if (competitor.equals(opponent)) {
                    continue;
                }
                log.info("{} will compete against {}", competitor.getName(), opponent.getName());
                log.info("Points before competition:");
                log.info("{}: {}", competitor.getName(), scores.get(competitor));
                log.info("{}: {}", opponent.getName(), scores.get(opponent));
                for (int i = 0; i < competitionSpecification.getRounds(); i++) {
                    PrisonerMessResult competitorMessResult = competitor.messAround(opponent.getName());
                    PrisonerMessResult opponentMessResult = opponent.messAround(competitor.getName());
                    switch (competitorMessResult) {
                        case BETRAY -> {
                            if (opponentMessResult.equals(PrisonerMessResult.BETRAY)) {
                                competitor.onPostMessEvent(new PostMessEvent(opponent.getName(), opponentMessResult, 0));
                                opponent.onPostMessEvent(new PostMessEvent(competitor.getName(), competitorMessResult, 0));
                            } else {
                                competitor.onPostMessEvent(new PostMessEvent(opponent.getName(), opponentMessResult, competitionSpecification.getBetrayPoints()));
                                modifyPoints(competitor, competitionSpecification.getBetrayPoints());
                                opponent.onPostMessEvent(new PostMessEvent(competitor.getName(), competitorMessResult, 0));
                            }
                        }
                        case COOPERATE -> {
                            if (opponentMessResult.equals(PrisonerMessResult.COOPERATE)) {
                                competitor.onPostMessEvent(new PostMessEvent(opponent.getName(), opponentMessResult, competitionSpecification.getCooperatePoints()));
                                opponent.onPostMessEvent(new PostMessEvent(competitor.getName(), competitorMessResult, competitionSpecification.getCooperatePoints()));
                                modifyPoints(competitor, competitionSpecification.getCooperatePoints());
                                modifyPoints(opponent, competitionSpecification.getCooperatePoints());
                            } else {
                                competitor.onPostMessEvent(new PostMessEvent(opponent.getName(), opponentMessResult, 0));
                                opponent.onPostMessEvent(new PostMessEvent(competitor.getName(), competitorMessResult, competitionSpecification.getBetrayPoints()));
                                modifyPoints(opponent, competitionSpecification.getBetrayPoints());
                            }
                        }
                    }
                }
                log.info("Points after competition:");
                log.info("{}: {}", competitor.getName(), scores.get(competitor));
                log.info("{}: {}", opponent.getName(), scores.get(opponent));
            }
        }
        log.info("Competition cycle is done");
    }

    private void evaluateTheMess() {
        log.info(" ");
        log.info("Evaluation:");
        List<Map.Entry<Prisoner, Integer>> result = scores.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
        int place = 1;
        for (Map.Entry<Prisoner, Integer> prisonerPointsMap : result) {
            Prisoner prisoner = prisonerPointsMap.getKey();
            log.info("{} scored {} points and is on place #{}", prisoner.getName(), prisonerPointsMap.getValue(), place);
            place++;
        }
        log.info(" ");
        log.info("Finished evaluation");
    }

    private void modifyPoints(Prisoner prisoner, int points) {
        scores.compute(prisoner, (prisoner_, integer) -> {
            if (integer == null) {
                return 0;
            }
            return integer + points;
        });
    }
}
