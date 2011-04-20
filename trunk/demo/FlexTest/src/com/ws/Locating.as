package com.ws
{	
	import com.modestmaps.TweenMap;
	import com.modestmaps.geo.Location;
	import com.modestmaps.geo.MercatorProjection;
	import com.ws.tip.PopMarker;
	import com.ws.base.*;
	
	import flash.geom.Point;
	
	import mx.controls.Alert;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	public class Locating extends WSBase
	{		
		public function Locating(map:TweenMap)
		{
			super("http://localhost:8080/sse4j/LocatingPort?wsdl",map);
		}
		
		public function geocoding(arg0:WSFilterGeoc):void
		{			
			var oper:AbstractOperation = ws.getOperation("geocoding");
			oper.addEventListener(FaultEvent.FAULT,OnFault);
			oper.addEventListener(ResultEvent.RESULT,OnGResult);
			oper.send(arg0);
		}
		
		public function reverseGeocoding(arg0:WSPointF):void
		{			
			var oper:AbstractOperation = ws.getOperation("reverseGeocoding");
			oper.addEventListener(FaultEvent.FAULT,OnFault);
			oper.addEventListener(ResultEvent.RESULT,OnResult);
			oper.send(arg0);
		}
		
		private function OnGResult(event:ResultEvent):void
		{	
			var result:WSResult = new WSResult(event.message.body.toString());			
			if(result.resultCode==1){
				this.map.removeAllMarkers();
				var loc:Location = MercatorProjection.MC2LL(new Point(result.json.x,result.json.y));
				this.map.putMarker(loc,new PopMarker());
				this.map.setCenterZoom(loc,14);
			}else{
				Alert.show("Fail:"+result.faultString);
			}			
		}
		
	}
}