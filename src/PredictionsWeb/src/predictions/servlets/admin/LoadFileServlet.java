package predictions.servlets.admin;

import com.google.gson.Gson;
import dto.FileReaderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "LoadFileServlet", urlPatterns = {"/loadFile"})
public class LoadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("file");
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        resp.getWriter().println(new Gson().toJson(predictionsService.readFileAndLoad(fileName)));
    }
}
