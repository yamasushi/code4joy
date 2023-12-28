// chaos system
// 2009-09-11
// 13:09 2009/10/29 Stream

trait ChaosSystem
{
	val limitPositive =  1.0e20
	val limitNegative = -limitPositive
	//
	def mapDifference(p:Vector[Double]) : Vector[Double] 
	def mapCoordinate(p:Vector[Double]) : Vector[Double] = {p}
	def validateParam() : Boolean = { true }
	//
	def from( pt0:Vector[Double] ) : Stream[Vector[Double]] = {
		lazy val sys:Stream[Vector[Double]] = Stream.cons( pt0 , sys.map(mapDifference) )
		sys takeWhile(limitCheck) map(mapCoordinate)
	}
	//
	def limitCheck(p:Vector[Double]) : Boolean = {
		(	limitNegative < p.x && limitPositive > p.x && 
			limitNegative < p.y && limitPositive > p.y )
	}
	
}
