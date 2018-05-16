package com.trivago.rta.runners;

import com.trivago.rta.files.FileIO;
import com.trivago.rta.vo.FeatureRunner;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RunnerFileContentRendererTest {
    private FileIO fileIO;
    private RunnerFileContentRenderer runnerFileContentRenderer;

    @Before
    public void setup() {
        fileIO = mock(FileIO.class);
        runnerFileContentRenderer = new RunnerFileContentRenderer(fileIO);
    }

    @Test
    public void getRenderedFeatureFileContentFromTextFileTest() throws Exception {
        String template = "package parallel;\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/[CUCABLE:FEATURE].feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/[CUCABLE:RUNNER].json\"}\n" +
                ")\n" +
                "public class [CUCABLE:RUNNER] {\n" +
                "}\n";

        when(fileIO.readContentFromFile(anyString())).thenReturn(template);

        String expectedOutput = "package parallel;\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/featureFileName.feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/featureFileName.json\"}\n" +
                ")\n" +
                "public class featureFileName {\n" +
                "}\n" +
                "\n" +
                "\n" +
                "// Source Feature: \n" +
                "// Generated by Cucable from pathToTemplate\n";

        ArrayList<String> featureFileNames = new ArrayList<>();
        featureFileNames.add("featureFileName");

        FeatureRunner featureRunner = new FeatureRunner(
                "pathToTemplate", "RunnerClass", featureFileNames
        );
    }

    @Test
    public void getRenderedFeatureFileContentReplaceBackslashInCommentTest() throws Exception {
        String template = "package parallel;\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/[CUCABLE:FEATURE].feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/[CUCABLE:RUNNER].json\"}\n" +
                ")\n" +
                "public class [CUCABLE:RUNNER] {\n" +
                "}\n";

        when(fileIO.readContentFromFile(anyString())).thenReturn(template);

        String expectedOutput = "package parallel;\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/featureFileName.feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/RunnerClass.json\"}\n" +
                ")\n" +
                "public class RunnerClass {\n" +
                "}\n" +
                "\n" +
                "\n" +
                "// Generated by Cucable from c:/unknown/path\n";

        ArrayList<String> featureFileNames = new ArrayList<>();
        featureFileNames.add("featureFileName");

        FeatureRunner featureRunner = new FeatureRunner(
                "c:\\unknown\\path", "RunnerClass", featureFileNames
        );

        String renderedRunnerFileContent = runnerFileContentRenderer.getRenderedRunnerFileContent(featureRunner);

        assertThat(renderedRunnerFileContent, is(expectedOutput));
    }


    @Test
    public void getRenderedFeatureFileContentFromJavaFileTest() throws Exception {

        String template = "package parallel;\n" +
                "\n" +
                "package some.package;\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/[CUCABLE:FEATURE].feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/[CUCABLE:RUNNER].json\"}\n" +
                ")\n" +
                "public class MyClass {\n" +
                "}\n";

        when(fileIO.readContentFromFile(anyString())).thenReturn(template);

        String expectedOutput = "\n" +
                "\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/featureFileName.feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/RunnerClass.json\"}\n" +
                ")\n" +
                "public class RunnerClass {\n" +
                "}\n" +
                "\n" +
                "\n" +
                "// Generated by Cucable from MyClass.java\n";


        ArrayList<String> featureFileNames = new ArrayList<>();
        featureFileNames.add("featureFileName");

        FeatureRunner featureRunner = new FeatureRunner(
                "MyClass.java", "RunnerClass", featureFileNames
        );

        String renderedRunnerFileContent = runnerFileContentRenderer.getRenderedRunnerFileContent(featureRunner);

        assertThat(renderedRunnerFileContent, is(expectedOutput));
    }

    @Test
    public void multipleFeatureRunnerTest() throws Exception {

        String template = "package parallel;\n" +
                "\n" +
                "package some.package;\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/[CUCABLE:FEATURE].feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/[CUCABLE:RUNNER].json\"}\n" +
                ")\n" +
                "public class MyClass {\n" +
                "}\n";

        when(fileIO.readContentFromFile(anyString())).thenReturn(template);

        String expectedOutput = "\n" +
                "\n" +
                "\n" +
                "import cucumber.api.CucumberOptions;\n" +
                "\n" +
                "@CucumberOptions(\n" +
                "    monochrome = false,\n" +
                "    features = {\"classpath:parallel/features/featureFileName.feature\",\n" +
                "\"classpath:parallel/features/featureFileName2.feature\"},\n" +
                "    plugin = {\"json:target/cucumber-report/RunnerClass.json\"}\n" +
                ")\n" +
                "public class RunnerClass {\n" +
                "}\n" +
                "\n" +
                "\n" +
                "// Generated by Cucable from MyClass.java\n";


        ArrayList<String> featureFileNames = new ArrayList<>();
        featureFileNames.add("featureFileName");
        featureFileNames.add("featureFileName2");

        FeatureRunner featureRunner = new FeatureRunner(
                "MyClass.java", "RunnerClass", featureFileNames
        );

        String renderedRunnerFileContent = runnerFileContentRenderer.getRenderedRunnerFileContent(featureRunner);

        assertThat(renderedRunnerFileContent, is(expectedOutput));
    }
}
