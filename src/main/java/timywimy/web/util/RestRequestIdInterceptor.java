package timywimy.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import timywimy.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RestRequestIdInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(RestRequestIdInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        //add request id and start time
        int randomRequestId = RequestUtil.getRandomRequestId();
        request.setAttribute("timywimy.request.id", randomRequestId);
        request.setAttribute("timywimy.request.startTs", System.currentTimeMillis());
        log.info("[{}] {} request to \"{}\"", randomRequestId, request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        Long startTs = (Long) request.getAttribute("timywimy.request.startTs");
        Long finTs = System.currentTimeMillis();
        log.info("[{}] request processed ({} ms)", request.getAttribute("timywimy.request.id"), finTs - startTs);

    }
}
