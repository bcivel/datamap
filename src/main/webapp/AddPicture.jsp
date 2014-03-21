<%-- 
    Document   : AddPicture
    Created on : 1 mars 2014, 22:28:53
    Author     : bcivel
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AddPicture</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style media="screen" type="text/css">
            @import "css/demo_page.css";
            @import "css/demo_table.css";
            @import "css/demo_table_jui.css";
            @import "css/themes/base/jquery-ui.css";
            @import "css/themes/smoothness/jquery-ui-1.7.2.custom.css";
        </style>
        <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
        <link rel="stylesheet"  type="text/css" href="css/crb_style.css">
        <link rel="stylesheet" type="text/css" href="css/elrte.min.css">

        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="js/elrte.min.js"></script>
        <script type="text/javascript" src="js/i18n/elrte.en.js"></script>
    </head>
    <body>
        <h1>Add Picture</h1>
        <div id="dialog">
        <form id="formAddNewRow" action="AddPicture" title="Add Picture" method="post" enctype="multipart/form-data" acceptcharset="UTF-8">
            <div  style="width: 200px; clear:both">
                <label for="Page" class="ncheader" style="width: 200px;height:20px">Page</label>
                <input id="Page" name="Page" placeholder="Put the Page Name" style="width:200px; font-size: medium" 
                       class="ncdetailstext">
            </div>
            <div  style="width: 200px; clear:both">
                <label for="PictureName" class="ncheader" style="width: 200px;height:20px">PictureName</label>
                <input id="PictureName" name="PictureName" placeholder="Choose a nam for the picture" style="width:200px; font-size: medium" 
                       class="ncdetailstext">
            </div>
            <div style="width: 400px; clear:both">
                <label for="Screenshot" style="font-weight:bold">Screenshot</label><br>
                <textarea name="Screenshot" id="Screenshot" class="ncdetailstext" placeholder="toot" style="width:500px; height:85px"></textarea>
                <input id="ScreenshotDetail" class="ncdetailstext" name="ScreenshotDetail" type="hidden" value="" />
            </div>
            <div>
             <br /><br />
            <button id="btnAddNewRowOk" submit="submit">Add</button>
            <button id="btnAddNewRowCancel">Cancel</button>
            </div>
        </form>
        </div>
        <script>
            $().ready(function() {
                elRTE.prototype.options.toolbars.redip = ['style', 'alignment', 'colors', 'format', 'indent', 'lists', 'links'];
                var opts = {
                    lang: 'en',
                    styleWithCSS: false,
                    width: 615,
                    height: 150,
                    toolbar: 'complete',
                    allowSource: false
                };
                $('#Screenshot').elrte(opts);

            });
        </script>
    </body>
</html>
