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
				(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
				(op:(Int,Vector[Int]) => Unit) : Unit = {
		//
		val canvasTransform=Geometry.transform(imgGeom,dataGeom)
		//
		startFrom(numTrajectory) { pt0 =>
			canvasPointsFrom(dropIter,maxIter)(pt0)(canvasTransform) foreach {ip=>
				if(imgGeom.frame.isInside(ip._2)) op(ip._1,ip._2) 
			}
		}
	}
}
