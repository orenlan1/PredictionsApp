package predictions.servlets.admin;

import com.google.gson.Gson;
import dto.FileReaderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import predictions.api.PredictionsService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet (name = "LoadFileServlet", urlPatterns = {"/loadFile"})
@MultipartConfig
public class LoadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        resp.getWriter().println(new Gson().toJson(predictionsService.readFileAndLoad(fileContent)));
    }
}
