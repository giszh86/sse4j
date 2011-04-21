package com.sse.util
{
	public class Radix
	{		
		public static function x2h(value:String, radix:int):Number {
			if (value==null || value=='')
				return 0;
			value = value.toUpperCase();			
			var n:Boolean = (value.charAt(0) == '-');
			if (n && value.length == 1)
				return 0;
			else if (n)
				value = value.substring(1);
			
			var result:Number = 0;
			for (var i:int = 0; i < value.length; i++) {
				result += Math.pow(radix, i) * charindex(value.charCodeAt(value.length - i - 1));
			}			
			
			if (n)
				return -result;
			else
				return result;
		}
		
		private static function charindex(value:Number):Number {
			var idx:Number = value;
			if (idx >= 48 && idx <= 57) {
				idx -= 48;
			} else if (idx >= 65 && idx <= 90) {
				idx -= 55;
			} else {
				idx = -1;
			}
			return idx;
		}
	}
}