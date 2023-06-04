package pageObjects;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementing allure log output attachment
 */
public class LogAttachmentExtension implements BeforeEachCallback, AfterEachCallback {
        private static final Logger logger = LoggerFactory.getLogger(LogAttachmentExtension.class);

        private StringBuilder logOutput = new StringBuilder();

        @Override
        public void beforeEach(ExtensionContext context) {
            logOutput.setLength(0);
        }

        @Override
        public void afterEach(ExtensionContext context) {
            String logContent = logOutput.toString();
            logger.info("Captured Log Output:\n{}", logContent);
            Allure.addAttachment("Log Output", logContent);
        }

    /**
     * Appends log message to log file
     * @param log String you want to append.
     */

    public void appendLog(String log) {
            logOutput.append(log);
        }

}


