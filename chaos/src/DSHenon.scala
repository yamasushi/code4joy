// Henon Strange Attractor
// 2009-09-28

import Math.{sin,cos}

class DSHenon(
		val header:String           , 
		//
		val func  :(Double)=>Double , 
		val alpha :Double ,
		val map    : ((Double,Double))=>(Double,Double) ,
		//
		val numRing:Int   ,
		val period:Double ) extends ChaosStreamCanvas
{
	val radius = period*0.5
	def remainder(x:Double):Double = {
		val rem = Math.IEEEremainder(x,period)
		//if(rem>=0) rem else rem + period
		rem
	}
	def remainder(p:(Double,Double)):(Double,Double) = {(remainder(p._1),remainder(p._2))}
	override val initialPoints = PointsOfRing( ( 0 , 0 ) , numRing , period ).points map remainder 
	//
	
	override val chaosName = "hchaos_" + header + "_" + 
						"("+alpha.formatted("%7.5fpi") + ")"
	
	override val chaosSystem = new ChaosSystem{
		val theta = alpha * Math.Pi
		val cosT = Math.cos(theta)
		val sinT = Math.sin(theta)
		//
		override def mapDifference( pt:(Double,Double) ) : (Double,Double) = {
			val (x,y) = pt
			val temp  = y-func(x)
			val p = ( x*cosT - temp*sinT , x*sinT + temp*cosT )
			//
			remainder(p)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			map(p) 
		}
	}
	
}

