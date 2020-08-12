package me.liheng.importchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE;
    private String testProjectPath;
    private String targetJarPath;
    private List<String> jarPaths;
    private HashMap<String, List<String>> knowledgeBase;
    private HashSet<String> classesNeeded;
    private HashSet<String> targetClasses;

    private DataManager() {
        jarPaths = new ArrayList<>();
        knowledgeBase = new HashMap<>();
        classesNeeded = new HashSet<>();
        targetClasses = new HashSet<>();
    }

    public static DataManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public String getTestProjectPath() {
        return testProjectPath;
    }

    public void setTestProjectPath(String testProjectPath) {
        this.testProjectPath = testProjectPath;
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
}
