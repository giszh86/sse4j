var BaseUrl = 'http://localhost:8080/sse4j/';
var LocatingUrl = BaseUrl+'sse/Locating?gzip=false';
var SearchingUrl = BaseUrl+'sse/Searching?gzip=false';
var RoutingUrl = BaseUrl+'sse/Routing?gzip=false';
var MatchingUrl = BaseUrl+'sse/Matching?gzip=false';
var HotMapUrl = BaseUrl+'sse/HotTile?';
var UrlStart = '<?xml version="1.0" encoding="UTF-8"?>';
var UrlEnd = '';

/***************************************************************************/
function SSEUtil(){	
}
SSEUtil.createHttpRequest = function(){
	var request = false;
	if(window.XMLHttpRequest){
		request = new XMLHttpRequest();
	}else if(window.ActiveXObject){
		try{
			request = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e1){
			try{
				request = new ActiveXObject("Microsoft.XMLHTTP");
		    }catch(e2){
		        alert(e2);
		    }
		}
	}
	return request;
}
SSEUtil.requestOnready = function(xml, wsdl, func){
	var request = SSEUtil.createHttpRequest();
	if(request){
		try{
			request.open("post", wsdl, true);
		}catch(e1){
			alert(e1);
			return;
		}
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.send(xml);
		request.onreadystatechange = function(){
			if (request.readyState==4 && request.status==200){
				var xmlDom = request.responseXML.documentElement;				
				if (xmlDom!=null && xmlDom!=undefined){
					var result = new SSEResult;
		            var faultString = xmlDom.getElementsByTagName("faultString");
		            var resultCode = xmlDom.getElementsByTagName("resultCode");
		            var jsonString = xmlDom.getElementsByTagName("jsonString");
		            if (navigator.appName.indexOf("Explorer") > -1){
						result.faultString = faultString[0].text;
		                result.resultCode = resultCode[0].text;
						result.jsonString = jsonString[0].text;
					}else{
						result.faultString = faultString[0].textContent;
		                result.resultCode = resultCode[0].textContent;
						result.jsonString = jsonString[0].textContent;
					}				            
					eval(func(result));
				}
			}
		}
	}
}
SSEUtil.MC2LL = function(point){
	var lat = (point.y/20037508.342789)*180;
	var lon = (point.x/20037508.342789)*180;
	lat = 180/Math.PI*(2*Math.atan(Math.exp(lat*Math.PI/180))-Math.PI/2);
	return new SSEPoint(lon.toFixed(6),lat.toFixed(6));
}
SSEUtil.createJS = function(url){				
	var script=document.createElement("script");
    script.type='text/javascript';
    script.src=url;
    document.getElementsByTagName("head")[0].appendChild(script);
}
SSEUtil.clip = function(n, min, max){
	return Math.min(Math.max(n, min), max);
}
SSEUtil.pixelToLL = function(x, y, size){
	x = (SSEUtil.clip(x,0,size-1) / size) - 0.5;
	x = 360 * x;
	y = 0.5 - (SSEUtil.clip(y,0,size-1) / size);
	y = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;	
	return new SSEPoint(x,y);
}

function Radix(){	
}
Radix.charindex = function(value){
	var idx = value;
	if (idx >= 48 && idx <= 57) {
		idx -= 48;
	} else if (idx >= 65 && idx <= 90) {
		idx -= 55;
	} else {
		idx = -1;
	}
	return idx;
}
Radix.x2h = function(value,radix){
	if (value==null || value=='')
		return 0;
	value = value.toUpperCase();			
	var n = (value.charAt(0) == '-');
	if (n && value.length == 1)
		return 0;
	else if (n)
		value = value.substring(1);
	
	var result = 0;
	for (var i=0;i<value.length;i++) {		
		result += Math.pow(radix, i)*Radix.charindex(value.charCodeAt(value.length-i-1));
	}			
	
	if (n)
		return -result;
	else
		return result;
}

/***************************************************************************/

