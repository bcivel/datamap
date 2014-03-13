<%@page import="org.json.JSONArray"%>
<%@page import="java.util.List"%>
<%@page import="com.redoute.datamap.entity.Picture"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.redoute.datamap.service.IPictureService"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Datamap</title>
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
        <link type="text/css" rel="stylesheet" href="css/jquery.multiselect.css">

        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.10.2.custom.min.js"></script>
        <script type="text/javascript" src="js/jquery.jeditable.mini.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.editable.js"></script>
        <script type="text/javascript" src="js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="js/jquery-migrate-1.2.1.min.js"></script>
        <script type="text/javascript" src="js/jquery.multiselect.js" charset="utf-8"></script>
        <style type="text/css">#contentParent{height:400pt;overflow:auto}</style> 
        <style type="text/css">#pictureList{height:400pt;overflow:auto}</style> 


        <script type="text/javascript">

            $(document).ready(function() {
                var test = getValue();
                var oTable = $('#datamapList').dataTable({
                    "aaSorting": [[0, "desc"]],
                    "bServerSide": true,
                    "sAjaxSource": "FindAllDatamap" + test,
                    "bJQueryUI": true,
                    "bProcessing": true,
                    "bPaginate": true,
                    "bAutoWidth": false,
                    "sPaginationType": "full_numbers",
                    "bSearchable": true,
                    "aTargets": [0],
                    "iDisplayLength": 10,
                    "aoColumns": [
                        {"sName": "ID", "sWidth": "10%", "bVisible": false},
                        {"sName": "Stream", "sWidth": "5%"},
                        {"sName": "Page", "sWidth": "20%"},
                        {"sName": "DataCerberus", "sWidth": "40%"},
                        {"sName": "Picture", "sWidth": "30%"},
                        {"sName": "Xpath", "sWidth": "5%", "sType": "int"},
                        {"sName": "Implemented", "sWidth": "5%"}

                    ],
                    "fnRowCallback": function(nRow, aData, iDisplayIndex) {
                        /* Append the grade to the default row class name */
                        if (aData[6] == "N")
                        {
                            nRow.className = "gradeX odd";
                            $('td:eq(0)', nRow).html('<b>' + aData[1] + '</b>');
                            $('td:eq(1)', nRow).html('<b>' + aData[2] + '</b>');
                            $('td:eq(2)', nRow).html('<b>' + aData[3] + '</b>');
                            $('td:eq(3)', nRow).html('<b>' + aData[4] + '</b>');
                            $('td:eq(4)', nRow).html('<b>' + aData[5] + '</b>');
                            $('td:eq(5)', nRow).html('<b>' + aData[6] + '</b>');
                        }
                    },
                }
                ).makeEditable({
                    sAddURL: "CreateDatamap",
                    sAddHttpMethod: "GET",
                    oAddNewRowButtonOptions: {
                        label: "Add data-cerberus",
                        background: "#AAAAAA",
                        icons: {primary: 'ui-icon-plus'}
                    },
                    sDeleteHttpMethod: "POST",
                    sDeleteURL: "DeleteDatamap",
                    sAddDeleteToolbarSelector: ".dataTables_length",
                    oDeleteRowButtonOptions: {
                        label: "Remove",
                        icons: {primary: 'ui-icon-trash'}
                    },
                    sUpdateURL: "UpdateDatamap",
                    fnOnEdited: function(status) {
                        $(".dataTables_processing").css('visibility', 'hidden');
                    },
                    oAddNewRowFormOptions: {
                        title: 'Add Data Cerberus',
                        show: "blind",
                        hide: "explode",
                        width: "1000px",
                    },
                    "aoColumns": [
                        null,
                        {onblur: 'submit',
                            placeholder: ''},
                        {onblur: 'submit',
                            placeholder: ''},
                        {onblur: 'submit',
                            placeholder: ''},
                        {onblur: 'submit',
                            placeholder: ''},
                        {onblur: 'submit',
                            placeholder: ''},
                        {onblur: 'submit',
                            placeholder: ''}

                    ]
                })
            });


        </script>
        <script>
            function getValue()
            {
                var x = document.getElementById("testtest").value;
                return x;
            }
        </script>
        <script type="text/javascript">
            function updatePicture(value, columnName, id) {
                var sValue = value.value;
                var sColumnName = columnName;
                var sId = id;
                var xhttp = new XMLHttpRequest();
                xhttp.open("GET", "UpdatePicture?value=" + sValue + "&id=" + id + "&columnName=" + columnName, true);
                xhttp.send();
                var xmlDoc = xhttp.responseText;

            }
        </script>
        <link rel="Stylesheet" type="text/css" href="./js/wPaint/demo/demo.css" />
    </head>
    <body  id="wrapper" onLoad="LoadMyJs(null, null)">
        <%
            String uri = "?";

            String[] pageName = null;
            if (request.getParameterValues("page") != null && !request.getParameter("page").equals("All")) {
                pageName = request.getParameterValues("page");
                for (int a = 0; a < pageName.length; a++) {
                    uri += "&page=" + pageName[a];
                }
            };

            String[] stream = null;
            if (request.getParameterValues("stream") != null && !request.getParameter("stream").equals("All")) {
                stream = request.getParameterValues("stream");
                for (int a = 0; a < stream.length; a++) {
                    uri += "&stream=" + stream[a];
                }
            };

            String[] picture = null;
            if (request.getParameterValues("picture") != null && !request.getParameter("picture").equals("All")) {
                picture = request.getParameterValues("picture");
                for (int a = 0; a < picture.length; a++) {
                    uri += "&picture=" + picture[a];
                }
            };

            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IPictureService pictureService = appContext.getBean(IPictureService.class);
            

        %>
        <input id="testtest" value="<%=uri%>" style="display:none">    
        <div class="ncdescriptionfirstpart" style="vertical-align: central; clear:both">
            <p style="text-align:left">Data-Cerberus Implementation</p>
            <form action="Datamap.jsp" method="get" name="ExecFilters" id="ExecFilters">
                <div style="width: 300px;float:left">
                    <!--<p style="float:left">creator</p>-->
                    <select style="width: 300px;float:left" multiple="multiple"  id="stream" name="stream">
                    </select>
                </div>
                <div style="width: 300px;float:left">
                    <select style="width: 300px;float:left" multiple="multiple"  id="page" name="page">
                    </select>
                </div>
                <div style="width: 300px;float:left">
                    <select style="width: 300px;float:left" multiple="multiple"  id="picture" name="picture">
                    </select>
                </div>
                <div><input style="float:left" type="button" value="Apply Filter" onClick="document.ExecFilters.submit()"></div>
            <div><input style="float:right;width:40px; height:40px; border-width: 1px; border-radius: 20px;" type="button" value="R" title="Reporting" onclick="location.href = 'Reporting.jsp'"></div>
            </form>
        </div>
