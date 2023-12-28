// dragon curve & C curve
// 2009-08-22 shuji yamamoto
// 2009-08-24
//
package code4joy.scala

case class IFSDragon(val revFactor:(Double,Double) // (+1,-1):Dragon , (+1,+1):C Curve
					)	extends IFSMap {
	//
	val factor    :Double   = Math.sqrt(2.)/2.
	val radianUnit:Double   = Math.Pi/4.
	//
	override val numMaps = 2
	override def getMap(index:Int) : ((Double,Double)) => (Double,Double) = {
		index match {
			case 0 => { (pt:(Double,Double)) => 
				val (x,y) = pt
				(
					( x * Math.cos(radianUnit)  - (revFactor._1*y) * Math.sin(radianUnit) )*factor , 
					( x * Math.sin(radianUnit)  + (revFactor._1*y) * Math.cos(radianUnit) )*factor ) 
				}
			case 1 => { (pt:(Double,Double)) => 
				val (x,y) = pt
				(
					( x * Math.cos(-radianUnit) - (revFactor._2*y) * Math.sin(-radianUnit) )*factor   + 0.5 , 
					( x * Math.sin(-radianUnit) + (revFactor._2*y) * Math.cos(-radianUnit) )*factor   + 0.5 )
				}
			case _ => { pt:(Double,Double) => pt }
			
		}
	}
	//
}
