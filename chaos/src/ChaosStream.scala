import Math.{log,abs,max,min} 

trait ChaosStream
{
	val chaosSystem:ChaosSystem
	val initialPoints:Stream[(Double,Double)]
	//
	def pointsFrom(dropIter:Int,maxIter:Int)( pt0:(Double,Double) ) : Stream[(Int,(Double,Double))] = {
		(Stream.from(-dropIter) zip chaosSystem.from(pt0) 
		) take( dropIter + maxIter ) 
	}
	def generatePoints(numTrajectory:Int)(dropIter:Int,maxIter:Int)(op:(Int,(Double,Double)) => Unit  ) : Unit = {
		initialPoints take(numTrajectory) foreach { pt0 =>
			pointsFrom(dropIter,maxIter)(pt0) foreach {ip =>op(ip._1,ip._2) }
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
				val xylog = log(abs(p._1)) + log(abs(p._2))
				if( xylog > 10 ) return true
			}
		}
		
		false
	}

	// calculate min/max
	def calcMinMax(dropIter:Int,maxIter:Int)(pt0:(Double,Double)) : ((Double,Double),(Double,Double)) = {
		val limit = 1e7
		( ((limit,limit),(-limit,-limit)) /: ((chaosSystem.from(pt0) drop dropIter) take maxIter)) {
			(acc,p) =>
				val (x,y) = p
				val (minXY,maxXY) = acc
				val (minx,miny) = minXY
				val (maxx,maxy) = maxXY
				(	(if(x<minx) x else minx , if(y<miny)y else miny) ,
					(if(x>maxx) x else maxx , if(y>maxy)y else maxy) )
		}
	}
	
	def calcMinMax(numTrajectory:Int)(dropIter:Int,maxIter:Int) : ((Double,Double),(Double,Double)) = {
		val limit = 1e7
		
		val mms = (initialPoints take numTrajectory) map { pt0 => calcMinMax(dropIter,maxIter)(pt0) }
		( ((limit,limit),(-limit,-limit)) /: mms ) {
			(acc,mm) =>
				val (accMinXY,accMaxXY) = acc
				val (accMinX ,accMinY ) = accMinXY
				val (accMaxX ,accMaxY ) = accMaxXY
				val (mmMinXY ,mmMaxXY ) = mm
				val (mmMinX  ,mmMinY  ) = mmMinXY
				val (mmMaxX  ,mmMaxY  ) = mmMaxXY
				
				(	(if(mmMinX < accMinX) mmMinX else accMinX , if(mmMinY < accMinY) mmMinY else accMinY ) ,
					(if(mmMaxX > accMaxX) mmMaxX else accMaxX , if(mmMaxY > accMaxY) mmMaxY else accMaxY ) )
		}	
	}
}
