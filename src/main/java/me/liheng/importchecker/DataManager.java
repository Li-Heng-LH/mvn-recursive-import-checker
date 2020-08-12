package me.liheng.importchecker;

public class DataManager {

    private static DataManager INSTANCE;
    private String testProjectPath;

    private DataManager() {}

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
}
