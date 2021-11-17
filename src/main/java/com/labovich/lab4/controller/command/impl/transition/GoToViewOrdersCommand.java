package com.labovich.lab4.controller.command.impl.transition;

import com.labovich.lab4.controller.command.Command;
import com.labovich.lab4.controller.command.CommandResult;
import com.labovich.lab4.controller.command.CommandResultType;
import com.labovich.lab4.controller.context.RequestContext;
import com.labovich.lab4.controller.context.RequestContextHelper;
import com.labovich.lab4.entity.Apartment;
import com.labovich.lab4.entity.User;
import com.labovich.lab4.entity.UserInformation;
import com.labovich.lab4.entity.UserOrder;
import com.labovich.lab4.exeptions.ServiceException;
import com.labovich.lab4.service.ServiceFactory;
import com.labovich.lab4.service.description.ApartmentService;
import com.labovich.lab4.service.description.UserInformationService;
import com.labovich.lab4.service.description.UserOrderService;
import com.labovich.lab4.service.description.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GoToViewOrdersCommand implements Command {
    private static final String PAGE = "WEB-INF/view/viewOrders.jsp";
    private static final String ERROR_PAGE = "WEB-INF/view/error.jsp";
    private static final String USER_ORDERS = "userOrders";
    private static final String USERS = "users";
    private static final String APARTMENTS = "apartments";
    private static final String USER_INFORMATION = "userInformation";
    private static final String EXPECTED = "booked";

    @Override
    public CommandResult execute(RequestContextHelper helper, HttpServletResponse response) {
        RequestContext requestContext = helper.createContext();

        try {


            UserOrderService userOrderService = ServiceFactory.getInstance().getUserOrderService();
            List<UserOrder> userOrders = userOrderService.retrieveUserOrderByStatus(EXPECTED);
            requestContext.addRequestAttribute(USER_ORDERS, userOrders);



            UserService userService = ServiceFactory.getInstance().getUserService();
            List<User> users = userService.getUsersFromOrders(userOrders);
            requestContext.addRequestAttribute(USERS, users);

            ApartmentService apartmentService=ServiceFactory.getInstance().getApartmentService();
            List<Apartment> apartments=apartmentService.retrieveApartamentsByUserOrders(userOrders);
            requestContext.addRequestAttribute(APARTMENTS, apartments);

            UserInformationService userInformationService = ServiceFactory.getInstance().getUserInformationService();
            List<UserInformation> userInformation = userInformationService.getUserInformationFromUsers(users);
            requestContext.addRequestAttribute(USER_INFORMATION, userInformation);

        } catch (ServiceException e) {
            return new CommandResult(ERROR_PAGE, CommandResultType.FORWARD);
        }

        helper.updateRequest(requestContext);
        return new CommandResult(PAGE, CommandResultType.FORWARD);
    }
}
