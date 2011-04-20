package com.ws.base
{
	public class WSRouter
	{
		private var _endPoint : WSPointF;
		private var _key : String = "110000";
		private var _preference : String = "Fastest";
		private var _startPoint : WSPointF;
		private var _viaPoints : Array;
		
		public function WSRouter()
		{
		}
		
		public function get endPoint() : WSPointF    
		{
			return _endPoint;
		}    
		
		public function get key() : String    
		{
			return _key;
		}    
		
		public function get preference() : String    
		{
			return _preference;
		}    
		
		public function get startPoint() : WSPointF    
		{
			return _startPoint;
		}    
		
		public function get viaPoints() : Array    
		{
			return _viaPoints;
		}
		
		public function set endPoint(value:WSPointF) : void 
		{
			_endPoint = value;
		}
		
		public function set key(value:String) : void 
		{ 
			_key = value;
		}
		
		public function set preference(value:String) : void 
		{ 
			_preference = value;
		}
		
		public function set startPoint(value:WSPointF) : void 
		{
			_startPoint = value;
		}
		
		public function set viaPoints(value:Array) : void
		{
			_viaPoints = value;
		}
	}
}