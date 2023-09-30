package predictions.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import predictions.impl.PredictionsServiceImpl;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("engine", new PredictionsServiceImpl());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down server...");
        ServletContextListener.super.contextDestroyed(sce);
    }
}
