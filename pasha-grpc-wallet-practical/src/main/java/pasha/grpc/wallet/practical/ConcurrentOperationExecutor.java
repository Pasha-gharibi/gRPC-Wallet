package pasha.grpc.wallet.practical;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.ConcurrencyFailureException;

/**
 * I also added an aspect to retry failed deposit/withdraw transactions
 * for any transaction that failed because of ConcurrencyFailureException exceptions including deadlock and ObjectOptimisticLockingFailureException.
 */
@Aspect
public class ConcurrentOperationExecutor implements Ordered {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentOperationExecutor.class);
  private static final int DEFAULT_MAX_RETRIES = 10;

  private int maxRetries = DEFAULT_MAX_RETRIES;
  private int order = 1;

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }

  public int getOrder() {
    return this.order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  @Around("execution(* pasha.grpc.wallet.practical.server.service.TransactionService.*(..))")
  public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
    int numAttempts = 0;
    ConcurrencyFailureException lockFailureException;
    do {
      numAttempts++;
      try {
        return pjp.proceed();
      } catch (ConcurrencyFailureException ex) {
        LOGGER.warn("!!!!!ConcurrencyFailureException!!!!! " + ex.getMessage() + ", " + numAttempts);
        lockFailureException = ex;
      }
    } while (numAttempts <= this.maxRetries);
    throw lockFailureException;
  }
}

