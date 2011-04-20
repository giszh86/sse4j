package com.ws
{	
	import com.modestmaps.TweenMap;
	import com.modestmaps.core.MapExtent;
	import com.modestmaps.events.MarkerEvent;
	import com.modestmaps.geo.Location;
	import com.modestmaps.geo.MercatorProjection;
	import com.modestmaps.overlays.Polyline;
	import com.ws.base.*;
	import com.ws.tip.PopMarker;
	import com.ws.tip.Tooltip;
	
	import flash.geom.Point;
	
	import mx.controls.Alert;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	public class Searching extends WSBase
	{	
		private static var tooltip:Tooltip = new Tooltip();
		private var filter:WSFilter;
		
		public function Searching(map:TweenMap)
		{
			super("http://localhost:8080/sse4j/SearchingPort?wsdl",map);			
			if(!this.map.contains(tooltip)){
				this.map.addChild(tooltip);
				this.map.addEventListener(MarkerEvent.MARKER_ROLL_OVER, onMarkerRollOver);
				this.map.addEventListener(MarkerEvent.MARKER_ROLL_OUT, onMarkerRollOut);
			}
		}
		
		private function onMarkerRollOver(event:MarkerEvent):void 
		{
			var marker:PopMarker = event.marker as PopMarker;
			var pt:Point = map.locationPoint(event.location, this.map.parent);
			tooltip.x = pt.x+10;
			tooltip.y = pt.y;
			tooltip.label = marker.title;
			tooltip.visible = true;
		}

		private function onMarkerRollOut(event:MarkerEvent):void 
		{
			tooltip.visible = false;
		}
		
		public function search(arg0:WSFilter):void
		{	
			filter = arg0;
			var oper:AbstractOperation = ws.getOperation("search");
			oper.addEventListener(FaultEvent.FAULT,OnFault);
			oper.addEventListener(ResultEvent.RESULT,OnSResult);
			oper.send(arg0);
		}
		
		public function poiInfo(arg0:WSFilterPoi):void
		{			
			var oper:AbstractOperation = ws.getOperation("poiInfo");
			oper.addEventListener(FaultEvent.FAULT,OnFault);
			oper.addEventListener(ResultEvent.RESULT,OnResult);
			oper.send(arg0);
		}
		
		private function OnSResult(event:ResultEvent):void
		{
			var result:WSResult = new WSResult(event.message.body.toString());			
			if(result.resultCode==1){
				this.map.removeAllMarkers();
				
				var tip:String="";
				var extent:MapExtent;
				var arr:Array = result.json as Array;				
				for(var i:int=0;i<arr.length;i++){
					var wkt:String = arr[i].wkt;
					wkt = wkt.substring(wkt.lastIndexOf("(")+1,wkt.lastIndexOf(")"));
					if(filter.preference=="POI"){
						var loc:Location = MercatorProjection.MC2LL(new Point(parseFloat(wkt.split(" ")[0]),parseFloat(wkt.split(" ")[1])));
						this.map.putMarker(loc,new PopMarker(arr[i].id+"_"+arr[i].title));	
						if(!extent)
							extent = new MapExtent(loc.lat,loc.lat,loc.lon,loc.lon);
						else
							extent.enclose(loc);			
					}else if(filter.preference=="NET"){
						tip += arr[i].id+"_"+arr[i].title+" \n";
						
						var locArr:Array = new Array();
						var varr:Array = wkt.split(", ");
						for(var j:int=0;j<varr.length;j++){
							var xy:Array = (varr[j] as String).split(" ");							
							trace(xy[0],xy[1]); 
							var locn:Location = MercatorProjection.MC2LL(new Point(xy[0],xy[1]));
							locArr.push(locn);
							if(!extent)
								extent = new MapExtent(locn.lat,locn.lat,locn.lon,locn.lon);
							else
								extent.enclose(locn);
						}
						var polyline:Polyline = new Polyline(new Date().toLocaleString(),locArr);				
						this.map.polylineClip.addPolyline(polyline);
					}		
				}
				if(extent)
					this.map.setExtent(extent);
				if(tip!="")
					Alert.show(tip);
			}else{
				Alert.show("Fail:"+result.faultString);
			}			
		}

	}
}