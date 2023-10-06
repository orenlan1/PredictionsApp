package predictions.servlets.client;


import com.google.gson.Gson;
import dto.AllocationDTO;
import dto.RequestDTO;
import dto.TerminationDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.allocations.Allocation;
import predictions.allocations.AllocationsManager;
import predictions.utils.ServletUtils;
import predictions.utils.SessionUtils;
import world.termination.Termination;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet (name = "clientRequestServlet" , urlPatterns = "/client/request")
public class ClientRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json;charset=UTF-8");
        AllocationsManager allocationsManager = ServletUtils.getAllocationsManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(req);
        RequestDTO request = gson.fromJson(req.getReader(),RequestDTO.class);
        TerminationDTO terminationDTO = request.getTerminationDTO();
        Termination termination = new Termination(terminationDTO.getTicksCount(), terminationDTO.getSecondCount(),
                terminationDTO.getByUser());
        Integer allocationId = allocationsManager.getAllocationID();
        Allocation newAllocation = new Allocation(request.getSimulationName(), usernameFromSession,
                request.getExecutionsRequestAmount(), termination,allocationsManager.getAllocationID());
        allocationsManager.addAllocation(usernameFromSession, newAllocation);
        resp.getWriter().println(new Gson().toJson(allocationId));
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<AllocationDTO> allocationDTOList = new ArrayList<>();
        AllocationsManager allocationsManager = ServletUtils.getAllocationsManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(req);
        for ( Allocation allocation : allocationsManager.getUserAllocations(usernameFromSession).values()) {
            if (allocation.isUpdated()) {
                AllocationDTO allocationDTO = new AllocationDTO(allocation.getSimulationName(),allocation.getExecutionsFinishedAmount()
                ,allocation.getExecutionsRunningCount(),allocation.getId(),allocation.getStatus(),null,null,allocation.getExecutionsRequestedCount());
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
