// Gumowski-Mira Strange Attractor
// 2009-09-09
// 2009-09-11
// 2009-09-28
// 10:25 2009/10/13
// 15:28 2009/10/16
// 
// see:
//   http://www.scipress.org/journals/forma/pdf/1502/15020121.pdf
//   http://math.cmaisonneuve.qc.ca/alevesque/chaos_fract/Attracteurs/Attracteurs.html// see also:

class DSGumowskiMira( val header:String ,
		//
		val phi      :((Double)=>Double) , 
		val paramAB  :(Double,Double)    ,
		val paramMuNu:(Double,Double)    , 
		//
		val numRing:Int   ,
		val ovalR:Double) extends ChaosStreamCanvas
{
	val (paramA,paramB) = paramAB
	val (mu    ,nu)     = paramMuNu
	override val initialPoints = PointsOfRing(Vector(0,0),numRing,ovalR).points
	//
	override val chaosName = "gmchaos_" + header + "_" + 
						"("+paramA.formatted("%7.5f")+","+paramB.formatted("%7.5f")+")" +
						"("+mu    .formatted("%7.5f")+","+nu    .formatted("%7.5f")+")" 
	
	override val chaosSystem = new ChaosSystem {
		def gmF(x:Double) : Double = {
			mu * x + 2*(1-mu)*phi(x)
		}
		//
		def gmG(y:Double) : Double = {
			nu *y + paramA * (1-paramB*y*y)*y
		}
		//
		override def mapDifference( p:Vector[Double] ) : Vector[Double] = {
			val (x,y) = (p.x,p.y)
			//
			val xx = gmG(y) + gmF(x)
			val yy = -x + gmF(xx)
			//
			(xx,yy)
		}
	}
}

