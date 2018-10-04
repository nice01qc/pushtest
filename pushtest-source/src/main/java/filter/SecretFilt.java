package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "filter0SecretFilt", urlPatterns = {"/*"})
public class SecretFilt implements Filter {
    private ServletContext servletContext = null;

    public SecretFilt() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.servletContext.setAttribute("secret", "open");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        String secret = (String)this.servletContext.getAttribute("secret");
        String room = servletRequest.getParameter("room");
        if (room != null && !room.equals("")) {
            if (room.equals("open")) {
                this.servletContext.setAttribute("secret", "open");
            } else if (room.equals("close")) {
                this.servletContext.setAttribute("secret", "close");
            }

            if (secret.equals("close")) {
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
