package me.liheng.importchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE;
    private boolean needToResolveImportStar;
    private String testProjectPath;
    private String testJarPath;
    private String targetJarPath;
    private List<String> jarPaths;
    private HashMap<String, List<String>> knowledgeBase;
    private HashSet<String> classesNeeded;
    private HashSet<String> targetClasses;
    private HashSet<String> visited;

    private DataManager() {
        needToResolveImportStar = false;
        jarPaths = new ArrayList<>();
        knowledgeBase = new HashMap<>();
        classesNeeded = new HashSet<>();
        targetClasses = new HashSet<>();
        visited = new HashSet<>();
    }

    public static DataManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public boolean isNeedToResolveImportStar() {
        return needToResolveImportStar;
    }

    public void setNeedToResolveImportStar(boolean needToResolveImportStar) {
        this.needToResolveImportStar = needToResolveImportStar;
    }

    public String getTestProjectPath() {
        return testProjectPath;
    }

    public void setTestProjectPath(String testProjectPath) {
        this.testProjectPath = testProjectPath;
    }

    public String getTestJarPath() {
        return testJarPath;
    }

    public void setTestJarPath(String testJarPath) {
        this.testJarPath = testJarPath;
    }

    public String getTargetJarPath() {
        return targetJarPath;
    }

    public void setTargetJarPath(String targetJarPath) {
        this.targetJarPath = targetJarPath;
    }

    public List<String> getJarPaths() {
        return jarPaths;
    }

    public HashMap<String, List<String>> getKnowledgeBase() {
        return knowledgeBase;
    }

    public HashSet<String> getClassesNeeded() {
        return classesNeeded;
    }

    public HashSet<String> getTargetClasses() {
        return targetClasses;
    }

    public HashSet<String> getVisited() {
        return visited;
    }
}
