package listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.webapp.StartupServletContextListener;

import javax.servlet.ServletContextEvent;


public class CustomStartupCtxListener extends StartupServletContextListener {

    public static final Log log = LogFactory.getLog(CustomStartupCtxListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug("Вызов родительского метода инита контекста из CustomStartupCtxListener");
        super.contextInitialized(servletContextEvent);
        String baseMsg = " выполнения кастомных действий по иниту контекста из CustomStartupCtxListener";
        log.info("Начало" + baseMsg);
        //TODO Запилить катомные действия инита
        log.info("Конец" + baseMsg);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.debug("Вызов родительского метода дестроя контекста из CustomStartupCtxListener");
        super.contextDestroyed(event);
        String baseMsg = " выполнения кастомных действий по дестрою контекста из CustomStartupCtxListener";
        log.info("Начало" + baseMsg);
        //TODO Запилить катомные действия инита
        log.info("Конец" + baseMsg);
    }
}