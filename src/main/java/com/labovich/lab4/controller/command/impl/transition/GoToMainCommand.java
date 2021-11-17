package com.labovich.lab4.controller.command.impl.transition;

import com.labovich.lab4.controller.command.Command;
import com.labovich.lab4.controller.command.CommandResult;
import com.labovich.lab4.controller.command.CommandResultType;
import com.labovich.lab4.controller.context.RequestContext;
import com.labovich.lab4.controller.context.RequestContextHelper;
import com.labovich.lab4.service.ServiceFactory;
import com.labovich.lab4.service.description.ApartmentService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GoToMainCommand implements Command {
    private static final String PAGE = "WEB-INF/view/main.jsp";



    @Override
    public CommandResult execute(RequestContextHelper helper, HttpServletResponse response) {
        RequestContext requestContext = helper.createContext();


        helper.updateRequest(requestContext);
        return new CommandResult(PAGE, CommandResultType.FORWARD);
    }
}