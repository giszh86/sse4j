package com.sse.http
{
	import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPMultiService;
	import mx.rpc.http.Operation;
	
	public class HttpBase
	{
		protected var https:HTTPMultiService;
		
		public function HttpBase(url:String, name:String, args:Array, method:String="GET")
		{
			https = new HTTPMultiService();		
			var oper:Operation = new Operation(null,name);
			oper.method = method;
			oper.url = url;	
			oper.argumentNames = args;
			https.operationList = new Array(oper);
			
			oper.addEventListener(FaultEvent.FAULT,OnFault);
			oper.addEventListener(ResultEvent.RESULT,OnResult);
		}
		
		protected function OnFault(event:FaultEvent):void
		{
			Alert.show("Fail:"+event.fault.faultDetail);
		}
		
		protected function OnResult(event:ResultEvent):void
		{
			throw new Error("abstract function");
		}
	}
}