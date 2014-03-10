<%-- 
    Document   : Reporting
    Created on : 10 mars 2014, 13:19:50
    Author     : bcivel
--%>

<%@page import="java.util.List"%>
<%@page import="com.redoute.datamap.service.IDatamapService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style media="screen" type="text/css">
            @import "css/demo_page.css";
            @import "css/demo_table.css";
            @import "css/demo_table_jui.css";
            @import "css/themes/base/jquery-ui.css";
            @import "css/themes/smoothness/jquery-ui-1.7.2.custom.css";
        </style>
        <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
        <link rel="stylesheet"  type="text/css" href="css/crb_style.css">
        <link type="text/css" rel="stylesheet" href="css/jquery.multiselect.css">
    </head>
    <body>
        <div class="ncdescriptionfirstpart" style="vertical-align: central; clear:both">
            <p style="text-align:left">Data-Cerberus Implementation</p>
            <form action="Datamap.jsp" method="get" name="ExecFilters" id="ExecFilters">
               <div><input style="float:right;width:40px; height:40px; border-width: 1px; border-radius: 20px;" type="button" value="M" title="Main" onclick="location.href = 'Datamap.jsp'"></div>
            </form>
        </div>
        <div style="clear:both; height:10px"><p></p></div>
        <%  
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IDatamapService datamapService = appContext.getBean(IDatamapService.class);
            
            List<String> streams = datamapService.findDistinctValuesfromColumn("stream");
            for (String stream : streams){
            %>
        <div style="float:left; width:400px; height:250px"><img style="width:400px; height:250px" src="GenerateGraph?stream=<%=stream%>">
        </div>
        <%
            }
    %>
    
    </body>
</html>
