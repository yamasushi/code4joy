// chaos system
// 2009-09-11
// 13:09 2009/10/29 Stream

trait ChaosSystem
{
	val limitPositive =  1.0e20
	val limitNegative = -limitPositive
	//
	def mapDifference(p:(Double,Double)) : (Double,Double) 
	def mapCoordinate(p:(Double,Double)) : (Double,Double) = {p}
	def validateParam() : Boolean = { true }
	//
	def from( pt0:(Double,Double) ) : Stream[(Double,Double)] = {
		lazy val sys:Stream[(Double,Double)] = Stream.cons( pt0 , sys.map(mapDifference) )
		sys takeWhile(limitCheck) map(mapCoordinate)
	}
	//
	def limitCheck(p:(Double,Double)) : Boolean = {
		val (x,y) = p
		(	limitNegative < x && limitPositive > x && 
			limitNegative < y && limitPositive > y )
	}
	
}
