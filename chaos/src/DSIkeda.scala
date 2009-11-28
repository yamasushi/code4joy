// Ikeda Strange Attractor
// 2009-09-28


class DSIkeda(
		val paramAB:(Double,Double) , 
		val paramKP:(Double,Double) , 
		//
		val numRing:Int   ,
		val ovalR:Double) extends ChaosStreamCanvas
{
	val (paramA,paramB) = paramAB
	val (paramK,paramP) = paramKP
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR).points
	//
	override val chaosName = "ichaos_" + 
						"("+paramA.formatted("%7.5f")+","+paramB.formatted("%7.5f")+")" +
						"("+paramK.formatted("%7.5f")+","+paramP.formatted("%7.5f")+")"
	
	override val chaosSystem = new ChaosSystem {
			override def mapDifference( pt:(Double,Double) ) : (Double,Double) = {
			val (x,y) = pt
			//
			val tn    = paramK - paramP/(1 + (x*x + y*y) )
			val sinTn = Math.sin(tn)
			val cosTn = Math.cos(tn)
			(	paramB*( x*cosTn - y*sinTn ) + paramA , 
				paramB*( x*sinTn + y*cosTn ) )
		}
	}
	
}

