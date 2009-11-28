// Gingerbread Man Strange Attractor
//   http://en.wikipedia.org/wiki/Gingerbreadman_map
//   http://mathworld.wolfram.com/GingerbreadmanMap.html
//   http://sprott.physics.wisc.edu/FRACTALS/booktext/sabook.pdf
//
//  Devaney, R. L.
//  "A Piecewise Linear Model for the Zones of Instability of an Area Preserving Map."
//  Physica D 10, 387-393, 1984.
//
//  Devaney, R. L. 
//  "The Gingerbreadman."
//  Algorithm 3, 15-16, Jan. 1992.
//
//  Dr. Mu.
//  "Cowculations: Gingerbread Man."
//  Quantum, pp. 55-57, January/February 1998.
//
//  Peitgen, H.-O. and Saupe, D. (Eds.). 
//  "A Chaotic Gingerbreadman." §3.2.3 in The Science of Fractal Images. 
//  New York: Springer-Verlag, pp. 149-150, 1988.

import Math.{abs,min,max,log,sqrt,pow}

class DSGingerbreadMan(
		val header:String           , 
		//
		phi    :(Double)=>Double,
		param  :(Double,Double) ,
		val map:((Double,Double))=>(Double,Double) ,
		//
		val numRing:Int   ,
		val ovalR :Double) extends ChaosStreamCanvas
{
	val (mu,nu) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "gbmchaos_"+header+"_" + 
						"("+mu.formatted("%7.5f")+"," +
							nu.formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			(- nu*y + 1 + mu*phi(x) , x)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			map(p)
		}
		override def validateParam : Boolean = {
			if( abs(mu)<1e-7 ) return false
			true
		}
	}
}

