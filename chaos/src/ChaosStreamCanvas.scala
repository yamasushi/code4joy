trait ChaosStreamCanvas extends ChaosStream
{
	def canvasPointsFrom
				(dropIter:Int,maxIter:Int)
				(minmax:Frame[Double])
				(canvas:Canvas)
				(pt0:Vector[Double] ) : Stream[(Int,Vector[Int])] = {
		//
		(Stream.from(-dropIter) zip (chaosSystem.from(pt0) map canvas.transform( minmax ) )  
		) take ( dropIter + maxIter ) 
	}
	//
	def generateCanvasPoints
				(numTrajectory:Int)
				(dropIter:Int,maxIter:Int)
				(canvas:Canvas)
				(minmax:Frame[Double])
				(op:(Int,Vector[Int]) => Unit) : Unit = {
		//
		initialPoints take(numTrajectory) foreach { pt0 =>
			canvasPointsFrom(dropIter,maxIter)(minmax)(canvas)(pt0) foreach {ip=>
				if(canvas.isPointVisible(ip._2)) op(ip._1,ip._2) 
			}
		}
	}
}
