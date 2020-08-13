package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultsAnalyser {
    public static final Logger LOG = LoggerFactory.getLogger(ResultsAnalyser.class);

    public static void analyse() {
        System.out.println("Analysing results...");
        LOG.info("Analysing results...");

        DataManager.getInstance().getIntersection().addAll(DataManager.getInstance().getVisited());
        DataManager.getInstance().getIntersection().retainAll(DataManager.getInstance().getTargetClasses());

        if (DataManager.getInstance().getIntersection().isEmpty()) {
            LOG.info("No intersection found.\n{} does not recursively import classes from {}.",
                    DataManager.getInstance().getTestProjectPath(), DataManager.getInstance().getTargetJarPath());
            System.out.println(String.format("No intersection found.\n%s does not recursively import classes from %s.",
                    DataManager.getInstance().getTestProjectPath(), DataManager.getInstance().getTargetJarPath()));
        } else {
            LOG.info("Intersection found.\n{} recursively imports these classes from {}: ",
                    DataManager.getInstance().getTestProjectPath(), DataManager.getInstance().getTargetJarPath());
            LOG.info("{}", DataManager.getInstance().getIntersection());
            System.out.println(String.format("Intersection found.\n%s recursively imports these classes from %s: ",
                    DataManager.getInstance().getTestProjectPath(), DataManager.getInstance().getTargetJarPath()));
            for (String result: DataManager.getInstance().getIntersection()) {
                System.out.println(result);
            }
        }
    }
}
