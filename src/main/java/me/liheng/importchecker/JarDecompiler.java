package me.liheng.importchecker;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class JarDecompiler {
    public static final Logger LOG = LoggerFactory.getLogger(JarDecompiler.class);

    private static Constants.decompileType type;

    public static void decompile(String jarPath) {
        System.out.println("Decompiling: " + jarPath + ". It may take a while...");
        LOG.info("Decompiling {}", jarPath);

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        OutputSinkFactory mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                // I only understand how to sink strings, regardless of what you have to give me.
                return Collections.singletonList(SinkClass.STRING);
            }

            @Override
            public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                return sinkType == SinkType.JAVA ? ps::println : ignore -> {};
            }
        };

        CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).build();
        driver.analyse(Collections.singletonList(jarPath));
        ps.flush();
        String [] javaLines =  baos.toString().split(System.lineSeparator());
        try {
            baos.close();
            ps.close();
        } catch (IOException e) {
            LOG.error("Error closing baos." ,e);
        }

        parseJavaLines(javaLines);
        System.out.println("Finished decompiling " + jarPath);
        LOG.info("Decompiled {} successfully.", jarPath);

    }

    public static void parseJavaLines(String [] javaLines) {
        StringBuilder sb = new StringBuilder();
        boolean afterPackage = false;
        List<String> imports = new ArrayList<>();

        for (String line : javaLines) {
            line = line.trim().replaceAll(" +", " ");

            if (line.startsWith("package ") && line.endsWith(";")) {
                String packageNameWithSemicolon = line.substring("package ".length());
                packageNameWithSemicolon = packageNameWithSemicolon.replaceAll(" +", "");
                String packageName = packageNameWithSemicolon.substring(0, packageNameWithSemicolon.length() - 1);
                sb.append(packageName);
                afterPackage = true;
            } else if (afterPackage && line.startsWith("import ") && line.endsWith(";")) {
                if (type == Constants.decompileType.DEPENDENCY || type == Constants.decompileType.SOURCE) {
                    String importNameWithSemicolon = line.substring("import ".length());
                    importNameWithSemicolon = importNameWithSemicolon.replaceAll(" +", "");
                    String importName = importNameWithSemicolon.substring(0, importNameWithSemicolon.length() - 1);
                    if (!importName.startsWith("java.")) {
                        imports.add(importName);
                        if (importName.endsWith(".*")) {
                            DataManager.getInstance().setNeedToResolveImportStar(true);
                        }
                    }
                }
            } else if (afterPackage
                    && (line.contains("class ") || line.contains("interface ") || line.contains("enum "))
                    && !line.startsWith("//") && !line.startsWith("/*") && !line.startsWith("*")) {

                String [] tokens = line.split(" ");
                int i;
                for (i = 0; i < tokens.length; i++) {
                    if (tokens[i].contains("class") || tokens[i].contains("interface") || tokens[i].contains("enum")) break;
                }
                sb.append(".").append(tokens[++i].split("<")[0]);
                switch (type) {
                    case DEPENDENCY:
                        DataManager.getInstance().getKnowledgeBase().put(sb.toString(),imports);
                        imports = new ArrayList<>();
                        break;
                    case SOURCE:
                        DataManager.getInstance().getClassesNeeded().add(sb.toString());
                        DataManager.getInstance().getKnowledgeBase().put(sb.toString(),imports);
                        imports = new ArrayList<>();
                        break;
                    case TARGET:
                        DataManager.getInstance().getTargetClasses().add(sb.toString());
                        break;
                }

                LOG.debug(sb.toString());
                afterPackage = false;
                sb.setLength(0);
            }
        }
    }

    public static void setDecompileType(Constants.decompileType type) {
        JarDecompiler.type = type;
    }
}