<!--        <div style="clear:both"><div style="float:left; width:550px"><p>data-cerberus forms</p></div>
        </div>-->
<div style="clear:both; height:10px"><p></p></div>
        <div style="float:left; width: 1200px;  font: 90% sans-serif">
            <table id="datamapList" class="display">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Stream</th>
                        <th>Page</th>
                        <th>DataCerberus</th>
                        <th>Picture</th>
                        <th>Xpath</th>
                        <th>Impl</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <!--<div style="clear:both; height:30px"><p>Pictures</p></div>-->
        <div style="clear:both; height:20px"><p></p></div>
        <div id="picturesListDiv" style="float:left; width: 400px;background-color:#E2E4FF">
            <div class="ncdescriptionheader" style="height:30px" >List of Pictures</div>
            <div id="pictureList" name="pictureList"></div>
        </div>

        <div id="pictures" style="float:left; width: 650px;background-color:#E2E4FF">    
            <div class="ncdescriptionheader" style="height:30px" >
                <input id="idInput" style="float:left; display:none" value="">
                <p style="float:left;">Page:</p><input id="pageInput" style="float:left;" value="" onChange="javascript: updatePicture(this, 'page', 'id')">
                <p style="float:left;">Picture:</p><input id="pictureInput" style="float:left;" value="" onChange="javascript: updatePicture(this, 'picture', 'id')">
                <button id="deletePicture" style="float:right" onClick="javacript: deletePicture('')">Delete Picture</button>
                <button id="addPicture" style="float:right" onClick="javacript: popup('AddPicture.jsp')">Add Picture</button>
            </div>
            <div id="contentParent">
                <!--<div id="contentDiv" class="content-box" style="background-color:#E2E4FF">-->
                <%@ include file="PictureDiv.jsp" %>
                <!--           <div id="wPaint" style="position:relative; width:600px; height:400px; background-color:#7a7a7a; margin:70px auto 20px auto;"></div>
                            <center id="wPaint-img"></center>
                            </div>-->
            </div>
            <div class="nctablefooter" style="height:6px"></div>
            <br>


        </div>


        <div>
            <form id="formAddNewRow" action="#" title="Add Data Cerberus" style="width:350px" method="post">
                <div style="width: 250px; float:left; display:none">
                    <label for="id" style="font-weight:bold">id</label>
                    <input id="id" name="id" style="width:150px;" 
                           class="ncdetailstext" rel="0" >
                </div>
                <div style="width: 250px; float:left">
                    <label for="stream" style="font-weight:bold">Stream</label>
                    <input id="stream" name="stream" style="width:150px;" 
                           class="ncdetailstext" rel="1" >
                </div>
                <div style="width: 310px; float:left">
                    <label for="page" style="font-weight:bold">Page</label>
                    <input id="page" name="page" style="width:210px;" 
                           class="ncdetailstext" rel="2" >
                </div>
                <div style="width: 310px; float:left">
                    <label for="datacerberus" style="font-weight:bold">DataCerberus</label>
                    <input id="datacerberus" name="datacerberus" style="width:210px;" 
                           class="ncdetailstext" rel="3" >
                </div>
                <div style="width: 310px; float:left">
                    <label for="picture" style="font-weight:bold">Picture</label>
                    <input id="picture" name="picture" style="width:210px;" 
                           class="ncdetailstext" rel="4" >
                </div>
                <div style="width: 310px; float:left">
                    <label for="xpath" style="font-weight:bold">Xpath</label>
                    <input id="xpath" name="xpath" style="width:210px;" 
                           class="ncdetailstext" rel="5" >
                </div>
                <div style="width: 250px; float:left">
                    <label for="implemented" style="font-weight:bold">Implemented</label>
                    <input id="implemented" name="implemented" style="width:150px;" 
                           class="ncdetailstext" rel="6" >
                </div>
                <br />
                <button id="btnAddNewRowOk">Add</button>
                <button id="btnAddNewRowCancel">Cancel</button>
            </form>
        </div>
        

        <script type="text/javascript">
            (document).ready($.get('GetDistinctValueFromTableColumn?table=Datamap&colName=Stream', function(data) {
                for (var i = 0; i < data.length; i++) {
                    $("#stream").append($("<option></option>")
                            .attr("value", data[i])
                            .text(data[i]))
                }
                $("#stream").multiselect({
                    header: "Stream",
                    noneSelectedText: "Select Stream",
                    selectedText: "# of # stream selected"
                });

            }
            ));
        </script>
        <script type="text/javascript">
            (document).ready($.get('GetDistinctValueFromTableColumn?table=Datamap&colName=Page', function(data) {
                for (var i = 0; i < data.length; i++) {
                    $("#page").append($("<option></option>")
                            .attr("value", data[i])
                            .text(data[i]))
                }
                $("#page").multiselect({
                    header: "Page",
                    noneSelectedText: "Select Page",
                    selectedText: "# of # page selected"
                });

            }
            ));
        </script>
        <script type="text/javascript">
            (document).ready($.get('GetDistinctValueFromTableColumn?table=Datamap&colName=Picture', function(data) {
                for (var i = 0; i < data.length; i++) {
                    $("#picture").append($("<option></option>")
                            .attr("value", data[i])
                            .text(data[i]))
                }
                $("#picture").multiselect({
                    header: "Picture",
                    noneSelectedText: "Select Picture",
                    selectedText: "# of # Picture selected"
                });

            }
            ));
        </script>
        <script type="text/javascript">
            var test = document.getElementById("testtest").value;
            function findAllPictures(test) {
                $.get('FindAllPicture'+test, function(data) {
                    $("#pictureList").empty();
                    for (var i = 0; i < data.aaData.length; i++) {
                        $("#pictureList").append($("<a></a>")
                                .attr("style", "cursor: pointer")
                                .attr("onclick", "$('#wPaint').fadeOut('slow');LoadMyJs('" + data.aaData[i][0] + "','" + data.aaData[i][3] + "');loadDataInput('" + data.aaData[i][0] + "','"+data.aaData[i][1]+"','" + data.aaData[i][2] + "');")
                                .attr("id","picture_"+ data.aaData[i][0])
                                .text(data.aaData[i][2]));
                        $("#pictureList").append("</br>");
                    }
                });
            }

            (document).ready(findAllPictures(test));
        </script>
        <script>
            function deletePicture(id) {

                if (confirm('Beware, the picture will be deleted')) {
                    window.location = "DeletePicture?id=" + id;
                    alert('toto');
                }
            }
        </script>
        <script>
            function popup(mylink) {
                window.open(mylink, 'popup',
                        'width=600,height=500,scrollbars=yes,menubar=false,location=false');
            }
        </script>
        <!-- jQuery UI -->
        <script type="text/javascript" src="./js/wPaint/lib/jquery.ui.core.1.10.3.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/lib/jquery.ui.widget.1.10.3.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/lib/jquery.ui.mouse.1.10.3.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/lib/jquery.ui.draggable.1.10.3.min.js"></script>

        <!-- wColorPicker -->
        <link rel="Stylesheet" type="text/css" href="./js/wPaint/lib/wColorPicker.min.css" >
        <script type="text/javascript" src="./js/wPaint/lib/wColorPicker.min.js"></script>

        <!-- wPaint -->
        <link rel="Stylesheet" type="text/css" href="./js/wPaint/wPaint.min.css" >
        <script type="text/javascript" src="./js/wPaint/src/wPaint.js"></script>
        <script type="text/javascript" src="./js/wPaint/src/wPaint.utils.js"></script>
        <script type="text/javascript" src="./js/wPaint/plugins/main/wPaint.menu.main.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/plugins/text/wPaint.menu.text.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/plugins/shapes/wPaint.menu.main.shapes.min.js"></script>
        <script type="text/javascript" src="./js/wPaint/plugins/file/wPaint.menu.main.file.min.js"></script>
        <!--script type="text/javascript" src="./js/wPaint/plugins/zoom/src/wPaint.menu.main.zoom.js"></script-->
        <script>
            function LoadMyJs(id, picture) {

            var sId = null;
            var sPicture = null;
            if (id !== null && picture !== null) {
                    sId = id;
                   sPicture = picture;
                }

                var images = ['./js/wPaint/test/uploads/redoute.jpg'];



                function saveImg(image) {
                    var _this = this;

                    $.ajax({
                        type: 'POST',
                        url: './UploadPicture?id=' + sId,
                        data: {image: image},
                        success: function(resp) {

                            // doesn't have to be json, can be anything
                            // returned from server after upload as long
                            // as it contains the path to the image url
                            // or a base64 encoded png, either will work
                            //resp = $.parseJSON(resp);

                            // update images array / object or whatever
                            // is being used to keep track of the images
                            // can store path or base64 here (but path is better since it's much smaller)
                            images.push(image);
                            findAllPictures(test);

                            // do something with the image
                            $('#wPaint-img').attr('src', image);

                            // internal function for displaying status messages in the canvas
                            _this._displayStatus('Image saved successfully');

                        }
                    });
                }

                function loadImgBg() {

                    // internal function for displaying background images modal
                    // where images is an array of images (base64 or url path)
                    // NOTE: that if you can't see the bg image changing it's probably
                    // becasue the foregroud image is not transparent.
                    this._showFileModal('bg', images);
                }
                /*
                function zoomImgBg() {

                    // internal function for displaying background images modal
                    // where images is an array of images (base64 or url path)
                    // NOTE: that if you can't see the bg image changing it's probably
                    // becasue the foregroud image is not transparent.
                    this._zoomImgBg('bg', images);
                }
                */
                function loadImgFg() {

                    // internal function for displaying foreground images modal
                    // where images is an array of images (base64 or url path)
                    this._showFileModal('fg', images);
                }

                // remove data of the current wPaint element
                $.removeData(wPaint);
                $('#wPaint').empty();

                // Create new one wPaint
                $('#wPaint').wPaint({
                    path: './js/wPaint/',
                    image: sPicture,
                    bg: '#E2E4FF',
                    menuOffsetLeft: 0,
                    menuOffsetTop: -50,
                    saveImg: saveImg,
                    loadImgBg: loadImgBg,
                    loadImgFg: loadImgFg,
                }).fadeIn("slow");
//                    zoomImgBg: zoomImgBg,
            
            }
        </script>
        <script>
            function loadDataInput(id, page, name){
                document.getElementById('pageInput').value= page;
                document.getElementById('pictureInput').value = name;
                document.getElementById('idInput').value = id;
                $("[aria-controls='datamapList']").val(name);
                
                //simulate keydown keyup to start search on image text.
                $("[aria-controls='datamapList']").keydown();
                $("[aria-controls='datamapList']").keyup();
            }
                    </script>
    </body>
</html>
