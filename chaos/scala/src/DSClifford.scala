// Clifford Strange Attractor
//   http://local.wasp.uwa.edu.au/~pbourke/fractals/clifford/
//   http://en.wikipedia.org/wiki/Clifford_A._Pickover
// 13:00 2009/11/03

import Math.{sin,cos,abs,sqrt,log,exp,pow}

class DSClifford(
		val header:String           , 
		//
		param  :(Double,Double,Double,Double) ,
		//
		val numRing:Int   ,
		val ovalR :Double) extends ChaosStream
{
	val (a,b,c,d) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR )
	override val chaosName = "cpchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+"," +
							c.formatted("%7.5f")+"," +
							d.formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:Vector[Double]) : Vector[Double] = {
			val (x,y) = (p.x,p.y)
			(	sin(a*y) + c*cos(a*x) , 
				sin(b*x) + d*cos(b*y))
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

