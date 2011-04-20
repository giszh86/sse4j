package com.ws.base
{
	public class WSFilter
	{
		private var _count : int = 50;
		private var _distance : int = 0;
		private var _geometryWKT : String;
		private var _key : String = "110000";
		private var _keyword : String;
		private var _preference : String = "POI";
		
		public function WSFilter()
		{
		}		
		
		public function get count() : int    
		{
			return _count;
		}   
		
		public function get distance() : int    
		{
			return _distance;
		}    
		
		public function get geometryWKT() : String    
		{
			return _geometryWKT;
		}    
		
		public function get key() : String    
		{
			return _key;
		}    
		
		public function get keyword() : String    
		{
			return _keyword;
		}    
		
		public function get preference() : String    
		{
			return _preference;
		}
		
		public function set count(value:int) : void 
		{ 
			_count = value;
		}
		
		public function set distance(value:int) : void 
		{ 
			_distance = value;
		}
		
		public function set geometryWKT(value:String) : void 
		{ 
			_geometryWKT = value;
		}
		
		public function set key(value:String) : void 
		{
			_key = value;
		}
		
		public function set keyword(value:String) : void 
		{
			_keyword = value;
		}
		
		public function set preference(value:String) : void 
		{
			_preference = value;
		}
		
	}
}