function SSEPoint(lon,lat){
	this.lon = lon;
	this.lat = lat;
	this.toString=function(){
		return (this.lon+","+this.lat);
	}
	this.equals=function(pt){
		if(pt){
			return ((this.lat==pt.lat)&&(this.lon==pt.lon));
		}
		return false;
	}
}
function SSEFilter(key,preference,keyword,count,distance,geometryWKT){
	this.key = key;
	this.preference = preference;//'POI' 'NET' 'DIST'
	this.keyword = keyword;	
	this.count = count;
	this.distance = distance;
	this.geometryWKT = geometryWKT;
}
function SSERouter(key,preference,startPoint,endPoint,viaPoints){
	this.key = key;
	this.preference = preference;//'Fastest' 'Shortest' 'OnFoot'
	this.startPoint = startPoint;//SSEPoint
	this.endPoint = endPoint;//SSEPoint
	this.viaPoints = viaPoints;//Array(SSEPoint)
}
function SSEResult(faultString,resultCode,jsonString){
	this.faultString = faultString;
	this.resultCode = resultCode;
	this.jsonString = jsonString;
	
	/**
	 * get geocoding result
	 * @return SSEPoint
	 */
	this.returnByGeocoding = function(){
		if(+this.resultCode==1){
			//var json = JSON.parse(this.jsonString);
			var json = eval('('+this.jsonString+')');
			return SSEUtil.MC2LL(json);
		}else{
			return false;
		}
	};
	
	/**
	 * get reversegeocoding result
	 * @return String
	 */
	this.returnByReGeocoding = function(){
		if(+this.resultCode==1){
			//var json = JSON.parse(this.jsonString);
			var json = eval('('+this.jsonString+')');
			return json.geoc;
		}else{
			return false;
		}
	};
	
	/**
	 * get search result
	 * @return [{id(String),title(String),wkt(SSEPoint)}]
	 */
	this.returnBySearch = function(){
		if(+this.resultCode==1){
			//var json = JSON.parse(this.jsonString);
			var json = eval('('+this.jsonString+')');
			for(var i=0;i<json.length;i++){
				var wkt = json[i].wkt;
				if(wkt.indexOf('POINT')>-1){
					wkt = wkt.substring(wkt.lastIndexOf('(')+1,wkt.lastIndexOf(')'));
					var awkt = wkt.split(' ');					
					json[i].wkt = SSEUtil.MC2LL({"x":awkt[0],"y":awkt[1]});
				}else if(wkt.indexOf('LINESTRING')>-1){
					wkt = wkt.substring(wkt.lastIndexOf('(')+1,wkt.lastIndexOf(')'));
					var awkt = wkt.split(', ');
					for(var j=0;j<awkt.length;j++){
						var aawkt = awkt[j].split(' ');
						awkt[j] = SSEUtil.MC2LL({"x":aawkt[0],"y":aawkt[1]});
					}
					json[i].wkt = awkt;
				}else{//Polygon or GeometryCollection
					//TODO
				}
			}
			return json;
		}else{
			return false;
		}
	};
	
	/**
	 * get poiinfo result
	 * @return {id,name,kind,phone,address,remark,vertex(SSEPoint)}
	 */
	this.returnByPoiInfo = function(){
		if(+this.resultCode==1){
			//var json = JSON.parse(this.jsonString);
			var json = eval('('+this.jsonString+')');
			var awkt = json.vertex.split(',');			
			json.vertex = SSEUtil.MC2LL({"x":awkt[0],"y":awkt[1]});
			return json;
		}else{
			return false;
		}
	};
	
	/**
	 * get webplan result
	 * @return {dis,cost,minx,miny,maxx,maxy,guids([{name,state,turn,len,cost,vertexes([SSEPoint])}])}
	 */
	this.returnByWebPlan = function(){
		if(+this.resultCode==1){
			//var json = JSON.parse(this.jsonString);
			var json = eval('('+this.jsonString+')');
			var cx = (json.minx+json.maxx)/2;
			var cy = (json.miny+json.maxy)/2;
			for(var i=0;i<json.guids.length;i++){
				var varr = json.guids[i].vertexes.split(';');
				for(var j=0;j<varr.length;j++){
					var xy = varr[j].split(",");
					var x = Radix.x2h(xy[0],36)+cx;
					var y = Radix.x2h(xy[1],36)+cy;					
					varr[j] = SSEUtil.MC2LL({"x":x,"y":y});
				}
				json.guids[i].vertexes = varr;
			}
			var min = SSEUtil.MC2LL({"x":json.minx,"y":json.miny});
			var max = SSEUtil.MC2LL({"x":json.maxx,"y":json.maxy});
			json.minx = min.lon;
			json.miny = min.lat;
			json.maxx = max.lon;
			json.maxy = max.lat;
			return json;
		}else{
			return false;
		}
	}
}

