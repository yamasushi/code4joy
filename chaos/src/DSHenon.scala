// Henon Strange Attractor
// 2009-09-28

import Math.{sin,cos}

class DSHenon(
		val header:String           , 
		//
		val func  :(Double)=>Double , 
		val alpha :Double ,
		val map    : (Vector[Double])=>Vector[Double] ,
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
	def remainder(p:Vector[Double]):Vector[Double] = {Vector(remainder(p.x),remainder(p.y))}
	override val initialPoints = PointsOfRing( Vector( 0 , 0 ) , numRing , period ).points map remainder 
	//
	
	override val chaosName = "hchaos_" + header + "_" + 
						"("+alpha.formatted("%7.5fpi") + ")"
	
	override val chaosSystem = new ChaosSystem{
		val theta = alpha * Math.Pi
		val cosT = Math.cos(theta)
		val sinT = Math.sin(theta)
		//
		override def mapDifference( pt:Vector[Double] ) : Vector[Double] = {
			val (x,y) = (pt.x , pt.y)
			val temp  = y-func(x)
			val p = ( x*cosT - temp*sinT , x*sinT + temp*cosT )
			//
			remainder(p)
		}
		override def mapCoordinate(p:Vector[Double]) : Vector[Double] = {
			map(p) 
		}
	}
	
}

