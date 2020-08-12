package me.liheng.importchecker;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager INSTANCE;
    private String testProjectPath;
    private List<String> jarPaths;

    private DataManager() {
        jarPaths = new ArrayList<>();
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

    public List<String> getJarPaths() {
        return jarPaths;
    }
}
