package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import util.RedisTool;


@WebFilter(filterName = "filter1FilterText", servletNames = {"Text"})
public class FilterText implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        String room = servletRequest.getParameter("room");
        if (room != null && !room.equals("") && room.matches("[0-9a-zA-Z]+")) {
            if (room.equals("clear")) {
                RedisTool.emptyRedis();
            }

            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    public void destroy() {
    }
}
