package com.ws
{
	import com.modestmaps.TweenMap;
	import com.ws.base.WSResult;
	
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.soap.WebService;
	
	public class WSBase
	{
		protected var ws:WebService;
		protected var map:TweenMap;	
		
		public function WSBase(wsdl:String,map:TweenMap)
		{
			this.map = map;			
			ws = new WebService();			
			ws.wsdl=wsdl;
			ws.loadWSDL();
		}
		
		protected function OnFault(event:FaultEvent):void
		{
			Alert.show("Fail:"+event.fault.faultDetail);
		}
		
		protected function OnResult(event:ResultEvent):void
		{
			var result:WSResult = new WSResult(event.message.body.toString());			
			if(result.resultCode==1){
				Alert.show("Result:"+result.jsonString);
			}else{
				Alert.show("Fail:"+result.faultString);
			}
		}
	}
}