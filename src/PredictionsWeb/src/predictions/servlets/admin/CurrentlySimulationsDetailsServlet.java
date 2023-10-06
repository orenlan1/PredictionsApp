package predictions.servlets.admin;

import com.google.gson.Gson;
import dto.AllSimulationsDTO;
import dto.SimulationInfoDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;


@WebServlet ( name = "CurrentlySimulationsDetailsServlet" , urlPatterns = "/allDetails")
public class CurrentlySimulationsDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
         Collection<String> allNames = predictionsService.getAllSimulationsNames();
        if  ( allNames.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_CONTINUE);
        }
        else {
            String dtoAsJson = new Gson().toJson(allNames);
            writer.println(dtoAsJson);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
