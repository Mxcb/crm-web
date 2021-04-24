<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%= basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){
		$.ajax({
			url:"workbench/activity/getUserList.do",
			type:"get",
			dataType:"json",
			success:function (data) {
				var html="";
				$.each(data,function(i,element){
					html+="<option value='"+element.id+"'>"+element.name+"</option>";
				})
				$("#create-marketActivityOwner").html(html);
				/**
				 * 操作模态窗口的方式:
				 * 		模态窗口对象.modal("show/hide")
				 */
				$("#createBtn").click(function(){
					$("#createActivityModal").modal("show");
				})
				//在js中使用el表达式,el表达式必须套用在字符串中
				$("#create-marketActivityOwner").val("${user.id}")
			}
		})
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				dataType: "json",
				type: "post",
				data:{
					"owner":$.trim($("#create-marketActivityOwner").val()),
					"name":$.trim($("#create-marketActivityName").val()),
					"startDate":$.trim($("#create-startTime").val()),
					"endDate":$.trim($("#create-endTime").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				success:function (data) {
					if (data.success){
						//添加成功后局部刷新活动信息列表
						/*
						* $("#activityPage").bs_pagination("getOption","currentPage")表示操作后停留在当前页
						* $("#activityPage").bs_pagination("getOption","rowsPerPage")表示维持设置好的pagesize
						*
						**/
						pageList(1,$("#activityPage").bs_pagination("getOption","rowsPerPage"))
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						//清空模态窗口中的数据,jQuery没有提供重置方法,js有这个方法
						document.getElementById("create-reset").reset();
					}else {
						alert("添加市场活动失败")
					}
				}
			})
		})
		function pageList(pageNo,pageSize){
			//每次分页都要刷新复选框的选中状态
			$("#check-all").prop("checked",false)
			//查询之前将隐藏域中的值取出
			$("#search-name").val($.trim($("#hidden-name").val()))
			$("#search-owner").val($.trim($("#hidden-owner").val()))
			$("#search-startTime").val($.trim($("#hidden-startDate").val()))
			$("#search-endTime").val($.trim($("#hidden-endDate").val()))
			$.ajax({
				url:"workbench/activity/pageList.do",
				dataType:"json",
				type:"get",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"name":$.trim($("#search-name").val()),
					"owner":$.trim($("#search-owner").val()),
					"startTime":$.trim($("#search-startTime").val()),
					"endTime":$.trim($("#search-endTime").val())
				},
				success:function (data) {
					var html="";
					$.each(data.dataList,function (i,e) {
						html+='<tr class="active">'
						html+='<td><input type="checkbox" name="check-o" value="'+e.id+'"></td>'
						html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+e.id+'\';">发传单</a></td>'
						html+='<td>'+e.owner+'</td>'
						html+='<td>'+e.startDate+'</td>'
						html+='<td>'+e.endDate+'</td>'
						html+='</tr>'
					})
					$("#activityBody").html(html)
					//总页数
					var totalPages=Math.ceil(data.total/pageSize)
					//数据处理完毕后,结合分页插件,对前端展现分页信息
					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 10, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 3, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						//回调函数,在点击分页组件时触发
						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});
				}
			})
		}
		pageList(1,2)
		$("#searchBtn").click(function(){
			//点击查询按钮给隐藏域赋值
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startTime").val()))
			$("#hidden-endDate").val($.trim($("#search-endTime").val()))
			pageList(1,2)
		})
		$("#check-all").click(function(){
			$("input[name=check-o]").prop("checked",this.checked)
		})
		//动态生成的元素[后来拼接的元素],是不能够以普通绑定事件的形式来进行操作的
		//动态生成的元素,我们要以on方法的形式来触发事件
		/**
		 * 语法:
		 * 		$(需要绑定元素的有效[不是动态生成的]的的外层元素).on(绑定事件的方式,需要绑定的元素的jQuery对象,回调函数)
		 */
		$("#activityBody").on("click",$("input[name=check-o]"),function(){
			$("#check-all").prop("checked",$("input[name=check-o]").length == $("input[name=check-o]:checked").length)
		})

		$("#deleteBtn").click(function(){
			var $de=$("input[name=check-o]:checked")
			if ($de.length==0){
				alert("请选择需要删除的记录")
			}else {
				if (confirm("确认删除所选中的记录吗?")){
					//删除操作,拼接参数
					var param="";
					$.each($de,function(i,e){
						param+="id="+e.value;
						if (i<$de.length-1){
							param+="&"
						}
					})
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){
							if (data.success){
								pageList(1,("#activityPage").bs_pagination("getOption","rowsPerPage"))
							}else {
								alert("删除市场活动失败")
							}
						}
					})
				}
			}
		})
		//为修改按钮绑定事件
		$("#editBtn").click(function(){
			var $co=$("input[name=check-o]:checked")
			if ($co.length==0){
				alert("请先选择一条记录")
			}else if ($co.length>1){
				alert("只能选择一条记录")
			}else {
				var id=$co.val()
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						var html=""
						$.each(data.uList,function(i,e){
							html+="<option value='"+e.id+"'>"+e.name+"</option>"
						})
						$("#edit-marketActivityOwner").html(html)

						$("#edit-id").val(data.a.id)
						$("#edit-marketActivityName").val(data.a.name)
						$("#edit-marketActivityOwner").val(data.a.owner)
						$("#edit-startTime").val(data.a.startDate)
						$("#edit-endTime").val(data.a.endDate)
						$("#edit-description").val(data.a.description)
						$("#edit-cost").val(data.a.cost)

						$("#editActivityModal").modal("show")
					}
				})
				$("#updateBtn").click(function () {
					$.ajax({
						url:"workbench/activity/update.do",
						dataType: "json",
						type: "post",
						data:{
							"id":$.trim($("#edit-id").val()),
							"owner":$.trim($("#edit-marketActivityOwner").val()),
							"name":$.trim($("#edit-marketActivityName").val()),
							"startDate":$.trim($("#edit-startTime").val()),
							"endDate":$.trim($("#edit-endTime").val()),
							"cost":$.trim($("#edit-cost").val()),
							"description":$.trim($("#edit-description").val())
						},
						success:function (data) {
							if (data.success){
								//添加成功后局部刷新活动信息列表
								pageList($("#activityPage").bs_pagination("getOption","currentPage"),
										$("#activityPage").bs_pagination("getOption","rowsPerPage"))
								//关闭模态窗口
								$("#editActivityModal").modal("hide");
								//清空模态窗口中的数据,jQuery没有提供重置方法,js有这个方法
								document.getElementById("create-reset").reset();
							}else {
								alert("修改市场活动失败")
							}
						}
					})
				})
			}
		})
	});
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="create-reset" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" >
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" >
							</div>
						</div>
                        <div class="form-group">
                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--
									文本域textarea,一定要以标签对的形式出现
								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  <input type="hidden" id="hidden-name">
				  <input type="hidden" id="hidden-owner">
				  <input type="hidden" id="hidden-startDate">
				  <input type="hidden" id="hidden-endDate">
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="check-all"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>