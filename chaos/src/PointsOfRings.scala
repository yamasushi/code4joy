import Math.{sin,cos}

object PointsOfRing
{
	val rand  = new java.util.Random
	var iRing = 0
	def apply(	center:Vector[Double] , 
				numRing:Int , 
				ovalR:Double ) : Stream[Vector[Double]] = {
		def pt : Vector[Double] = {
			val (cx,cy) = (center.x,center.y)
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
		def pts :Stream[Vector[Double]] = Stream.cons( pt , pts )
		//
		pts
	}
	//
	def apply(center:Vector[Double],ovalR:Double) : Stream[Vector[Double]]={
		apply(center,1,ovalR)
	}
	//
	def apply(center:Vector[Double]) : Stream[Vector[Double]]={
		apply(center,1,1)
	}
}
