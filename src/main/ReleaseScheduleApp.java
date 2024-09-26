import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReleaseScheduleApp {
    private static final String INPUT_FILE_NAME = "releases.txt";
    private static final String OUTPUT_FILE_NAME = "solution.txt";
    private static final int SPRINT_DAYS = 10;
    private static final String SPACE = " ";

    public static void main(String[] args) {
        String resourcesDir = System.getProperty("user.dir") + "/src/resources/";
        String inputPath = resourcesDir + INPUT_FILE_NAME;

        List<Release> releases = readInputData(inputPath);
        List<Release> sortedReleases = sortReleasesByEndDay(releases);
        List<Release> solutionReleases = collectMaxNumberReleases(sortedReleases);

        String outputPath = resourcesDir + OUTPUT_FILE_NAME;
        writeSolution(solutionReleases, outputPath);
    }

    /**
     * Collect maximum number of releases using Greedy algorithm for Interval Scheduling
     *
     * @param sortedReleases - releases list sorted by end day
     */
    private static List<Release> collectMaxNumberReleases(List<Release> sortedReleases) {
        List<Release> solutionReleases = new ArrayList<>();

        Release latestRelease = sortedReleases.get(0);
        if (latestRelease.isFitInSprint(SPRINT_DAYS)) {
            solutionReleases.add(latestRelease);
        }
        for (int i = 1; i < sortedReleases.size(); i++) {
            Release releaseToCheck = sortedReleases.get(i);
            if (!releaseToCheck.isFitInSprint(SPRINT_DAYS)) {
                break;
            }
            if (!latestRelease.hasIntersection(releaseToCheck)) {
                solutionReleases.add(releaseToCheck);
                latestRelease = releaseToCheck;
            }
        }

        return solutionReleases;
    }

    private static List<Release> sortReleasesByEndDay(List<Release> releases) {
        return releases.stream()
                .sorted(Comparator.comparing(Release::endDay))
                .toList();
    }

    private static List<Release> readInputData(String inputPath) {
        Path releaseDataPath = Paths.get(inputPath);
        List<String> releaseData = readStringInputData(releaseDataPath);

        return releaseData.stream()
                .map(line -> {
                    String[] days = line.split(SPACE);
                    return new Release(days[0], days[1]);
                })
                .toList();
    }

    private static List<String> readStringInputData(Path inputPath) {
        try {
            return Files.readAllLines(inputPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to read a file at %s path.%n Exception message: %s",
                    inputPath, e.getMessage()));
        }
    }

    private static void writeSolution(List<Release> finalReleases, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(finalReleases.size() + "\n");
            for (Release finalRelease : finalReleases) {
                writer.write(String.format("%s %s\n", finalRelease.startDay(), finalRelease.endDay()));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to write to a file at %s path.%n Exception message: %s",
                    outputPath, e.getMessage()));
        }
    }
}