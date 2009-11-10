// Koch pentaagon
// 2009-08-22 shuji yamamoto
// 2009-08-24
// 
package code4joy.scala

object IFSKochPenta extends IFSMap {
	//
	val scaleRatio   = ( 2*Math.cos(Math.Pi/5.) -1 )/(2*Math.cos(Math.Pi/5.))
	val offsetLength = scaleRatio*( Math.sin(3*Math.Pi/10.)/(2*Math.cos(3*Math.Pi/10.)) )
	val radianUnit   = (2*Math.Pi)/5.
	//
	override val numMaps = 5
	override def getMap(index:Int) : ((Double,Double))=>(Double,Double) = {
		index match {
			case i:Int if 0<=i && i<numMaps => { ( pt:(Double,Double) ) => 
				val (x,y) = pt
				(
					x*scaleRatio + Math.cos(radianUnit*i)*offsetLength , 
					y*scaleRatio + Math.sin(radianUnit*i)*offsetLength ) 
				}
			case _ => { pt:(Double,Double) => pt }
		}
	}
}
