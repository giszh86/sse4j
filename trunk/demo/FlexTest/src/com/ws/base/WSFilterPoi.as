package com.ws.base
{
	public class WSFilterPoi
	{
		private var _id : String;
		private var _key : String = "110000";
		
		public function WSFilterPoi()
		{
		}
		
		public function get id() : String    
		{
			return _id;
		}    
		
		public function get key() : String    
		{
			return _key;
		}
		
		public function set id(value:String) : void 
		{ 
			_id = value;
		}
		
		public function set key(value:String) : void 
		{
			_key = value;
		}
		
	}
}