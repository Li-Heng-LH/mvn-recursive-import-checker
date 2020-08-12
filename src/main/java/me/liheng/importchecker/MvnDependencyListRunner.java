package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MvnDependencyListRunner {
    public static final Logger LOG = LoggerFactory.getLogger(MvnDependencyListRunner.class);

    boolean needToDeletePom = false;

    public void run() {
        copyPomFileIfNeeded();
    }

    private void copyPomFileIfNeeded() {
        Path originalPath = Paths.get("");

        if (! new File(DataManager.getInstance().getTestProjectPath() + File.separator + "pom.xml").exists()) {

            File dir = new File(DataManager.getInstance().getTestProjectPath());
            for (File file : dir.listFiles()) {
                if (file.getName().toLowerCase().endsWith((".pom"))) {
                    originalPath = file.toPath();
                    break;
                }
            }
            Path copiedPomPath = Paths.get(DataManager.getInstance().getTestProjectPath() + File.separator + "pom.xml");
            try {
                Files.copy(originalPath, copiedPomPath);
                needToDeletePom = true;
                LOG.info("Copied pom file from {} to {}", originalPath,DataManager.getInstance().getTestProjectPath() + File.separator + "pom.xml");
            } catch (IOException e) {
                LOG.error("Failed to copy pom file from {} to {}", originalPath,DataManager.getInstance().getTestProjectPath() + File.separator + "pom.xml", e);
                System.out.println(Constants.EXIT_WITH_ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
