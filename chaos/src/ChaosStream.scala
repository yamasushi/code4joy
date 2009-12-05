import Math.{log,abs,max,min,sin,cos} 

trait ChaosStream
{
	val chaosSystem:ChaosSystem
	val chaosName  :String
	val initialPoints:Stream[Vector[Double]]
	val eps       = 1e-7
	//
	def pointsFrom(dropIter:Int,maxIter:Int)( pt0:Vector[Double] ) : Stream[(Int,Vector[Double])] = {
		(Stream.from(-dropIter) zip chaosSystem.from(pt0) 
		) take( dropIter + maxIter ) 
	}
	def generatePoints(numTrajectory:Int)(dropIter:Int,maxIter:Int)(op:(Int,Vector[Double]) => Unit  ) : Unit = {
		startFrom(numTrajectory){ pt0 =>
			pointsFrom(dropIter,maxIter)(pt0) foreach {ip =>op(ip._1,ip._2) }
		}
	}
	
	// initial points
	def startFrom(numTrajectory:Int)(op:Vector[Double]=>Unit){
		initialPoints take(numTrajectory) foreach { p0=>
			op( p0 )
			for( i <- 0 to 6 ){
				op( p0 operate( Vector.trig map {t=>(t(i*Math.Pi/3.0) )*eps} , { _ + _ }  ) )
			}
		}
	}
	
	// validation
	def isValid : Boolean = {
		chaosSystem.validateParam
	}
	
	// divergancy check
	def isDivergence(numTrajectory:Int)(numIter:Int) : Boolean = 
	{
		initialPoints take(numTrajectory) foreach { pt0 =>
			(chaosSystem.from(pt0) take numIter) foreach { p =>
				val xylog = log(abs(p.x)) + log(abs(p.y))
				if( xylog > 10 ) return true
			}
		}
		
		false
	}

	// calculate min/max
	def calcMinMax(dropIter:Int,maxIter:Int)(pt0:Vector[Double]) : Frame[Double] = {
		val limit:Double = 1e7
		val f = Frame[Double](Vector(limit,limit),Vector(-limit,-limit))
		( f /: ((chaosSystem.from(pt0) drop dropIter) take maxIter)) {
			(acc,p) => acc.inflate(p)
		}
	}
	
	def calcMinMax(numTrajectory:Int)(dropIter:Int,maxIter:Int) : Frame[Double] = {
		val limit:Double = 1e7
		val mms = (initialPoints take numTrajectory) map { pt0 => calcMinMax(dropIter,maxIter)(pt0) }
		//
		val f = Frame[Double](Vector(limit,limit),Vector(-limit,-limit))
		( f /: mms ) {
			(acc,mm) => acc.inflate(mm)
		}
	}
}
