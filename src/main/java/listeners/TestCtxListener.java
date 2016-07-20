package listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestCtxListener implements ServletContextListener {

    public static final Log log = LogFactory.getLog(CustomStartupCtxListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String baseMsg = " выполнения кастомных действий по иниту контекста из TestCtxListener";
        log.info("Начало" + baseMsg);
        //TODO Запилить катомные действия инита
        log.info("Конец" + baseMsg);    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        String baseMsg = " выполнения кастомных действий по дестрою контекста из TestCtxListener";
        log.info("Начало" + baseMsg);
        //TODO Запилить катомные действия инита
        log.info("Конец" + baseMsg);
    }
}
