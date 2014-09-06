package com.radcortez.wow.auctions.servlet;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
@WebServlet(urlPatterns = {"/BatchExecutionServlet"})
public class BatchExecutionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String batch = req.getParameter("batch");

        JobOperator jobOperator = BatchRuntime.getJobOperator();

        switch (batch) {
            case "prepare":
                jobOperator.start("prepare-job", new Properties());
                break;
            case "files":
                jobOperator.start("files-job", new Properties());
                break;
            default:
                throw new UnsupportedOperationException();
        }

        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