/***************************************************************************/

var SSELocating = {
	"geocoding":function(key,address,func){
		if(key && address){
			var xml = UrlStart + '<ws:geocoding>';
			xml +='<arg0><key>'+key+'</key><address>'+address+'</address></arg0>';
			xml += '</ws:geocoding>' + UrlEnd;
			xml = 'xml=' + xml;
			SSEUtil.requestOnready(xml,LocatingUrl,func);
		}else{
			alert("input null!");
		}
	},
	"reverseGeocoding":function(pt,func){
		if(pt){
			var xml = UrlStart + '<ws:reverseGeocoding>';
			xml += '<arg0><x>'+pt.lon+'</x><y>'+pt.lat+'</y></arg0>';
			xml += '</ws:reverseGeocoding>' + UrlEnd;
			xml = 'xml=' + xml;
			SSEUtil.requestOnready(xml,LocatingUrl,func);
		}else{
			alert("input null!");
		}
	}
}

var SSESearching = {
	"search":function(filter,func){
		if(filter && filter.key && filter.preference){
			var xml = UrlStart + '<ws:search><arg0>';
			xml += '<key>'+filter.key+'</key><preference>'+filter.preference+'</preference>';			
			if(filter.keyword)xml+='<keyword>'+filter.keyword+'</keyword>';
			if(filter.count)xml+='<count>'+filter.count+'</count>';
			if(filter.distance)xml+='<distance>'+filter.distance+'</distance>';
			if(filter.geometryWKT)xml+='<geometryWKT>'+filter.geometryWKT+'</geometryWKT>';
			xml += '</arg0></ws:search>' + UrlEnd;
			xml = 'xml=' + xml;
			SSEUtil.requestOnready(xml,SearchingUrl,func);
		}else{
			alert("input null!");
		}
	},
	"poiInfo":function(key,id,func){
		if(id && key){
			var xml = UrlStart + '<ws:poiInfo><arg0>';
			xml += '<key>'+key+'</key><id>'+id+'</id>';
			xml += '</arg0></ws:poiInfo>' + UrlEnd;
			xml = 'xml=' + xml;
			SSEUtil.requestOnready(xml,SearchingUrl,func);
		}else{
			alert("input null!");
		}
	}
}

var SSERouting = {
	"webPlan":function(router,func){
		if(router && router.preference && router.startPoint && router.endPoint){
			var xml = UrlStart + '<ws:webPlan><arg0>';
			xml += '<preference>'+router.preference+'</preference>';
			xml += '<startPoint><x>'+router.startPoint.lon+'</x><y>'+router.startPoint.lat+'</y></startPoint>';
			xml += '<endPoint><x>'+router.endPoint.lon+'</x><y>'+router.endPoint.lat+'</y></endPoint>';
			if(router.key)xml+='<key>'+router.key+'</key>';
			if(router.viaPoints){
				for(var i=0;i<router.viaPoints.length;i++){
					var p=router.viaPoints[i];
					if(p)xml+='<viaPoints><x>'+p.lon+'</x><y>'+p.lat+'</y></viaPoints>';
				}
			}
			xml += '</arg0></ws:webPlan>' + UrlEnd;
			xml = 'xml=' + xml;
			SSEUtil.requestOnready(xml,RoutingUrl,func);
		}else{
			alert("input null!");
		}
	}
}

/***************************************************************************/