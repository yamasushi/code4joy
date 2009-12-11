import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow,exp}
import scala.collection.immutable._

case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Double]](imgGeom.size.x*2,imgGeom.size.y*2)
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double],v:Double) : Double = {
		val pt = canvasTransform(p)
		val ix = ( (pt.x / 0.5       ) + 0.5 ).asInstanceOf[Int]
		val iy = ( (pt.y / sqrt(3.0) ) + 0.5 ).asInstanceOf[Int]
		update((ix,iy) , v)
	}
	//
	private def update(ip:(Int,Int) , v:Double) : Double = {
		val (ix,iy) = ip
		if ( ix < 0 ) return 0.0
		if ( iy < 0 ) return 0.0
		if ( ix >= imgGeom.size.x*2 ) return 0.0
		if ( iy >= imgGeom.size.y*2 ) return 0.0
		//
		val hist = ( histgram(ix)(iy) + v )*0.5 
		histgram(ix)(iy) = hist
		hist
	}
	//
	private def histgramAt(ip:(Int,Int)) : Double = {
		val (ix,iy) = ip
		if ( ix < 0 ) return 0.0
		if ( iy < 0 ) return 0.0
		if ( ix >= imgGeom.size.x*2 ) return 0.0
		if ( iy >= imgGeom.size.y*2 ) return 0.0
		//
		return histgram(ix)(iy)
	}
	//
	def foreach ( op: (Vector[Int],Double)=>Unit ) : Unit = {
		for(ix<- 0 until imgGeom.size.x ; iy <- 0 until imgGeom.size.y ){
			val jx = ( (ix.asInstanceOf[Double] / 0.5      ) + 0.5 ).asInstanceOf[Int]
			val jy = ( (iy.asInstanceOf[Double] / sqrt(3.0)) + 0.5 ).asInstanceOf[Int]
			//
			val nb = Lattice.neighbor( (jx,jy) )
			val histNb = ( (0.0 /: nb){(acc,v) => acc+histgramAt(v) } ) / nb.length
			//
			op( (ix,iy) , (histgram(jx)(jy)+histNb)*0.5 )
		}
	}
	//
	def rendering(samplingDegree:Int)(op:(Vector[Int],Double)=>Unit) : Unit = {
		this.foreach { (ip,h) =>
			if( h>0 ){
				op(ip,h)
			}
		}
	}
}
