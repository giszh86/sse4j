package com.ws.base
{
	
	public class WSFilterGeoc
	{
		private var _address : String;
		private var _key : String = "110000";
		
		public function WSFilterGeoc()
		{
		}
		
		public function get address() : String    
		{
			return _address;
		}    
		public function get key() : String    
		{
			return _key;
		}
		public function set address(value:String) : void 
		{
			_address = value;
		}
		public function set key(value:String) : void 
		{
			_key = value;
		}
				
	}
}