package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CheckerRunner {
    public static final Logger LOG = LoggerFactory.getLogger(CheckerRunner.class);

    private int testType = 0;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.BANNER);

        do {
            System.out.println("Press 1 if you are testing on a third-party jar. ");
            System.out.println("Press 2 if you are testing on a development project. ");
            String input = scanner.nextLine();
            try {
                testType = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                testType = 0;
            }
        } while (testType != 1 && testType != 2);
        LOG.info("User input testType as {}.", testType);

        if (testType == 1) {
            //test on third-party jar
            do {
                System.out.println("Please enter the directory path to the artifact jar that you want to test on: ");
                System.out.println("(for example: /Users/liheng/.m2/repository/net/sf/jasperreports/jasperreports/6.13.0) ");
                DataManager.getInstance().setTestProjectPath(scanner.nextLine());
            } while (DataManager.getInstance().getTestProjectPath().isEmpty() || !Files.exists(Paths.get(DataManager.getInstance().getTestProjectPath())));
            LOG.info("User input test project path as {}.", DataManager.getInstance().getTestProjectPath());

            do {
                System.out.println("Please enter the file path to the artifact jar that you want to test on: ");
                System.out.println("(for example: /Users/liheng/.m2/repository/net/sf/jasperreports/jasperreports/6.13.0/jasperreports-6.13.0.jar) ");
                DataManager.getInstance().setTestJarPath(scanner.nextLine());
            } while (DataManager.getInstance().getTestJarPath().isEmpty() || !Files.exists(Paths.get(DataManager.getInstance().getTestJarPath())));
            LOG.info("User input test jar path as {}.", DataManager.getInstance().getTestJarPath());

            do {
                System.out.println("Please enter the file path to the artifact jar that you want to see if it is being imported: ");
                System.out.println("(for example: /Users/liheng/.m2/repository/commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar) ");
                DataManager.getInstance().setTargetJarPath(scanner.nextLine());
            } while (DataManager.getInstance().getTargetJarPath().isEmpty() || !Files.exists(Paths.get(DataManager.getInstance().getTargetJarPath())));
            LOG.info("User input target jar path as {}.", DataManager.getInstance().getTargetJarPath());

            decompileSourceJar();
            decompileDependencyJars();
            decompileTargetJar();

            if (DataManager.getInstance().isNeedToResolveImportStar()) {
                ImportsResolver.resolveImportStar();
            }


            // Debug
            System.out.println("************************");
            for (String s : DataManager.getInstance().getClassesNeeded()) {
                System.out.println(s);
            }
            System.out.println(DataManager.getInstance().getClassesNeeded().size());

            System.out.println("************************");
            for (Map.Entry<String, List<String>> entry: DataManager.getInstance().getKnowledgeBase().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println(DataManager.getInstance().getKnowledgeBase().size());

            System.out.println("************************");
            for (String s : DataManager.getInstance().getTargetClasses()) {
                System.out.println(s);
            }
            System.out.println(DataManager.getInstance().getTargetClasses().size());
        }

    }

    private void decompileSourceJar() {
        JarDecompiler.setDecompileType(Constants.decompileType.SOURCE);
        JarDecompiler.decompile(DataManager.getInstance().getTestJarPath());
    }

    private void decompileDependencyJars() {
        new MvnDependencyListRunner().run();
        for (String jarPath : DataManager.getInstance().getJarPaths()) {
            JarDecompiler.setDecompileType(Constants.decompileType.DEPENDENCY);
            JarDecompiler.decompile(jarPath);
        }
    }

    private void decompileTargetJar() {
        JarDecompiler.setDecompileType(Constants.decompileType.TARGET);
        JarDecompiler.decompile(DataManager.getInstance().getTargetJarPath());
    }
}
