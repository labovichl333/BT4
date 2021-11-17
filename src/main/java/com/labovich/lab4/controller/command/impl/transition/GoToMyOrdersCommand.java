package com.labovich.lab4.controller.command.impl.transition;

import com.labovich.lab4.controller.command.Command;
import com.labovich.lab4.controller.command.CommandResult;
import com.labovich.lab4.controller.command.CommandResultType;
import com.labovich.lab4.controller.context.RequestContext;
import com.labovich.lab4.controller.context.RequestContextHelper;
import com.labovich.lab4.entity.Apartment;
import com.labovich.lab4.entity.User;
import com.labovich.lab4.entity.UserOrder;
import com.labovich.lab4.exeptions.ServiceException;
import com.labovich.lab4.service.ServiceFactory;
import com.labovich.lab4.service.description.ApartmentService;
import com.labovich.lab4.service.description.UserOrderService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GoToMyOrdersCommand implements Command {
    private static final String PAGE = "WEB-INF/view/myOrders.jsp";
    private static final String ERROR_PAGE = "WEB-INF/view/error.jsp";
    private static final String USER_ORDERS = "userOrders";
    private static final String APARTMENTS = "apartments";
    private static final String USER = "user";

    @Override
    public CommandResult execute(RequestContextHelper helper, HttpServletResponse response) {
        RequestContext requestContext = helper.createContext();

        User user = (User) requestContext.getSessionAttribute(USER);
        if (user == null) {
            helper.updateRequest(requestContext);
            return new CommandResult(PAGE, CommandResultType.FORWARD);
        }
        try {
            UserOrderService userOrderService=ServiceFactory.getInstance().getUserOrderService();
            List<UserOrder> userOrders=userOrderService.retrieveUserOrderByUserId(user.getId());
            requestContext.addRequestAttribute(USER_ORDERS, userOrders);
            ApartmentService apartmentService=ServiceFactory.getInstance().getApartmentService();
            List<Apartment> apartments=apartmentService.retrieveApartamentsByUserId(user.getId());
            requestContext.addRequestAttribute(APARTMENTS, apartments);

        } catch (ServiceException e) {
            return new CommandResult(ERROR_PAGE, CommandResultType.FORWARD);
        }

        helper.updateRequest(requestContext);
        return new CommandResult(PAGE, CommandResultType.FORWARD);
    }
}
