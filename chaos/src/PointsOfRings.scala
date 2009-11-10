import Math.{sin,cos}

case class PointsOfRing( val center:(Double,Double) , val numRing:Int , val ovalR:Double )
{
	val rand  = new java.util.Random
	var iRing = 0
	def points : Stream[(Double,Double)] = {
		def pt : (Double,Double) = {
			val (cx,cy) = center
			iRing += 1
			if(iRing>numRing) iRing = 1
			//
			//println("iRing = "+iRing)
			//
			val r     = (ovalR * iRing)/(numRing.asInstanceOf[Double])
			val theta = rand.nextDouble()*Math.Pi*2
			//
			(	cx + r*cos(theta) , 
				cy + r*sin(theta) )
		}
		def pts :Stream[(Double,Double)] = Stream.cons( pt , pts )
		//
		pts
	}
}
