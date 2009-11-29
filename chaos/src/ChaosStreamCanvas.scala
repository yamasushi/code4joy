trait ChaosStreamCanvas extends ChaosStream
{
	def canvasPointsFrom
				(dropIter:Int,maxIter:Int)
				(minmax:((Double,Double),(Double,Double)))
				(canvas:Picture[_] with Canvas)
				(pt0:Vector[Double] ) : Stream[(Int,(Int,Int))] = {
		//
		(Stream.from(-dropIter) zip (chaosSystem.from(pt0) map canvas.transform( minmax ) )  
		) take ( dropIter + maxIter ) 
	}
	//
	def generateCanvasPoints
				(numTrajectory:Int)
				(dropIter:Int,maxIter:Int)
				(canvas:Picture[_] with Canvas)
				(minmax:((Double,Double),(Double,Double)))
				(op:(Int,(Int,Int)) => Unit) : Unit = {
		//
		initialPoints take(numTrajectory) foreach { pt0 =>
			canvasPointsFrom(dropIter,maxIter)(minmax)(canvas)(pt0) foreach {ip=>
				if(canvas.isPointVisible(ip._2)) op(ip._1,ip._2) 
			}
		}
	}
}
