var LocatingService = "http://localhost:8080/sse4j/LocatingPort?wsdl";

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
function requestOnready(xml, url, func){
	var request = createHttpRequest();
	if(request){
		request.open("post", url, true);
		request.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
		request.send(xml);
		request.onreadystatechange = function(){
			if (request.readyState==4 && request.status==200){
				var xmlDom = request.responseXML.documentElement;
				var result = new SSEResult;
				if (xmlDom!=null && xmlDom!=undefined){
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

function SSEPoint(lat,lon){
	this.lat = lat;
	this.lon = lon;
}
function SSEFilterGeoc(key,address){
	this.key = key;
	this.address = address;
}
function SSEResult(faultString,resultCode,jsonString){
	this.faultString = faultString;
	this.resultCode = resultCode;
	this.jsonString = jsonString;
}

/***************************************************************************/

var SSELocating = {
	"soapStart":'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.sse.org/"><soapenv:Header/><soapenv:Body>',
	"soapEnd":'</soapenv:Body></soapenv:Envelope>',
	"geocoding":function(geoc,func){
		if(geoc!=null){
			var xml = this.soapStart + '<ws:geocoding>';
			xml +='<arg0><address>'+geoc.address+'</address><key>'+geoc.key+'</key></arg0>';
			xml += '</ws:geocoding>' + this.soapEnd;
			requestOnready(xml,LocatingService,func);
		}else{
			alert("input null!");
		}
	},
	"reverseGeocoding":function(pt,func){
		if(pt!=null){
			var xml = this.soapStart + '<ws:reverseGeocoding>';
			xml += '<arg0><x>'+pt.lon+'</x><y>'+pt.lat+'</y></arg0>';
			xml += '</ws:reverseGeocoding>' + this.soapEnd;
			requestOnready(xml,LocatingService,func);
		}else{
			alert("input null!");
		}
	}
}
