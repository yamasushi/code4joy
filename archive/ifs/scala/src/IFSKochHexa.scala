// Koch hexagon
// 2009-08-22 shuji yamamoto
// 2009-08-24
//
package code4joy.scala

object IFSKochHexa  extends IFSMap{
	//
	val scaleRatio   = 1./3. 
	val offsetLength = Math.sqrt(3.) * scaleRatio
	val radianUnit   = Math.Pi/3.
	//
	override val numMaps = 6
	override def getMap(index:Int) : ((Double,Double))=>(Double,Double) = {
		index match {
			case i if 0<=i && i<numMaps => { (pt:(Double,Double)) => 
				val (x,y) = pt
				(
					x*scaleRatio + Math.cos(radianUnit*i)*offsetLength , 
					y*scaleRatio + Math.sin(radianUnit*i)*offsetLength )
				}
			case _ => { pt:(Double,Double) => pt }
			
		}
	}
	//
}
