SSE4J(Spatial Search Engine for Java)是针对地理信息数据源构建的垂直搜索引擎应用接口，是基于Lucene+JTS Topology Suite开源库设计的框架。

规划的SSE4J包含：
  1）SSE4J应用开发包
  2）SSE4J Webservice应用接口
  3）SSE4J工具集

规划的SSE4J包含的功能：
  1) POI搜索（名称、地址、类型等关键字搜索；周边搜索）
  2）道路搜索、区域搜索
  3）地址匹配、反地址匹配、道路匹配
  4）深度信息关联（网页关联）
  5）路径规划（支持途径点、回避路障）
  6）公交换乘
SSE4J支持中文分词检索。SSE4J Webservice接口的POI搜索（周边2公里的关键字查询，返回100条结果的应用场景；TP-T410环境）具备300毫秒/100用户的并发处理能力；路径规划（起点到终点直线距离为150公里的应用场景；TP-T410环境）支持600毫秒/50用户的并发处理能力。

SSE4J Webservice接口与Openlayers（ModestMaps）的组合，可以轻松构建地图服务的Web应用；通过调用SSE4J Webservice接口，也可实现LBS的终端应用。

SSE4J依赖的开源库有：
  1）gson（http://code.google.com/p/google-gson/）  
  2）lucene（http://lucene.apache.org/）
  3）IKAnalyzer（http://code.google.com/p/ik-analyzer/）
  4）JTS（http://sourceforge.net/projects/jts-topo-suite/）
  