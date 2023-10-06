package predictions.servlets.admin;


import com.google.gson.Gson;
import dto.AllocationDTO;
import dto.TerminationDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.allocations.Allocation;
import predictions.allocations.AllocationsManager;
import predictions.utils.ServletUtils;
import world.factory.DTOFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet ( name = "adminAllocationServlet", urlPatterns = "/admin/allocation")
public class AdminAllocationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        PrintWriter writer = resp.getWriter();
        DTOFactory dtoFactory = new DTOFactory();
        resp.setContentType("application/json;charset=UTF-8");
        AllocationsManager allocationsManager = ServletUtils.getAllocationsManager(getServletContext());
        List<AllocationDTO> allocationDTOList = new ArrayList<>();
        List<Allocation> allocationList = allocationsManager.getAllAllocations();
        for ( Allocation allocation : allocationList) {
            if (allocation.isUpdated() || allocation.getStatus().equals("unhandled")) {
                TerminationDTO terminationDTO = dtoFactory.createTerminationDTO(allocation.getTermination());
                AllocationDTO allocationDTO = new AllocationDTO(allocation.getSimulationName(), allocation.getExecutionsFinishedAmount()
                        , allocation.getExecutionsRunningCount(), allocation.getId(), allocation.getStatus(), allocation.getUsername()
                        , terminationDTO, allocation.getExecutionsRequestedCount());
                allocationDTOList.add(allocationDTO);
            }
        }
        if ( allocationDTOList.isEmpty())
            resp.setStatus(HttpServletResponse.SC_CONTINUE);
        else {
            writer.println(new Gson().toJson(allocationDTOList));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
