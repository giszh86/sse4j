package com.sse.base
{
	import com.adobe.json.JSON;
	
	public class WSResult
	{
		private var _resultCode:Number;
		private var _faultString:String;
		private var _jsonString:String;
		private var _json:Object;
		
		public function WSResult(result:String)
		{
			var list:XMLList = new XML(result).descendants("return");			
//			_resultCode = list.child("resultCode");
//			_faultString = list.child("faultString");
//			_jsonString = list.child("jsonString");
			_resultCode = list[0].resultCode;
			_faultString = list[0].faultString;
			_jsonString = list[0].jsonString;		
			trace(_jsonString);
			if(_resultCode==1)
				_json = JSON.decode(_jsonString);
		}
		
		public function get resultCode():Number
		{
			return _resultCode;
		}
		
		public function get faultString():String
		{
			return _faultString;
		}
		
		public function get json():Object
		{
			return _json;
		}
		
		public function get jsonString():String
		{
			return _jsonString;
		}
	}
}