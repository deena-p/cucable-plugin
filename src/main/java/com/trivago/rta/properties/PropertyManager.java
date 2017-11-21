/*
 * Copyright 2017 trivago N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trivago.rta.properties;

import com.trivago.rta.exceptions.CucablePluginException;
import com.trivago.rta.exceptions.properties.WrongOrMissingPropertyException;
import com.trivago.rta.logging.CucableLogger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class PropertyManager {
    // Generated source runner template file placeholder for logging.
    private static final String SOURCE_RUNNER_TEMPLATE_FILE = "<sourceRunnerTemplateFile>";

    // Generated runner directory placeholder for logging.
    private static final String GENERATED_RUNNER_DIRECTORY = "<generatedRunnerDirectory>";

    // Source features placeholder for logging.
    private static final String SOURCE_FEATURES = "<sourceFeatures>";

    // Generated feature directory placeholder for logging.
    private static final String GENERATED_FEATURE_DIRECTORY = "<generatedFeatureDirectory>";

    private final CucableLogger logger;

    private String sourceRunnerTemplateFile;
    private String generatedRunnerDirectory;
    private String sourceFeatures;
    private String generatedFeatureDirectory;
    private Integer scenarioLineNumber;
    private int numberOfTestRuns;
    private List<String> excludeTags;

    @Inject
    public PropertyManager(CucableLogger logger) {
        this.logger = logger;
    }

    public String getSourceRunnerTemplateFile() {
        return sourceRunnerTemplateFile;
    }

    public void setSourceRunnerTemplateFile(final String sourceRunnerTemplateFile) {
        this.sourceRunnerTemplateFile = sourceRunnerTemplateFile;
    }

    public String getGeneratedRunnerDirectory() {
        return generatedRunnerDirectory;
    }

    public void setGeneratedRunnerDirectory(final String generatedRunnerDirectory) {
        this.generatedRunnerDirectory = generatedRunnerDirectory;
    }

    public String getSourceFeatures() {
        return sourceFeatures;
    }

    public Integer getScenarioLineNumber() {
        return scenarioLineNumber;
    }

    public boolean hasValidScenarioLineNumber() {
        return scenarioLineNumber != null;
    }

    public void setSourceFeatures(final String sourceFeatures) {
        String sourceFeaturesWithoutLineNumber = sourceFeatures;
        final int lastColonPosition = sourceFeatures.lastIndexOf(':');
        if (lastColonPosition > -1) {
            String scenarioLineNumber = sourceFeatures.substring(lastColonPosition + 1).trim();
            try {
                this.scenarioLineNumber = Integer.parseInt(scenarioLineNumber);
                sourceFeaturesWithoutLineNumber = sourceFeatures.substring(0, lastColonPosition).trim();
            } catch (NumberFormatException e) {
                // Line number could not be parsed so keeping original sourceFeatures
            }
        }
        this.sourceFeatures = sourceFeaturesWithoutLineNumber;
    }

    public String getGeneratedFeatureDirectory() {
        return generatedFeatureDirectory;
    }

    public void setGeneratedFeatureDirectory(final String generatedFeatureDirectory) {
        this.generatedFeatureDirectory = generatedFeatureDirectory;
    }

    public int getNumberOfTestRuns() {
        return numberOfTestRuns;
    }

    public void setNumberOfTestRuns(final int numberOfTestRuns) {
        this.numberOfTestRuns = numberOfTestRuns;
    }

    public List<String> getExcludeTags() {
        return excludeTags;
    }

    public void setExcludeScenarioTags(final List<String> excludeTags) {
        this.excludeTags = excludeTags;
    }

    /**
     * Checks the pom settings for the plugin.
     *
     * @throws CucablePluginException Thrown when a required setting
     *                                is not specified in the pom.
     */
    public void validateSettings() throws CucablePluginException {
        String missingProperty = null;
        if (sourceRunnerTemplateFile.equals("")) {
            missingProperty = SOURCE_RUNNER_TEMPLATE_FILE;
        } else if (generatedRunnerDirectory.equals("")) {
            missingProperty = GENERATED_RUNNER_DIRECTORY;
        } else if (sourceFeatures.equals("")) {
            missingProperty = SOURCE_FEATURES;
        } else if (generatedFeatureDirectory.equals("")) {
            missingProperty = GENERATED_FEATURE_DIRECTORY;
        } else if (excludeTags != null) {
            for (String excludeTag : excludeTags) {
                if (!excludeTag.startsWith("@")) {
                    throw new CucablePluginException("Exlude tag '" + excludeTag + "' does not start with an '@'.");
                }
            }
        }

        if (missingProperty != null) {
            throw new WrongOrMissingPropertyException(missingProperty);
        }
    }

    public void logProperties() {
        logger.info(String.format("- sourceRunnerTemplateFile  : %s", sourceRunnerTemplateFile));
        logger.info(String.format("- generatedRunnerDirectory  : %s", generatedRunnerDirectory));
        logger.info(String.format("- sourceFeatures            : %s", sourceFeatures));
        if (hasValidScenarioLineNumber()) {
            logger.info(String.format("                              with line number %d", scenarioLineNumber));
        }
        if (excludeTags != null) {
            logger.info("                              exclude tags:");
            for (String excludeTag : excludeTags) {
                logger.info(String.format("                              - %s", excludeTag));
            }
        }
        logger.info(String.format("- generatedFeatureDirectory : %s", generatedFeatureDirectory));
        logger.info(String.format("- numberOfTestRuns          : %d", numberOfTestRuns));
    }
}
