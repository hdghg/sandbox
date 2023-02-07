package com.mycompany.gb;

import com.mycompany.gb.config.Constants;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

public class App {
    public static void main(String[] args) throws LifecycleException {
        Tomcat server = new Tomcat();
        server.setBaseDir(System.getProperty("java.io.tmpdir"));
        String port = System.getenv(Constants.PORT);
        server.setPort(null == port ? 8080 : Integer.parseInt(port));

        Context rootContext = server.addContext("", System.getProperty("java.io.tmpdir"));
        String filterName = "encodingFilter";
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(filterName);
        SetCharacterEncodingFilter filter = new SetCharacterEncodingFilter();
        filter.setIgnore(false);
        filter.setEncoding("UTF-8");
        filterDef.setFilter(filter);
        rootContext.addFilterDef(filterDef);
        FilterMap mapping = new FilterMap();
        mapping.setFilterName(filterName);
        mapping.addURLPattern("/*");
        rootContext.addFilterMap(mapping);

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.scan("com.mycompany");
        ctx.setServletContext(rootContext.getServletContext());
        ctx.refresh();
        DispatcherServlet dispatcher = new DispatcherServlet(ctx);
        String dispatcherServletName = "servlet";
        Tomcat.addServlet(rootContext, dispatcherServletName, dispatcher);
        rootContext.addServletMapping("/", dispatcherServletName);
        server.start();
        server.getServer().await();
    }
}
