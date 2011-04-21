package com.sse.http
{
	import com.sse.base.WSFilter;
	
	import mx.controls.Alert;
	import mx.rpc.events.ResultEvent;

	public class Searching extends HttpBase
	{
		public function Searching()
		{
			super("http://localhost:8080/sse4j.http/servlet/Searching","Searching",["xml"],"POST");
		}
		
		public function search(filter:WSFilter):void
		{
			var xml:String = "<ws:poiInfo><arg0><id>200</id><key>110000</key></arg0></ws:poiInfo>";
			this.https.getOperation("Searching").send(xml);
		}
		
		protected override function OnResult(event:ResultEvent):void
		{			
			Alert.show(event.result);
		}
		
	}
}