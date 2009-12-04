// Sine Strange Attractor

import Math.{sin,cos,abs,sqrt,log,exp,pow}

class DSSine(
		val header:String           , 
		param  :(Double,Double) ,
		//
		val numRing:Int   ,
		val ovalR  :Double) extends ChaosStream
{
	val (a,b) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR )
	override val chaosName = "sinchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+")"
						
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:Vector[Double]) : Vector[Double] = {
			val (x,y) = (p.x,p.y)
			(	y , 
				a*sin(x) + b*y )
		}
		override def mapCoordinate(p:Vector[Double]) : Vector[Double] = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(a)+abs(b) < 1e-7 ) return false
			true
		}
	}
}

