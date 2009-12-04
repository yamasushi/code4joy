// Peter de Jong Strange Attractor
//   http://demonstrations.wolfram.com/PeterDeJongAttractors/
//   http://local.wasp.uwa.edu.au/~pbourke/fractals/peterdejong/
//   http://www.complexification.net/gallery/machines/peterdejong/
// 13:00 2009/11/03

import Math.{sin,cos,abs,sqrt,log,exp,pow}

class DSPeterDeJong(
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
	override val chaosName = "pdjchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+"," +
							c.formatted("%7.5f")+"," +
							d.formatted("%7.5f")+")"
						
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:Vector[Double]) : Vector[Double] = {
			val (x,y) = ( p.x , p.y )
			(	sin(a*Math.Pi*y) - cos(b*Math.Pi*x) , 
				sin(c*Math.Pi*x) - cos(d*Math.Pi*y))
		}
		override def mapCoordinate(p:Vector[Double]) : Vector[Double] = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(a)+abs(b) < 1e-7 ) return false
			if( abs(c)+abs(d) < 1e-7 ) return false
			true
		}
	}
}

