// Chaos Game

import Math.{sin,cos,abs,sqrt,log,exp,pow}

class DSChaosGame(
		val header:String ,
		val sys   :List[ChaosStreamCanvas] ,
		val numRing:Int ,
		val ovalR  :Double ) extends ChaosStreamCanvas
{
	val nsys = sys.size
	val rand = new java.util.Random
	//
	override val initialPoints = PointsOfRing(Vector(0,0),numRing,ovalR ).points
	override val chaosName = "cg_"+header
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:Vector[Double]) : Vector[Double] = {
			val isys = min( (rand.nextDouble() * nsys).asInstanceOf[Int] , nsys-1 )
			sys(isys).mapDifference(p)
		}
		override def mapCoordinate(p:Vector[Double]) : Vector[Double] = {
			p
		}
		override def validateParam : Boolean = {
			true
		}
	}
}


