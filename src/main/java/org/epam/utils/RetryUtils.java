package org.epam.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.epam.exceptions.FrameworkException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Retry Utility - Implements retry mechanism with exponential backoff
 *
 * Features:
 * - Automatic retry with exponential backoff (1s → 2s → 4s)
 * - Detailed logging of each retry attempt
 * - Logs to both file and console
 * - Tracks retry timing and performance
 * - Throws informative exceptions on final failure
 *
 * Configuration:
 * - MAX_RETRIES: 3 attempts
 * - BACKOFF_MULTIPLIER: 2x (exponential)
 * - Initial wait: 1 second
 * - Total max wait: ~7 seconds per operation
 *
 * Usage Example:
 * WebElement button = RetryUtils.retryOperation(
 *     () -> driver.findElement(By.id("login-button")),
 *     "Finding login button",
 *     3  // max retries
 * );
 *
 * Logging Output:
 * [2025-12-24 14:30:15] [Thread-1] [DEBUG] Starting retry operation: Finding element (max retries: 3)
 * [2025-12-24 14:30:15] [Thread-1] [DEBUG] Attempt 1/3: Finding element
 * [2025-12-24 14:30:15] [Thread-1] [WARN] Attempt 1/3 failed: Element not visible
 * [2025-12-24 14:30:15] [Thread-1] [DEBUG] Waiting 1 second before retry...
 * [2025-12-24 14:30:16] [Thread-1] [DEBUG] Attempt 2/3: Finding element
 * [2025-12-24 14:30:17] [Thread-1] [INFO] Operation succeeded on attempt 2/3: Finding element
 */
public class RetryUtils {
    private static final Logger logger = LoggerFactory.getLogger(RetryUtils.class);

    // Configuration Constants
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int INITIAL_WAIT_SECONDS = 1;
    private static final int BACKOFF_MULTIPLIER = 2;  // Exponential: 1s → 2s → 4s

    // Date format for detailed logging
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Retry an operation with default settings (3 retries, exponential backoff)
     *
     * @param <T> return type of the operation
     * @param operation the lambda/function to execute and retry
     * @param operationName description of what we're trying (for logging)
     * @return result of the operation
     * @throws FrameworkException if all retries fail
     *
     * Example:
     * WebElement button = RetryUtils.retryOperation(
     *     () -> driver.findElement(By.id("login")),
     *     "Finding login button"
     * );
     */
    public static <T> T retryOperation(RetryableOperation<T> operation, String operationName) {
        return retryOperation(operation, operationName, DEFAULT_MAX_RETRIES);
    }

    /**
     * Retry an operation with custom max retries and exponential backoff
     *
     * Backoff Strategy:
     * - Attempt 1: Immediate (no wait)
     * - Attempt 2: Wait 1 second before retry
     * - Attempt 3: Wait 2 seconds before retry
     * - Attempt 4+: Wait 4 seconds before retry
     *
     * @param <T> return type of the operation
     * @param operation the lambda/function to execute and retry
     * @param operationName description of what we're trying
     * @param maxRetries maximum number of attempts
     * @return result of the operation
     * @throws FrameworkException if all retries fail
     */
    public static <T> T retryOperation(RetryableOperation<T> operation, String operationName, int maxRetries) {
        long operationStartTime = System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(dateFormatter);

        logger.info("═══════════════════════════════════════════════════════════════");
        logger.info("RETRY OPERATION START [{}]", timestamp);
        logger.info("Operation: {}", operationName);
        logger.info("Max Retries: {}", maxRetries);
        logger.info("Thread: {}", Thread.currentThread().getName());
        logger.info("───────────────────────────────────────────────────────────────");

        Exception lastException = null;

        // Try up to maxRetries times
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            long attemptStartTime = System.currentTimeMillis();
            String attemptTimestamp = LocalDateTime.now().format(dateFormatter);

            try {
                // Log attempt start
                logger.info("┌─ ATTEMPT {}/{} [{}]", attempt, maxRetries, attemptTimestamp);
                logger.debug("  │ Operation: {}", operationName);

                // Execute the operation
                T result = operation.execute();

                // Success!
                long attemptDuration = System.currentTimeMillis() - attemptStartTime;

                logger.info("├─ ✓ SUCCESS on attempt {}/{}", attempt, maxRetries);
                logger.info("├─ Duration: {} ms", attemptDuration);
                logger.info("└─ Result: Operation succeeded");

                long totalDuration = System.currentTimeMillis() - operationStartTime;
                logger.info("═══════════════════════════════════════════════════════════════");
                logger.info("✓ OPERATION SUCCESSFUL");
                logger.info("├─ Operation: {}", operationName);
                logger.info("├─ Succeeded on attempt: {}/{}", attempt, maxRetries);
                logger.info("├─ Total duration: {} ms", totalDuration);
                logger.info("└─ Timestamp: {}", LocalDateTime.now().format(dateFormatter));
                logger.info("═══════════════════════════════════════════════════════════════");

                return result;

            } catch (Exception e) {
                // Capture the exception
                lastException = e;
                long attemptDuration = System.currentTimeMillis() - attemptStartTime;
                String exceptionType = e.getClass().getSimpleName();

                // Log failure details
                logger.warn("├─ ✗ FAILED on attempt {}/{}", attempt, maxRetries);
                logger.warn("├─ Exception Type: {}", exceptionType);
                logger.warn("├─ Exception Message: {}", e.getMessage());
                logger.warn("├─ Duration: {} ms", attemptDuration);
                logger.debug("├─ Stack Trace: ", e);

                // If not the last attempt, calculate wait time and log
                if (attempt < maxRetries) {
                    int waitSeconds = calculateWaitTime(attempt);
                    logger.info("├─ Waiting {} second(s) before retry...", waitSeconds);
                    logger.info("└─ Retry scheduled at: {}",
                            LocalDateTime.now().plusSeconds(waitSeconds).format(dateFormatter));

                    // Wait before retrying
                    waitBeforeRetry(waitSeconds);

                    logger.info("├─ Retry resuming after {} second(s) wait", waitSeconds);

                } else {
                    // Last attempt failed
                    logger.error("└─ ✗ FINAL ATTEMPT FAILED - No more retries");
                }
            }
        }

