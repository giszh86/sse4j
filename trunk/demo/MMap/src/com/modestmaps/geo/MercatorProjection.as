/*
 * $Id: MercatorProjection.as 647 2008-08-25 23:38:15Z tom $
 */

package com.modestmaps.geo
{
	import com.modestmaps.geo.AbstractProjection;
	import com.modestmaps.geo.Transformation;
	
	import flash.geom.Point;
	
	import flashx.textLayout.formats.Float;
	 
	public class MercatorProjection extends AbstractProjection
	{
	    public function MercatorProjection(zoom:Number, T:Transformation)
	    {
	        super(zoom, T);
	    }
	    
	   /*
	    * String signature of the current projection.
	    */
	    override public function toString():String
	    {
	        return 'Mercator('+zoom+', '+T.toString()+')';
	    }
	    
	   /*
	    * Return raw projected point.
	    * See: http://mathworld.wolfram.com/MercatorProjection.html (2)
	    */
	    override protected function rawProject(point:Point):Point
	    {
	        return new Point(point.x, 
	                         Math.log(Math.tan(0.25 * Math.PI + 0.5 * point.y)));
	    }
	    
	   /*
	    * Return raw unprojected point.
	    * See: http://mathworld.wolfram.com/MercatorProjection.html (7)
	    */
	    override protected function rawUnproject(point:Point):Point
	    {
	        return new Point(point.x,
	                         2 * Math.atan(Math.pow(Math.E, point.y)) - 0.5 * Math.PI);
	    }

		/**
		 * LatLon To Mercator
		 */
		public static function LL2MC(location:Location):Point
		{
			var a:Number = Math.log(Math.tan((90+location.lat)*Math.PI/360))/(Math.PI/180);
			return new Point((a*20037508.342789/180),(location.lon*20037508.342789/180));
		}
		
		/**
		 *  Mercator To LatLon
		 */
		public static function MC2LL(point:Point):Location
		{
			var lat:Number = (point.y/20037508.342789)*180;
			var lon:Number = (point.x/20037508.342789)*180;
			lat = 180/Math.PI*(2*Math.atan(Math.exp(lat*Math.PI/180))-Math.PI/2);
			return new Location(lat,lon);
		}
	}
}