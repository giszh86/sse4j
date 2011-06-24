package com.modestmaps.mapproviders
{
	import com.modestmaps.core.Coordinate;
	
	public class XYZMapProvider extends AbstractMapProvider implements IMapProvider
	{
		private var url:String = "http://localhost:8080/TileServer/tile/roadTile?";
		
		public function XYZMapProvider(url:String=null, minZoom:int=MIN_ZOOM, maxZoom:int=MAX_ZOOM)
		{
			if(url != null)
				this.url = url;
			super(minZoom, maxZoom);			
		}
		
		public function toString() : String
		{
			return "XYZ_MAP";
		}
		
		public function getTileUrls(coord:Coordinate):Array
		{
			return [ url+getZoomString(sourceCoordinate(coord)) ];
		}
		
		protected function getZoomString(coord:Coordinate):String
		{
			trace("x=" + coord.row + "&y=" + coord.column + "&zoom=" + coord.zoom);
			return "x=" + coord.row + "&y=" + coord.column + "&zoom=" + coord.zoom+"&t="+Math.random();
		}
		
	}
}