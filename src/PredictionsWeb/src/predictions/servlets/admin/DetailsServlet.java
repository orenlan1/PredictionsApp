package predictions.servlets.admin;

import com.google.gson.Gson;
import dto.SimulationInfoDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import predictions.api.PredictionsService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "DetailsServlet", urlPatterns = {"/details"})
public class DetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String simulationName = req.getParameter("name");
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        String dtoAsJson = new Gson().toJson(predictionsService.getSimulationInformation(simulationName));
        writer.println(dtoAsJson);
    }
}
