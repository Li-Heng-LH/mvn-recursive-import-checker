package me.liheng.importchecker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MvnDependencyListRunner {
    public static final Logger LOG = LoggerFactory.getLogger(MvnDependencyListRunner.class);

    private boolean needToDeletePom = false;
    private String pomXmlPath;

    public void run() {
        copyPomFileIfNeeded();
        runMvnDependencyList();
        deletePomFileIfNeeded();
    }

    private void copyPomFileIfNeeded() {
        pomXmlPath = DataManager.getInstance().getTestProjectPath() + File.separator + "pom.xml";
        Path originalPath = Paths.get("");

        if (! new File(pomXmlPath).exists()) {
            File dir = new File(DataManager.getInstance().getTestProjectPath());
            for (File file : dir.listFiles()) {
                if (file.getName().toLowerCase().endsWith((".pom"))) {
                    originalPath = file.toPath();
                    break;
                }
            }
            Path copiedPomPath = Paths.get(pomXmlPath);
            try {
                Files.copy(originalPath, copiedPomPath);
                needToDeletePom = true;
                LOG.info("Copied pom file from {} to {}", originalPath, pomXmlPath);
            } catch (IOException e) {
                LOG.error("Failed to copy pom file from {} to {}", originalPath, pomXmlPath, e);
                System.out.println(Constants.EXIT_WITH_ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    private void runMvnDependencyList() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File(DataManager.getInstance().getTestProjectPath()));
        builder.command("mvn", "dependency:list", "-DoutputAbsoluteArtifactFilename");
        LOG.info("Running mvn dependency:list on {}", pomXmlPath);
        System.out.println("Running mvn dependency:list on " + pomXmlPath + " ...");
        try {
            Process process = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String dependencyLine;
            while ((dependencyLine = r.readLine()) != null) {
                LOG.debug(dependencyLine);

                if ((StringUtils.countMatches(dependencyLine, ":") >= 5) &&
                        (dependencyLine.contains(":compile:") || dependencyLine.contains(":provided:") ||
                                dependencyLine.contains(":system:") || dependencyLine.contains(":import:"))) {

                    String[] tokens = dependencyLine.split(":");
                    System.out.println((tokens[tokens.length - 1])); ////
                }
            }
            process.waitFor();
        } catch (Exception e) {
            LOG.error("Error running mvn dependency:list.", e);
            System.out.println(Constants.EXIT_WITH_ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void deletePomFileIfNeeded() {
        if (needToDeletePom) {
            try {
                Files.delete(Paths.get(pomXmlPath));
                LOG.info("Deleted {}.", pomXmlPath);
            } catch (IOException e) {
                LOG.error("Error deleting {}.", pomXmlPath, e);
            }
        }
        needToDeletePom = false;
    }
}
