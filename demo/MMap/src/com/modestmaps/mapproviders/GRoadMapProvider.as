package com.modestmaps.mapproviders
{
	import com.modestmaps.core.Coordinate;
	
	public class GRoadMapProvider extends AbstractMapProvider implements IMapProvider
	{
		private var url:String = ".google.cn/vt/lyrs=m@156999999&hl=zh-CN&gl=cn&";
		
		public function GRoadMapProvider(url:String=null, minZoom:int=MIN_ZOOM, maxZoom:int=MAX_ZOOM)
		{
			if(url != null)
				this.url = url;
			super(minZoom, maxZoom);			
		}
		
		public function toString() : String
		{
			return "GOOGLE_ROAD";
		}
		
		public function getTileUrls(coord:Coordinate):Array
		{
			return [getZoomString(sourceCoordinate(coord))];
		}
		
		protected function getZoomString(coord:Coordinate):String
		{
			trace("x=" + coord.row + "&y=" + coord.column + "&zoom=" + coord.zoom);
			return "http://mt"+(coord.row+coord.column)%4+url+"x=" + coord.column + "&y=" + coord.row + "&z=" + coord.zoom+"&t="+Math.random();
		}
		
	}
}