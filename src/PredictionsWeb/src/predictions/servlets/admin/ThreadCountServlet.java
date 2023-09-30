package predictions.servlets.admin;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.IOException;

@WebServlet (name = "ThreadCountServlet", urlPatterns = {"/threadCount"})
public class ThreadCountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        try {
            Integer threadCount = Integer.parseInt(req.getParameter("threadCount"));
            predictionsService.setThreadCount(threadCount);
            resp.getWriter().println(new Gson().toJson(threadCount));
        } catch (NumberFormatException e) {
            //TODO
            //add exception handler
        }
    }
}
