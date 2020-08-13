package me.liheng.importchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ImportsResolver {
    public static final Logger LOG = LoggerFactory.getLogger(ImportsResolver.class);

    public static void resolveImportStar() {
        LOG.debug("Resolving import .*");
        System.out.println("Resolving import .* ...");

        for (String key: DataManager.getInstance().getKnowledgeBase().keySet()) {
            List<String> stars = new ArrayList<>();

            for (String importClass : DataManager.getInstance().getKnowledgeBase().get(key)) {
                if (importClass.endsWith(".*")) {
                    stars.add(importClass);
                }
            }

            for (String starLine : stars) {
                LOG.debug("Resolving {}", starLine);
                String starLineFront = starLine.substring(0, starLine.length() - 2);
                for (String classLine: DataManager.getInstance().getKnowledgeBase().keySet()) {
                    if (classLine.startsWith(starLineFront)) {
                        DataManager.getInstance().getKnowledgeBase().get(key).add(classLine);
                    }
                }
                DataManager.getInstance().getKnowledgeBase().get(key).remove(starLine);
            }
        }
    }

    public static void resolveRecursiveImports() {
        System.out.println("Resolving recursive imports ...");
        LOG.info("Resolving recursive imports with DFS...");

        for (String initialClass: DataManager.getInstance().getClassesNeeded()) {
            dfs(initialClass);
        }

        System.out.println("Finished resolving recursive imports.");
        LOG.info("Finished resolving recursive imports with DFS.");
    }

    private static void dfs(String className) {
        DataManager.getInstance().getVisited().add(className);
        for (String importClass: DataManager.getInstance().getKnowledgeBase().getOrDefault(className, new ArrayList<>())) {
            if (!DataManager.getInstance().getVisited().contains(importClass)) {
                dfs(importClass);
            }
        }
    }

}
