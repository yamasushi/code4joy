// Simplified Gumowski-Mira Strange Attractor
// 17:42 2009/10/14
// 
// see:
//   http://www.ny.airnet.ne.jp/satoh/chaos.htm
// see also:
//   http://www.scipress.org/journals/forma/pdf/1502/15020121.pdf
//   http://math.cmaisonneuve.qc.ca/alevesque/chaos_fract/Attracteurs/Attracteurs.html

import Math.{abs,min,max,log,sqrt,pow}

class DSSimplifiedGumowskiMira(
				val header       :String , 
				//
				val pi           :(Double)=>Double ,
				val psi          :(Double)=>Double ,
				val param        :(Double,Double,Double,Double)    ,
				val map          :(Vector[Double])=>Vector[Double] ,
				//
				val numRing:Int   ,
				val ovalR:Double) extends ChaosStream
{
	val (paramA,paramB,paramC,paramD) = param
	//
	override val initialPoints = PointsOfRing(Vector(0,0),numRing,ovalR).points
	override val chaosName     = "sgmchaos_" + header + "_" + 
						"("+paramA.formatted("%7.5f")+","+
							paramB.formatted("%7.5f")+","+
							paramC.formatted("%7.5f")+","+
							paramD.formatted("%7.5f")+")"
	
	override val chaosSystem = new ChaosSystem{
		val (a,b,c,d) = param
		//
		override def mapCoordinate( p:Vector[Double] ) : Vector[Double] = { map(p) }
		//
		override def mapDifference( p:Vector[Double] ) : Vector[Double] = {
			val (x,y) = (p.x , p.y)
			//
			val xx = a*x + b*y + (c*pi(x)+d)/(1+psi(x))
			val yy = -x
			//
			(xx,yy)
		}
		
		override def validateParam : Boolean = {
			// coefficient of pi
			if( abs(c) < 1e-7 ) return false
			
			true 
		}
	}
}
