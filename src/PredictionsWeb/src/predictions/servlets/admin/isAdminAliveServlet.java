package predictions.servlets.admin;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.api.PredictionsService;

import java.io.IOException;



@WebServlet ( name = "isAdminAliveServlet" , urlPatterns = "/isAlive")
public class isAdminAliveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PredictionsService predictionsService = (PredictionsService) req.getServletContext().getAttribute("engine");
        if (predictionsService.isAdminAlive()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            predictionsService.setAdmin();
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
