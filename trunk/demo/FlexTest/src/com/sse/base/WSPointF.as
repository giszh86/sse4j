package com.sse.base
{
	public class WSPointF
	{
		private var _x:Number;
		private var _y:Number;
		
		public function WSPointF()
		{
		}		
		
		public function get x() : Number    
		{
			return _x;
		}
		
		public function get y() : Number    
		{
			return _y;
		}
		
		public function set x(value:Number) : void 
		{ 
			_x = value; 
		}
		
		public function set y(value:Number) : void 
		{
			_y = value;			
		}
	}
}