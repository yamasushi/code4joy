// Chirikov standard map Strange Attractor
// 11:38 2009/11/04
//   http://www.scholarpedia.org/article/Chirikov_standard_map
//   http://mathworld.wolfram.com/StandardMap.html
//   http://en.wikipedia.org/wiki/Standard_map

import Math.{sin,cos,log,sqrt,abs,exp}

class DSChirikov( val header:String ,
		val paramK : Double ,
		val map    : ((Double,Double))=>(Double,Double) ,
		//
		val numRing:Int   ,
		val ovalR  : Double) extends ChaosStreamCanvas
{
	val period = 2*Math.Pi
	val radius = period*0.5
	def remainder(x:Double):Double = {
		val rem = Math.IEEEremainder( x , period )
		if(rem>=0) rem else rem + period
	}
	
	def remainder(p:(Double,Double)):(Double,Double) = {(remainder(p._1),remainder(p._2))}
	//
	override val initialPoints = PointsOfRing( ( 0 , 0 ) , numRing , period ).points map remainder 
	//
	override val chaosName = "csmchaos_" + header + "_" + 
						"("+paramK.formatted("%7.5f")+")" 
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference( xp:(Double,Double) ) : (Double,Double) = {
			val (x,p) = xp
			//
			val pp = remainder( p + paramK * sin(x) )  
			val xx = remainder( x + pp              )
			//
			(xx,pp)
		}
		override def mapCoordinate(xp:(Double,Double)) : (Double,Double) = {
			map(xp)
		}
		override def validateParam : Boolean = {
			// coefficient of pi
			if( paramK < 1e-7 ) return false
			
			true 
		}
	}
	
}

