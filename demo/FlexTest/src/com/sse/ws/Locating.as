package com.sse.ws
{	
	import com.modestmaps.TweenMap;
	import com.modestmaps.geo.Location;
	import com.modestmaps.geo.MercatorProjection;
	import com.mmap.tip.PopMarker;
	import com.sse.base.*;
	
	import flash.geom.Point;
	
	import mx.controls.Alert;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	public class Locating extends WSBase
	{	
		private var operG:AbstractOperation;
		private var operRG:AbstractOperation;
		
		public function Locating(map:TweenMap)
		{
			super("http://localhost:8080/sse4j/LocatingPort?wsdl",map);
			
			operG = this.ws.getOperation("geocoding");
			operG.addEventListener(FaultEvent.FAULT,OnFault);
			operG.addEventListener(ResultEvent.RESULT,OnGResult);
			
			operRG = this.ws.getOperation("reverseGeocoding");
			operRG.addEventListener(FaultEvent.FAULT,OnFault);
			operRG.addEventListener(ResultEvent.RESULT,OnResult);
		}
		
		public function geocoding(arg0:WSFilterGeoc):void
		{
			operG.send(arg0);
		}
		
		public function reverseGeocoding(arg0:WSPointF):void
		{
			operRG.send(arg0);
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