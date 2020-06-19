package com.springboot.app.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter {

  private static Logger logger = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true; //Si retorna true ejecuta el metodo Run
  }


  @Override //Aquí se resuelve la lógica de nuestro filtro
  public Object run() throws ZuulException {

    RequestContext context = RequestContext.getCurrentContext();
    HttpServletRequest request = context.getRequest();
    logger.info(
        String.format("%s request enrutado a %s", request.getMethod(), request.getRequestURI()));
    Long timepoInicio = System.currentTimeMillis();
    request.setAttribute("tiempoInicio", timepoInicio);

    return null;
  }
}
