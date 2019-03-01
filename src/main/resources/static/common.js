var pipercontext ="";
var defWidth = 1200 + "px";
var defHeight = 674 + "px";
$(document).ready(function(){
	$(".date").each(function(i){
		var val = $(this).text();
		if(val != null){
			var dateLong = parseInt(val);
			if(dateLong < 1000000000000){
				$(this).text(formatDateTime(dateLong * 1000));	
			}else{
				$(this).text(formatDateTime(dateLong));
			}
		}
	});
	console.log("页面加载完成" + formatDateTime(null));
	isLoad = 1;
	layui.use(['form'], function(){
		  var formObj = layui.form;
		  checkFormRender(formObj);
	});
	var height = $(window).height() * 0.96;
	var width = $(window).width() * 0.96;
	if(width < defWidth){
		defWidth = width + "px";
	}
	if(height < defHeight){
		defHeight = height + "px";
	}
	updateTableUI();
});
function updateTableUI(){
	$("table").parent().css("overflow","auto");
	$("table td").css("min-width","60px");
	$("table th").css("min-width","60px");
	var winHeight = $(window).height();
	var winWidth = $(window).width();
	$.each($("table td"), function(index, obj) {
		var textVal = $(obj).text();
		var viewWidth = $(obj).width();
		if($(obj).find(".date").length > 0 || $(obj).hasClass("date")){
			$(obj).attr("nowrap","nowrap");
		}
		if($(obj).find(".layui-btn").length > 0){
			$(obj).attr("nowrap","nowrap");
			$(obj).css("word-break","break-all");
		}
//		console.log("textVal:" + textVal + ",viewWidth:" + viewWidth);
	});
}
window.onresize = function(){
	var height = $(window).height() * 0.96;
	var width = $(window).width() * 0.96;
	if(width < defWidth){
		defWidth = width + "px";
	}
	if(height < defHeight){
		defHeight = height + "px";
	}
}
function formatDateTime(inputTime) {    
	if(inputTime == 0){
		return "";
	}
    var date; 
    if(inputTime == null){
    	date = new Date();
    }else{
    	date = new Date(inputTime);
    }
    var y = date.getFullYear();    
    var m = date.getMonth() + 1;    
    m = m < 10 ? ('0' + m) : m;    
    var d = date.getDate();    
    d = d < 10 ? ('0' + d) : d;    
    var h = date.getHours();  
    h = h < 10 ? ('0' + h) : h;  
    var minute = date.getMinutes();  
    var second = date.getSeconds();  
    minute = minute < 10 ? ('0' + minute) : minute;    
    second = second < 10 ? ('0' + second) : second;   
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;    
};
layui.use(['form'], function(){
	  var formObj = layui.form;
		//监听指定开关
	  formObj.on('switch(switchTest)', function(data){
	    layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
	      offset: '6px'
	    });
	    layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
	  });
		formObj.on('radio(outside)', function(data){
			var val = data.value;
			if(val == 0){
				$("#prefix").css("display","inline-block");
				$("#suffix").css("display","inline-block");
				$("#prefix").text("信息");
				$("#suffix").text("之后");
			}else{
				$("#prefix").css("display","inline-block");
				$("#suffix").css("display","none");
				$("#prefix").text("模块");
				$("#suffix").text("之后");
			}
		});
		console.log("form渲染完成" + formatDateTime(null));
		isRender = 1;
		checkFormRender(formObj);
});
var isRender = 0;
var isLoad = 0;
var renderSecond = 0;
function checkFormRender(formObj){
	if(isRender == 0 || isLoad == 0){
		return;
	}
	if(renderSecond > 5){
		console.log("已经从新渲染5次了，真的无能为力了");
	}
	console.log("开始第" + renderSecond + "次检查");
	$("input[type='radio'],select").each(function(i){
		console.log($(this).next().attr("class") + " -- " + $($(this).next()).hasClass("layui-unselect"));
		if(!$($(this).next()).hasClass("layui-unselect")){
			console.log("开始第" + renderSecond + "次渲染");
			formObj.render();
			checkFormRender(formObj);
			renderSecond = renderSecond + 1;
			return;
		}
	});
}
function previewImg(obj) {
	 var img = new Image();  
         img.src = obj.src;
         img.onload = function(){
			var height = $(window).height() * 0.96;
			var width = $(window).width() * 0.96;
			if(width > img.width){
				width = img.width;
			}
			if(height > img.height){
				height = img.height;
			}
			var imgHtml = "<img src='" + obj.src + "' style='width:" + width + "px;height:"+height+"px;'/>";  
			//捕获页  
			layer.open({  
				type: 1,  
				shade: false,  
				title: false, //不显示标题  
				area: [width+'px', height+'px'],  
				content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响  
				cancel: function () {}  
            }); 
         };
    }