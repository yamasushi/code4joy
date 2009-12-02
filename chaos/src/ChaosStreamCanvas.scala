trait ChaosStreamCanvas
{
	this : ChaosStream=>
	//
	def canvasPointsFrom
				(dropIter:Int,maxIter:Int)
				(pt0:Vector[Double])
				(canvasTransform:Vector[Double]=>Vector[Int])
				 : Stream[(Int,Vector[Int])] = {
		//
		(Stream.from(-dropIter) zip (chaosSystem.from(pt0) map canvasTransform )  
		) take ( dropIter + maxIter ) 
	}
	//
	def generateCanvasPoints
				(numTrajectory:Int)
				(dropIter:Int,maxIter:Int)
				(canvas:Canvas,geom:Geometry[Double])
				(op:(Int,Vector[Int]) => Unit) : Unit = {
		//
		startFrom(numTrajectory) { pt0 =>
			canvasPointsFrom(dropIter,maxIter)(pt0)(canvas.transform(geom)) foreach {ip=>
				if(canvas.isPointVisible(ip._2)) op(ip._1,ip._2) 
			}
		}
	}
}
