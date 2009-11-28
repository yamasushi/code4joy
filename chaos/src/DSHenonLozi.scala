// Henon/Lozi Strange Attractor
//   http://mathworld.wolfram.com/LoziMap.html
//   http://mathforum.org/advanced/robertd/lozi.html.
//  
// modified Lozi -->
//   http://www.emis.de/journals/HOA/DDNS/c754.pdf 
// 
// Peitgen, H.-O.; Jürgens, H.; and Saupe, D. 
// §12.1 in Chaos and Fractals: New Frontiers of Science. 
// New York: Springer-Verlag, p. 672, 1992.
//
// 9:00 2009/11/03

import Math.{sin,cos,abs,sqrt,log,exp,pow}

class DSHenonLozi(
		val header:String           , 
		//
		phi    :(Double,Double)=>Double,	//phi(x,y) = x^2 --> Henon
											//phi(x,y) = |x| --> Lozi
											//phi(x,y) = y^2 - |x| --> modified Lozi
		param  :(Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (alpha,beta) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "hlchaos_"+header+"_" + 
						"("+alpha.formatted("%7.5f")+"," +
							beta .formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			( 1 + y - alpha*phi(x,y) , beta*x)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(alpha)<1e-7 ) return false
			if( abs(beta )<1e-7 ) return false
			true
		}
	}
}

