package predictions.servlets.client;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;

@WebServlet (name = "SimulationListServlet", urlPatterns = {"/simulations/names"})
public class SimulationListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            PredictionsService predictionsService = (PredictionsService) request.getServletContext().getAttribute("engine");
            Collection<String> allSimulationsNames = predictionsService.getAllSimulationsNames();
            String json = gson.toJson(allSimulationsNames);
            out.println(json);
            out.flush();
        }
    }
}
