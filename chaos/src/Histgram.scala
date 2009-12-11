import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow,exp}
import scala.collection.immutable._

case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Double]](imgGeom.size.x,imgGeom.size.y)
	val eps = 0.5
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double],v:Double) : Double = {
		var result = 0.0
		//
		val pt = canvasTransform(p)
		//
		val ip:Vector[Int] = pt map { t => (t+0.5).asInstanceOf[Int] }
		//
		update((ip.x,ip.y) , v)
	}
	//
	private def update(ip:(Int,Int) , v:Double) : Double = {
		val (ix,iy) = ip
		if ( ix < 0 ) return 0.0
		if ( iy < 0 ) return 0.0
		if ( ix >= imgGeom.size.x ) return 0.0
		if ( iy >= imgGeom.size.y ) return 0.0
		//
		val hist = ( histgram(ix)(iy) + v )*0.5 
		histgram(ix)(iy) = hist
		hist
	}
	//
	def foreach ( op: (Vector[Int],Double)=>Unit ) : Unit = {
		for(ix<- 0 until imgGeom.size.x ; iy <- 0 until imgGeom.size.y ){
			op((ix,iy),histgram(ix)(iy))
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
