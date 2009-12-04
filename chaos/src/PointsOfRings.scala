import Math.{sin,cos}

object PointsOfRing
{
	val rand  = new java.util.Random
	var iRing = 0
	def apply(	center:Vector[Double] , 
				numRing:Int , 
				ovalR:Double ) : Stream[Vector[Double]] = {
		def pt : Vector[Double] = {
			iRing += 1
			if(iRing>numRing) iRing = 1
			//
			//println("iRing = "+iRing)
			//
			val r     = (ovalR * iRing)/(numRing.asInstanceOf[Double])
			val theta = rand.nextDouble()*Math.Pi*2
			//
			center operate( Vector.trig map{ _.apply(theta) * r}  , _ + _)
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
