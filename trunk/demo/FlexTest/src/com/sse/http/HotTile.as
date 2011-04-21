package com.sse.http
{
	import mx.controls.Alert;
	import mx.rpc.events.ResultEvent;
	
	public class HotTile extends HttpBase
	{
		public function HotTile()
		{
			super("http://localhost:8080/sse4j.http/servlet/HotTile","HotTile",["x","y","zoom","type","keyword"]);
		}
		
		public function js(x:Number, y:Number, zoom:Number, keyword:String):void
		{
			this.https.getOperation("HotTile").send(x,y,zoom,"js",keyword);
		}
		
		protected override function OnResult(event:ResultEvent):void
		{			
			Alert.show(event.message.body.toString());
		}
	}
}