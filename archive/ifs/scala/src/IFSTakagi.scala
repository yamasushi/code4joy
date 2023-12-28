// Koch takagi curve
// 2009-08-22 shuji yamamoto
// 2009-08-24
package code4joy.scala

object IFSTakagi extends IFSMap {
	//
	val factor    :Double   = 0.5
	//
	override val numMaps = 2
	override def getMap(index:Int) : ((Double,Double))=>(Double,Double) = {
		index match {
			case 0 => { (pt:(Double,Double)) => 
				val (x,y) = pt
				( 0.5*x          , 0.5*(y+x)      )
				}
			case 1 => { (pt:(Double,Double)) => 
				val (x,y) = pt
				( 0.5*x    +0.5  , 0.5*(y-x) +0.5 )
				}
			case _ => { pt:(Double,Double) => pt }
			
		}
	}
}
