<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>My JSP 'demo.jsp' starting page</title>
	
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="/WEB-INF/jsp/common/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/sfile/css/jquery-ui.css?${v}">
	
</head>

<body>
	<p>    
		<label>自动提示:</label>    
		<input type="text" id="tags" />
	</p>
	<a id="testIdList">测试传List参数</a>
	
	<div>
		<img id="imgFile" src="http://img.jumper-health.com/img/food/303D48E853ACF569FDBD8B3418C58A44.jpg" width="50px" height="50px">
		<a id="download">测试下载网络文件</a>
	</div>
	
</body>
<jsp:include page="/WEB-INF/jsp/common/foot.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/sfile/js/jquery-ui.js?${v}" ></script>
<script type="text/javascript">    
	$(document).ready(function() {
	    var availableTags = [
	      "ActionScript",
	      "AppleScript",
	      "Asp",
	      "BASIC",
	      "C",
	      "C++",
	      "Clojure",
	      "COBOL",
	      "ColdFusion",
	      "Erlang",
	      "Fortran",
	      "Groovy",
	      "Haskell",
	      "Java",
	      "JavaScript",
	      "Lisp",
	      "Perl",
	      "PHP",
	      "Python",
	      "Ruby",
	      "Scala",
	      "Scheme"
	    ];
	    var sou = [
			{ label: "Chinese", value: 11, sayHi: "你好" },
			{ label: "English", value: 12, sayHi: "Hello" },
			{ label: "Spanish", value: 3, sayHi: "Hola" },
			{ label: "Russian", value: 4, sayHi: "Привет" },
			{ label: "French", value: 5, sayHi: "Bonjour" },
			{ label: "Japanese", value: 6, sayHi: "こんにちは" }
		];
		
		//////////////////////////////////////////////////////////////
		
	    $("#tags").autocomplete({
	    	autoFocus: true,
	      	minLength: 1,        //触发自动填充需要的最小字符长度
	      	source: function(request, response) {
	      		var postData = {nickName: request.term, size: 15};
	      		$.post(baseUrl + "/userPage/findByNickName", postData, function(ret){
	      			if(ret.msg == 1){
	      				var searchList = $.map(ret.data, function(item, index){
	      					return {label: item.nickName, value: item.id, id: item.id};
	      				});
	      				response(searchList);
	      			}else{
	      				alert("搜索异常");
	      			}
	      		});
	      	},
	      	select: function(event, ui){
	      		alert("选中了："+ui.item.label+", id："+ui.item.id);
	      	}
	      	
	    });
	    
	    //测试传List参数
	    $("#testIdList").on("click", function() {
	    	var postDate = new Array();
	    	postDate.push(3);
	    	postDate.push(4);
			$.post(baseUrl + "/userPage/testIdList", {idList : postDate}, function(ret) {
				alert(ret.msgbox);				
			}, "json");
		});
		
		//测试下载网络文件
		$("#download").click(function() {
			var urlString = $("#imgFile").attr("src");
			window.location.href = baseUrl + "/userPage/downloadNet?urlString=" + urlString;
		});
	    
	});
	/* $(function(){
		
	}); */
</script>   

</html>
