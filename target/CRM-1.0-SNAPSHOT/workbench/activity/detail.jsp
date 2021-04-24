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

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		

		/**
		 * 以下代码因为是动态添加的,不能使用以下语法,需要使用on事件绑定
		 *
		 * */
		// $(".remarkDiv").mouseover(function(){
		// 	$(this).children("div").children("div").show();
		// });
		//
		// $(".remarkDiv").mouseout(function(){
		// 	$(this).children("div").children("div").hide();
		// });
		// $(".myHref").mouseover(function(){
		// 	$(this).children("span").css("color","red");
		// });
		//
		// $(".myHref").mouseout(function(){
		// 	$(this).children("span").css("color","#E6E6E6");
		// });
		showRemarkList();
		$("#remarkBody").on("mouseover",".remarkDiv",function () {
			$(this).children("div").children("div").show()
		})
		$("#remarkBody").on("mouseout",".remarkDiv",function () {
			$(this).children("div").children("div").hide()
		})
		$("#saveRemarkBtn").click(function () {
			$.ajax({
				url:"workbench/activity/saveRemark.do",
				data:{
					"noteContent":$.trim($("#remark").val()),
					"activityId":"${act.id}"
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						$("#remark").val("")
						var html=""
						html+='<div id="'+data.actRem.id+'" class="remarkDiv" style="height: 60px;">'
						html+='<img title="${act.name}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
						html+='<div style="position: relative; top: -40px; left: 40px;" >'
						html+='<h5 id="e'+data.actRem.id+'">'+data.actRem.noteContent+'</h5>'
						html+='<font color="gray">市场活动-</font> <b>${act.name}</b> <small style="color: gray;" id="s'+data.actRem.id+'">'+data.actRem.createTime+'由'+data.actRem.createBy+'</small>'
						html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
						html+='<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+data.actRem.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>'
						html+='&nbsp;&nbsp;&nbsp;&nbsp;'
						html+='<a class="myHref" href="javascript:void(0);" onclick="removeRemark(\''+data.actRem.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>'
						html+='</div>'
						html+='</div>'
						html+='</div>'
						$("#remarkDiv").before(html)
					}else {
						alert("添加失败")
					}
				}
			})
		})
		$("#updateRemarkBtn").click(function () {
			var id=$("#remarkId").val()
			var noteContent=$.trim($("#noteContent").val())
			$.ajax({
				url:"workbench/activity/updateRemark.do",
				type:"post",
				dataType:"json",
				data:{
					"id":id,
					"noteContent":noteContent
				},
				success:function (data) {
					if (data.success){
						$("#e"+id).text(noteContent)
						$("#s"+id).text(data.ac.editTime+"由"+data.ac.editBy)
						$("#editRemarkModal").modal("hide")
					}else {
						alert("修改备注失败")
					}
				}
			})
		})
	});
	function showRemarkList() {
		$.ajax({
			url:"workbench/activity/getRemarkListByActId.do",
			//在js中的el表达式要加引号
			data:{"activityId":"${act.id}"},
			type:"get",
			dataType:"json",
			success:function(data){
				var html=""
				$.each(data,function(i,e){
					/*
					javascript:void(0)表示禁用超链接,只能通过触发事件的形式来操作。
					onclick="removeRemark(\''+e.id+'\')":
					e.id是一个32位字符串变量'x...x',解释执行的时候e.id=x...x,而x...x是一个字符串,所以需要加\'转义包装成一个字
					符串,需要注意的是如果onclick=""是双引号,\'必须是单引号。
					 */
					html+='<div id="'+e.id+'" class="remarkDiv" style="height: 60px;">'
					html+='<img title="${act.name}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
					html+='<div style="position: relative; top: -40px; left: 40px;" >'
					html+='<h5 id="e'+e.id+'">'+e.noteContent+'</h5>'
					html+='<font color="gray">市场活动-</font> <b>${act.name}</b> <small style="color: gray;" id="s'+e.id+'">'+(e.editFlag==0?e.createTime:e.editTime)+'由'+(e.editFlag==0?e.createBy:e.editBy)+'</small>'
					html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
					html+='<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+e.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>'
					html+='&nbsp;&nbsp;&nbsp;&nbsp;'
					html+='<a class="myHref" href="javascript:void(0);" onclick="removeRemark(\''+e.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>'
					html+='</div>'
					html+='</div>'
					html+='</div>'
				})
				$("#remarkDiv").before(html)
			}
		})
	}
	function editRemark(id) {
		$("#remarkId").val(id)
		$("#noteContent").val($("#e"+id).text())
		$("#editRemarkModal").modal("show")
	}
	function removeRemark(id) {
		$.ajax({
			url:"workbench/activity/deleteRemark.do",
			data:{
				"id":id
			},
			type: "post",
			dateType:"json",
			success:function (data) {
				if (data.success){
					/*
					showRemarkList()
						这个方法每次删除之后都会保留原来的记录
					 */
					$("#"+id).remove()
				}else {
					alert("删除失败")
				}
			}
		})
	}
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>



	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${act.name} <small>${act.startDate} ~ ${act.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${act.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${act.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${act.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${act.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${act.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${act.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${act.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${act.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${act.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${act.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
<%--		<!-- 备注1 -->--%>
<%--		<div class="remarkDiv" style="height: 60px;">--%>
<%--			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
<%--			<div style="position: relative; top: -40px; left: 40px;" >--%>
<%--				<h5>哎呦！</h5>--%>
<%--				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>--%>
<%--				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--				</div>--%>
<%--			</div>--%>
<%--		</div>--%>

		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>