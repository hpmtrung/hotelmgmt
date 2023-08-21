package vn.lotusviet.hotelmgmt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Configuration(proxyBeanMethods = false)
@EnableAsync
@EnableScheduling
public class AsyncConfig implements AsyncConfigurer {

  private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

  private final TaskExecutionProperties taskExecutionProperties;

  public AsyncConfig(TaskExecutionProperties taskExecutionProperties) {
    this.taskExecutionProperties = taskExecutionProperties;
  }

  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
    executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
    executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
    executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
    executor.initialize();
    return new ExceptionHandlingAsyncTaskExecutor(executor);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }

  public static class ExceptionHandlingAsyncTaskExecutor
      implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    private static final String EXCEPTION_MESSAGE = "Caught async exception";

    private static final Logger log =
        LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

    private final AsyncTaskExecutor executor;

    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
      this.executor = executor;
    }

    public void execute(Runnable task) {
      this.executor.execute(this.createWrappedRunnable(task));
    }

    public void execute(Runnable task, long startTimeout) {
      this.executor.execute(this.createWrappedRunnable(task), startTimeout);
    }

    public Future<?> submit(Runnable task) {
      return this.executor.submit(this.createWrappedRunnable(task));
    }

    public <T> Future<T> submit(Callable<T> task) {
      return this.executor.submit(this.createCallable(task));
    }

    public void destroy() throws Exception {
      if (this.executor instanceof DisposableBean) {
        DisposableBean bean = (DisposableBean) this.executor;
        bean.destroy();
      }
    }

    public void afterPropertiesSet() throws Exception {
      if (this.executor instanceof InitializingBean) {
        InitializingBean bean = (InitializingBean) this.executor;
        bean.afterPropertiesSet();
      }
    }

    protected void handle(Exception e) {
      log.error(EXCEPTION_MESSAGE, e);
    }

    private <T> Callable<T> createCallable(Callable<T> task) {
      return () -> {
        try {
          return task.call();
        } catch (Exception var3) {
          this.handle(var3);
          throw var3;
        }
      };
    }

    private Runnable createWrappedRunnable(Runnable task) {
      return () -> {
        try {
          task.run();
        } catch (Exception var3) {
          this.handle(var3);
        }
      };
    }
  }
}