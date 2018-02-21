package timywimy.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import timywimy.util.StringUtil;
import timywimy.web.controllers.APIController;
import timywimy.web.dto.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class APIServlet extends HttpServlet {
    private static final Logger log = getLogger(APIServlet.class);
    private ConfigurableApplicationContext springContext;
    private APIController api;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        api = springContext.getBean(APIController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(200);
        String method = request.getParameter("method");
        String userName = request.getParameter("user_name");
        String userEmail = request.getParameter("user_email");
        String userPass = request.getParameter("user_pass");
        String session1 = request.getParameter("session");
        User user = new User();
        user.setEmail(userEmail);
        user.setName(userName);
        user.setPassword(userPass);

        if (StringUtil.isEmpty(method)) {
            response.getWriter().append("NO METHOD,LOL");
        } else {
            switch (method) {
                case "register":
                    response.getWriter().append("REGISTER,LOL\n");
                    UUID register = api.register(user);
                    response.getWriter().append(register.toString());
                    break;
                case "login":
                    response.getWriter().append("LOGIN,LOL\n");
                    UUID session = api.openSession(user);
                    response.getWriter().append(session.toString());
                    break;
                case "logout":
                    response.getWriter().append("LOGOUT,LOL\n");
                    Boolean coole = api.closeSession(UUID.fromString(session1));
                    response.getWriter().append(coole.toString());
                    break;
                default:
                    response.getWriter().append("UNKNOWN METHOD,LOL");
                    break;
            }
            int stop = 0;
        }

    }
}
