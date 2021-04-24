<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%= basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            if (window.top!==window){
                window.top.location=window.location;
            }
            $(":text").val("");
            $(":text").focus();
            $(":button").click(function () {
               login()
            })
            //为窗口绑定敲键盘事件
            $(window).keydown(function (event) {
                if (event.keyCode === 13){
                   login()
                }
            })
        })
        //验证登陆,一定要写在$(function(){})外面,其实写在里边也行(最好放在第一行)
        function login() {
            //去除前后空白$.trim(文本)
            var loginAct=$.trim($(":text").val());
            var loginPwd=$(":password").val();
            if(loginAct === "" || loginPwd === ""){
                return  $("#msg").text("账号密码不能为空");
            }
            $.ajax({
                url:"settings/user/login.do",
                data:{
                     "loginAct":loginAct,
                     "loginPwd":loginPwd
                },
                type :"post",
                datatype :"json",
                success :function (resp) {
                  /*
                  data:{"success":true/false ,"msg":"哪里出错了"}
                   */
                    if (resp.success){
                        window.location.href="workbench/index.jsp";
                    }else {
                        $("#msg").text(resp.msg);
                    }
                }
            })
        }
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2020&nbsp;杨宇</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" name="loginName">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" name="loginPwd">
                </div>
                <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" style="color: red"></span>

                </div>
<%--                按钮写在form表单中,不加type="button",默认是submit--%>
                <button type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
