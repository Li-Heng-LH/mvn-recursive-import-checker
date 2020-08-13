package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesWalker {
    public static final Logger LOG = LoggerFactory.getLogger(FilesWalker.class);

    public static void walkDirectory() {
        for (String javaFilePath: getJavaFilePaths()) {
            parseJavaFile(javaFilePath);
        }
    }

    private static Set<String> getJavaFilePaths() {
        String directory = DataManager.getInstance().getTestProjectPath()
                + File.separator + "src"
                + File.separator + "main"
                + File.separator + "java";
        LOG.info("Walking through directory {}", directory);
        System.out.println("Walking through directory: " + directory);

        try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(file -> file.toFile().getAbsolutePath())
                    .filter(file -> file.toLowerCase().endsWith(".java"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOG.error("Error walking the project directory.", e);
            System.out.println(Constants.EXIT_WITH_ERROR_MESSAGE);
            System.exit(1);
        }
        return new HashSet<>();
    }

    private static void parseJavaFile (String file) {
        LOG.debug("Parsing java file {}", file);
        BufferedReader inputStream = null;
        List<String> lines = new ArrayList<>();
        try {
            inputStream = new BufferedReader(new FileReader(file));
            String line;
            while ((line = inputStream.readLine()) != null) {
                lines.add(line);
            }
            if (inputStream != null) {
                inputStream.close();
            }

            String[] linesArray = new String[lines.size()];
            linesArray = lines.toArray(linesArray);

            JarDecompiler.setDecompileType(Constants.decompileType.SOURCE);
            JarDecompiler.parseJavaLines(linesArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
