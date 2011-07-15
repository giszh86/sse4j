var LocatingWSDL = 'http://localhost:8080/sse4j/LocatingPort?wsdl';
var SearchingWSDL = 'http://localhost:8080/sse4j/SearchingPort?wsdl';
var RoutingWSDL = 'http://localhost:8080/sse4j/RoutingPort?wsdl';
var MatchingWSDL = 'http://localhost:8080/sse4j/MatchingPort?wsdl';
var SoapStart = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.sse.org/"><soapenv:Header/><soapenv:Body>';
var SoapEnd = '</soapenv:Body></soapenv:Envelope>';

/***************************************************************************/

function createHttpRequest(){
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
function requestOnready(xml, wsdl, func){
	var request = createHttpRequest();
	if(request){
		try{
			request.open("post", wsdl, true);
		}catch(e1){
			alert(e1);
			return;
		}
		request.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
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

/***************************************************************************/

function SSEPoint(lon,lat){
	this.lon = lon;
	this.lat = lat;
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
	this.returnByGeocoding = function(){
		if(+this.resultCode==1){
		}else{
			return false;
		}
	};
	this.returnByReGeocoding = function(){
		if(+this.resultCode==1){
		}else{
			return false;
		}
	};
	this.returnBySearch = function(){
		if(+this.resultCode==1){
		}else{
			return false;
		}
	};
	this.returnByPoiInfo = function(){
		if(+this.resultCode==1){
		}else{
			return false;
		}
	};
	this.returnByWebPlan = function(){
		if(+this.resultCode==1){
		}else{
			return false;
		}
	}
}

/***************************************************************************/

var SSELocating = {
	"geocoding":function(key,address,func){
		if(key && address){
			var xml = SoapStart + '<ws:geocoding>';
			xml +='<arg0><key>'+key+'</key><address>'+address+'</address></arg0>';
			xml += '</ws:geocoding>' + SoapEnd;
			requestOnready(xml,LocatingWSDL,func);
		}else{
			alert("input null!");
		}
	},
	"reverseGeocoding":function(pt,func){
		if(pt){
			var xml = SoapStart + '<ws:reverseGeocoding>';
			xml += '<arg0><x>'+pt.lon+'</x><y>'+pt.lat+'</y></arg0>';
			xml += '</ws:reverseGeocoding>' + SoapEnd;
			requestOnready(xml,LocatingWSDL,func);
		}else{
			alert("input null!");
		}
	}
}

var SSESearching = {
	"search":function(filter,func){
		if(filter && filter.key && filter.preference){
			var xml = SoapStart + '<ws:search><arg0>';
			xml += '<key>'+filter.key+'</key><preference>'+filter.preference+'</preference>';			
			if(filter.keyword)xml+='<keyword>'+filter.keyword+'</keyword>';
			if(filter.count)xml+='<count>'+filter.count+'</count>';
			if(filter.distance)xml+='<distance>'+filter.distance+'</distance>';
			if(filter.geometryWKT)xml+='<geometryWKT>'+filter.geometryWKT+'</geometryWKT>';
			xml += '</arg0></ws:search>' + SoapEnd;
			requestOnready(xml,SearchingWSDL,func);
		}else{
			alert("input null!");
		}
	},
	"poiInfo":function(key,id,func){
		if(id && key){
			var xml = SoapStart + '<ws:poiInfo><arg0>';
			xml += '<key>'+key+'</key><id>'+id+'</id>';
			xml += '</arg0></ws:poiInfo>' + SoapEnd;
			requestOnready(xml,SearchingWSDL,func);
		}else{
			alert("input null!");
		}
	}
}

var SSERouting = {
	"webPlan":function(router,func){
		if(router && router.preference && router.startPoint && router.endPoint){
			var xml = SoapStart + '<ws:webPlan><arg0>';
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
			xml += '</arg0></ws:webPlan>' + SoapEnd;
			requestOnready(xml,RoutingWSDL,func);
		}else{
			alert("input null!");
		}
	}
}
