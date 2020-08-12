package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
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
                System.out.println("Please enter the path to the artifact jar that you want to test on: ");
                System.out.println("(for example: /Users/liheng/.m2/repository/net/sf/jasperreports/jasperreports/6.13.0 )");
                DataManager.getInstance().setTestProjectPath(scanner.nextLine());
            } while (DataManager.getInstance().getTestProjectPath().isEmpty() || !Files.exists(Paths.get(DataManager.getInstance().getTestProjectPath())));
            LOG.info("User input test jar path as {}.", DataManager.getInstance().getTestProjectPath());

            new MvnDependencyListRunner().run();

            for (String jarPath : DataManager.getInstance().getJarPaths()) {
                JarDecompiler.setIncludeImports(true);
                JarDecompiler.decompile(jarPath);
            }
        }

    }
}
