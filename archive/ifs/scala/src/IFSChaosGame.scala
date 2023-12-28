// IFS image generation
// 2009-08-22 shuji yamamoto
package code4joy.scala

import scala.actors.Actor._

case class IFSChaosGame(ifs:IFSMap){
	//
	def mapPoint(r:Double          , // random number (0...1)
	             pt:(Double,Double)  // point
				) : (Double,Double) = {
		val index = (r*ifs.numMaps).asInstanceOf[Int]
		ifs.getMap(index)(pt)
	}
	
	def generatePoint(maxIter:Int // number of iteration)
				) : (Double,Double) = {
		var pt=(0.,0.)
		val rand  = new java.util.Random
		for(i <-(0 until maxIter) ){
			//println(scale)
			pt = this.mapPoint( rand.nextDouble , pt )
		}
		pt // result
	}
		
	def generate(maxPt:Int   , // number of points 
	             maxIter:Int   // number of iteration for convergence of start-point
				 )( op:((Double,Double)) => Unit ) = {
		val rand  = new java.util.Random
		
		// converge first
		var pt =generatePoint( maxIter )
		
		// plot it
		op( pt )
		//
		for(i <- 1 until maxPt){
			pt = this.mapPoint( rand.nextDouble , pt )
			//println(result)
			
			// plot it
			op(pt)
			//
		}
		
	}
	//
}
