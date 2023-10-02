package predictions.servlets.admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet (name = "ThreadCountServlet", urlPatterns = {"/threadCount"})
public class ThreadCountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        Integer threadCount;
        try {
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            Gson gson = new Gson();
            threadCount = gson.fromJson(jsonBody.toString(), JsonObject.class).get("threadCount").getAsInt();

            predictionsService.setThreadCount(threadCount);
        } catch (NumberFormatException e) {
            //TODO
            //add exception handler
        }
    }
}
