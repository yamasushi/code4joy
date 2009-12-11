import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow,exp}
import scala.collection.immutable._


case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Float]](imgGeom.size.x*2,imgGeom.size.y*2)
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double] , v:Double) : Double = {
		val pt = canvasTransform(p)
		val ix = ( (pt.x / 0.5       ) + 0.5 ).asInstanceOf[Int]
		val iy = ( (pt.y / sqrt(3.0) ) + 0.5 ).asInstanceOf[Int]
		//
		val result:Double = histgram.updateCell((ix,iy) , v.asInstanceOf[Float])
		result
	}
	//
	def foreach ( op: (Vector[Int],Float)=>Unit ) : Unit = {
		val rand  = new java.util.Random
		//
		for(	ix <- 0 until imgGeom.size.x-1 ; 
				iy <- 0 until imgGeom.size.y-1 ){
			val jx = ( (ix.asInstanceOf[Double] / 0.5      ) + rand.nextDouble ).asInstanceOf[Int]
			val jy = ( (iy.asInstanceOf[Double] / sqrt(3.0)) + rand.nextDouble ).asInstanceOf[Int]
			//
			op( (ix,iy) , histgram((jx,jy)) )
		}
	}
	//
	def smoothing( condOp:Float=>Boolean ) : Unit = {
		val jxMax = ( imgGeom.size.x / 0.5       ).asInstanceOf[Int]
		val jyMax = ( imgGeom.size.y / sqrt(3.0) ).asInstanceOf[Int]
		for(	jx <- 0 to jxMax ;
				jy <- 0 to jyMax ) {
			val h = histgram((jx,jy))
			if( condOp(h) ) {
				val nb = Lattice.neighbor((jx,jy))
				val histNb = ( (0.0f /: nb){(acc,v) => acc+histgram(v) } ) / nb.length
				//
				histgram.updateCell( (jx,jy) , histNb )
			}
		}
	}
	//
	def rendering(op:(Vector[Int],Double)=>Unit) : Unit = {
		//
		smoothing( { _:Float=>true } )
		smoothing( { _ < 1.0f } )
		smoothing( { _ == 0.0f } )
		//
		this.foreach { (ip,h) =>
			if( h>0 ){
				op(ip,h)
			}
		}
	}
	
	class ImageBuffer( buffer:Array[Array[Float]] )
	{
		def indexIsValid( ip:(Int,Int) ) : Boolean = {
			val (ix,iy) = ip
			//
			if ( ix < 0 ) return false
			if ( iy < 0 ) return false
			//
			if ( ix >= buffer.length     ) return false
			if ( iy >= buffer(ix).length ) return false
			//
			return true
		}
		
		def apply( ip:Vector[Int] ) : Float = apply((ip.x,ip.y))
		def apply( ip:(Int,Int) ) : Float = {
			if (indexIsValid(ip)) buffer(ip._1)(ip._2)
			else 0.0f
		}
		
		def updateCell( ip:(Int,Int) , v:Float ) : Float = {
			if (indexIsValid(ip)) {
				val h:Float = ( buffer(ip._1)(ip._2) + v ) * 0.5f
				buffer(ip._1)(ip._2) = h
				h
			}
			else {
				0.0f
			}
		}
	}
	//
	implicit def a2ib( a:Array[Array[Float]] ):ImageBuffer = new ImageBuffer(a)
}