        // All attempts failed - throw exception with comprehensive details
        long totalDuration = System.currentTimeMillis() - operationStartTime;
        String errorMessage = String.format(
            "Operation failed after %d attempts (total duration: %d ms): %s. Last error: %s",
            maxRetries,
            totalDuration,
            operationName,
            lastException != null ? lastException.getMessage() : "Unknown error"
        );

        logger.error("═══════════════════════════════════════════════════════════════");
        logger.error("✗ OPERATION FAILED");
        logger.error("├─ Operation: {}", operationName);
        logger.error("├─ All {} attempts failed", maxRetries);
        logger.error("├─ Last Exception Type: {}", lastException != null ? lastException.getClass().getSimpleName() : "N/A");
        logger.error("├─ Last Exception Message: {}", lastException != null ? lastException.getMessage() : "N/A");
        logger.error("├─ Total duration: {} ms", totalDuration);
        logger.error("├─ Timestamp: {}", LocalDateTime.now().format(dateFormatter));
        logger.error("└─ Throwing FrameworkException with context");
        logger.error("═══════════════════════════════════════════════════════════════");

        throw new FrameworkException(errorMessage, lastException);
    }

    /**
     * Calculate wait time between retries using EXPONENTIAL BACKOFF
     *
     * Backoff Strategy:
     * Attempt 1 fails → Wait 1 second (2^0 = 1)
     * Attempt 2 fails → Wait 2 seconds (2^1 = 2)
     * Attempt 3 fails → Wait 4 seconds (2^2 = 4)
     * Attempt 4+ fails → Wait 4 seconds (capped)
     *
     * @param attemptNumber the current attempt number (1-based)
     * @return seconds to wait before next retry
     */
    private static int calculateWaitTime(int attemptNumber) {
        // Exponential backoff: 1s * (2 ^ attemptNumber-1)
        // Attempt 1 → wait 1s, Attempt 2 → wait 2s, Attempt 3 → wait 4s
        int waitTime = INITIAL_WAIT_SECONDS * (int) Math.pow(BACKOFF_MULTIPLIER, attemptNumber - 1);

        // Cap at maximum reasonable wait (e.g., 8 seconds max)
        int maxWait = 8;
        return Math.min(waitTime, maxWait);
    }

    /**
     * Pause execution for specified seconds
     *
     * @param seconds number of seconds to wait
     */
    private static void waitBeforeRetry(int seconds) {
        try {
            long startTime = System.currentTimeMillis();
            Thread.sleep(seconds * 1000L);
            long actualDuration = System.currentTimeMillis() - startTime;

            logger.debug("Retry wait completed. Expected: {} seconds, Actual: {} ms",
                    seconds, actualDuration);

        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted during retry wait: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Functional interface for retryable operations
     * Any operation that can throw an exception can be wrapped in this
     *
     * @param <T> return type of the operation
     */
    @FunctionalInterface
    public interface RetryableOperation<T> {
        /**
         * Execute the operation
         * @return result
         * @throws Exception if operation fails
         */
        T execute() throws Exception;
    }
}

