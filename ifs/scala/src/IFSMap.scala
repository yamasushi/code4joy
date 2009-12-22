// 2009-08-22 shuji yamamoto
package code4joy.scala

trait IFSMap{
	val numMaps:Int
	def getMap(index:Int) : ((Double,Double)) => (Double,Double)
}
